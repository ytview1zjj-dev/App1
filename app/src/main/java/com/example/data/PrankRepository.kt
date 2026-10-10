package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.model.PrankItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class PrankRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("notify_prank_prefs", Context.MODE_PRIVATE)

    private val _pranks = MutableStateFlow<List<PrankItem>>(emptyList())
    val pranks: StateFlow<List<PrankItem>> = _pranks.asStateFlow()

    init {
        loadPranks()
    }

    private fun loadPranks() {
        val jsonStr = prefs.getString("saved_pranks", null) ?: "[]"
        try {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<PrankItem>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    PrankItem(
                        id = obj.getString("id"),
                        senderName = obj.getString("senderName"),
                        appType = obj.getString("appType"),
                        appLabel = obj.optString("appLabel", obj.getString("senderName")),
                        title = obj.getString("title"),
                        message = obj.getString("message"),
                        imagePath = if (obj.has("imagePath") && !obj.isNull("imagePath")) obj.getString("imagePath") else null,
                        presetImageResId = if (obj.has("presetImageResId") && !obj.isNull("presetImageResId")) obj.getInt("presetImageResId") else null,
                        scheduledTimeMillis = obj.getLong("scheduledTimeMillis"),
                        status = obj.optString("status", "SCHEDULED"),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }
            _pranks.value = list.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            _pranks.value = emptyList()
        }
    }

    private fun savePranks(list: List<PrankItem>) {
        try {
            val jsonArray = JSONArray()
            list.forEach { item ->
                val obj = JSONObject().apply {
                    put("id", item.id)
                    put("senderName", item.senderName)
                    put("appType", item.appType)
                    put("appLabel", item.appLabel)
                    put("title", item.title)
                    put("message", item.message)
                    put("imagePath", item.imagePath)
                    put("presetImageResId", item.presetImageResId ?: JSONObject.NULL)
                    put("scheduledTimeMillis", item.scheduledTimeMillis)
                    put("status", item.status)
                    put("createdAt", item.createdAt)
                }
                jsonArray.put(obj)
            }
            prefs.edit().putString("saved_pranks", jsonArray.toString()).apply()
            _pranks.value = list.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addPrank(item: PrankItem) {
        val current = _pranks.value.toMutableList()
        current.removeAll { it.id == item.id }
        current.add(0, item)
        savePranks(current)
    }

    fun updateStatus(id: String, newStatus: String) {
        val current = _pranks.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = current[index].copy(status = newStatus)
            current[index] = updated
            savePranks(current)
        }
    }

    fun deletePrank(id: String) {
        val current = _pranks.value.toMutableList()
        val item = current.find { it.id == id }
        if (item?.imagePath != null) {
            try {
                val f = File(item.imagePath)
                if (f.exists()) f.delete()
            } catch (_: Exception) {}
        }
        current.removeAll { it.id == id }
        savePranks(current)
    }

    fun clearAll() {
        savePranks(emptyList())
    }

    /**
     * Copies an external content Uri to internal storage file so it can be accessed
     * reliably by background AlarmReceiver and NotificationManager.
     */
    fun saveUriToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val fileDir = File(context.filesDir, "prank_images").apply { mkdirs() }
            val fileName = "prank_img_${UUID.randomUUID()}.jpg"
            val destFile = File(fileDir, fileName)

            inputStream?.use { input ->
                // Decode and re-compress to limit size and ensure valid bitmap
                val bitmap = BitmapFactory.decodeStream(input)
                if (bitmap != null) {
                    FileOutputStream(destFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    destFile.absolutePath
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
