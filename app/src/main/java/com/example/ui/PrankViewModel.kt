package com.example.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.PrankRepository
import com.example.model.AppPreset
import com.example.model.PrankItem
import com.example.model.PresetData
import com.example.util.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

enum class DelayOption(val labelBn: String, val labelEn: String, val seconds: Long) {
    SEC_5("৫ সেকেন্ড", "5 sec", 5),
    SEC_15("১৫ সেকেন্ড", "15 sec", 15),
    SEC_30("৩০ সেকেন্ড", "30 sec", 30),
    MIN_1("১ মিনিট", "1 min", 60),
    MIN_5("৫ মিনিট", "5 min", 300),
    CUSTOM("কাস্টম সময়", "Custom", -1)
}

data class CreatePrankUiState(
    val selectedPresetId: String = "whatsapp",
    val customSenderName: String = "WhatsApp",
    val customTitle: String = "Mahi",
    val messageText: String = "Can you talk right now? Call me please urgent!",
    val selectedDelayOption: DelayOption = DelayOption.SEC_15,
    val customHour: Int = 12,
    val customMinute: Int = 0,
    val attachedImageUri: Uri? = null,
    val attachedInternalPath: String? = null,
    val selectedPresetImageResId: Int? = null,
    val snackbarMessage: String? = null,
    val showPermissionDialog: Boolean = false,
    val isPermissionGranted: Boolean = false
)

class PrankViewModel(private val repository: PrankRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePrankUiState())
    val uiState: StateFlow<CreatePrankUiState> = _uiState.asStateFlow()

    val savedPranks: StateFlow<List<PrankItem>> = repository.pranks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentTab = MutableStateFlow(0) // 0: Create, 1: Scheduled/History, 2: Templates
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    init {
        // Initialize custom time to current time + 5 minutes
        val now = Calendar.getInstance()
        now.add(Calendar.MINUTE, 5)
        _uiState.value = _uiState.value.copy(
            customHour = now.get(Calendar.HOUR_OF_DAY),
            customMinute = now.get(Calendar.MINUTE)
        )
    }

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    fun setPermissionGranted(granted: Boolean) {
        _uiState.value = _uiState.value.copy(
            isPermissionGranted = granted,
            showPermissionDialog = !granted
        )
    }

    fun dismissPermissionDialog() {
        _uiState.value = _uiState.value.copy(showPermissionDialog = false)
    }

    fun selectPreset(preset: AppPreset) {
        _uiState.value = _uiState.value.copy(
            selectedPresetId = preset.id,
            customSenderName = preset.defaultSender,
            customTitle = preset.defaultTitle,
            messageText = preset.defaultMessage
        )
    }

    fun updateSenderName(name: String) {
        _uiState.value = _uiState.value.copy(customSenderName = name)
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(customTitle = title)
    }

    fun updateMessage(text: String) {
        _uiState.value = _uiState.value.copy(messageText = text)
    }

    fun selectDelayOption(option: DelayOption) {
        _uiState.value = _uiState.value.copy(selectedDelayOption = option)
    }

    fun setCustomTime(hour: Int, minute: Int) {
        _uiState.value = _uiState.value.copy(
            customHour = hour,
            customMinute = minute,
            selectedDelayOption = DelayOption.CUSTOM
        )
    }

    fun onImagePicked(context: Context, uri: Uri?) {
        if (uri == null) return
        val path = repository.saveUriToInternalStorage(uri)
        _uiState.value = _uiState.value.copy(
            attachedImageUri = uri,
            attachedInternalPath = path,
            selectedPresetImageResId = null // clear preset image when user picks custom
        )
    }

    fun selectPresetImage(resId: Int?) {
        _uiState.value = _uiState.value.copy(
            selectedPresetImageResId = resId,
            attachedImageUri = null,
            attachedInternalPath = null
        )
    }

    fun clearAttachedImage() {
        _uiState.value = _uiState.value.copy(
            attachedImageUri = null,
            attachedInternalPath = null,
            selectedPresetImageResId = null
        )
    }

    fun applyTemplate(template: PresetData.PrankTemplate) {
        val preset = PresetData.presets.find { it.id == template.appPresetId }
            ?: PresetData.presets.first()
        _uiState.value = _uiState.value.copy(
            selectedPresetId = preset.id,
            customSenderName = template.sender,
            customTitle = template.title,
            messageText = template.message,
            selectedPresetImageResId = template.presetImageResId,
            attachedImageUri = null,
            attachedInternalPath = null
        )
        _currentTab.value = 0 // Switch back to editor
        showSnackbar("টেমপ্লেট সিলেক্ট করা হয়েছে! এবার সময় সেট করুন।")
    }

    fun schedulePrank(context: Context) {
        val state = _uiState.value
        val preset = PresetData.presets.find { it.id == state.selectedPresetId }
            ?: PresetData.presets.first()

        val sender = if (state.customSenderName.isNotBlank()) state.customSenderName else preset.defaultSender
        val title = if (state.customTitle.isNotBlank()) state.customTitle else sender
        val message = if (state.messageText.isNotBlank()) state.messageText else "Hello!"

        val targetTimeMillis = when (state.selectedDelayOption) {
            DelayOption.CUSTOM -> {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, state.customHour)
                    set(Calendar.MINUTE, state.customMinute)
                    set(Calendar.SECOND, 0)
                }
                // If scheduled time is earlier than right now, schedule for tomorrow
                if (cal.timeInMillis <= System.currentTimeMillis()) {
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                }
                cal.timeInMillis
            }
            else -> {
                System.currentTimeMillis() + (state.selectedDelayOption.seconds * 1000L)
            }
        }

        val item = PrankItem(
            id = UUID.randomUUID().toString(),
            senderName = sender,
            appType = preset.id,
            appLabel = preset.nameEn,
            title = title,
            message = message,
            imagePath = state.attachedInternalPath,
            presetImageResId = state.selectedPresetImageResId,
            scheduledTimeMillis = targetTimeMillis,
            status = "SCHEDULED"
        )

        repository.addPrank(item)
        NotificationHelper.scheduleNotification(context, item)

        val diffSec = Math.max(1, (targetTimeMillis - System.currentTimeMillis()) / 1000)
        val timeDesc = if (state.selectedDelayOption == DelayOption.CUSTOM) {
            String.format("%02d:%02d এ", state.customHour, state.customMinute)
        } else {
            "${diffSec} সেকেন্ড পর"
        }
        showSnackbar("মেসেজ সফলভাবে শিডিউল করা হয়েছে ($timeDesc আসবে) 🚀")
        _currentTab.value = 1 // Switch to Scheduled tab to show countdown
    }

    fun triggerInstantTest(context: Context) {
        val state = _uiState.value
        val preset = PresetData.presets.find { it.id == state.selectedPresetId }
            ?: PresetData.presets.first()

        val sender = if (state.customSenderName.isNotBlank()) state.customSenderName else preset.defaultSender
        val title = if (state.customTitle.isNotBlank()) state.customTitle else sender
        val message = if (state.messageText.isNotBlank()) state.messageText else "Instant Test!"

        viewModelScope.launch {
            showSnackbar("৩ সেকেন্ডের মধ্যে টেস্ট নোটিফিকেশন আসছে... 🔔")
            delay(3000)
            NotificationHelper.showNotification(
                context = context,
                id = UUID.randomUUID().toString(),
                senderName = sender,
                appType = preset.id,
                appLabel = preset.nameEn,
                title = title,
                message = message,
                imagePath = state.attachedInternalPath,
                presetImageResId = state.selectedPresetImageResId
            )
        }
    }

    fun cancelPrank(context: Context, prankId: String) {
        NotificationHelper.cancelScheduledNotification(context, prankId)
        repository.updateStatus(prankId, "CANCELLED")
        showSnackbar("শিডিউল বাতিল করা হয়েছে!")
    }

    fun deletePrank(prankId: String) {
        repository.deletePrank(prankId)
        showSnackbar("তালিকায় থেকে মুছে ফেলা হয়েছে!")
    }

    fun resendPrank(context: Context, item: PrankItem) {
        val newItem = item.copy(
            id = UUID.randomUUID().toString(),
            scheduledTimeMillis = System.currentTimeMillis() + 10000L, // 10 seconds
            status = "SCHEDULED",
            createdAt = System.currentTimeMillis()
        )
        repository.addPrank(newItem)
        NotificationHelper.scheduleNotification(context, newItem)
        showSnackbar("পুনরায় ১০ সেকেন্ড পরে শিডিউল করা হলো! ⏱️")
    }

    fun showSnackbar(message: String) {
        _uiState.value = _uiState.value.copy(snackbarMessage = message)
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
}
