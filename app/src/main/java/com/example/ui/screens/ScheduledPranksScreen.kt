package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PrankItem
import com.example.model.PresetData
import com.example.ui.PrankViewModel
import com.example.ui.theme.BrandAlert
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSuccess
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScheduledPranksScreen(
    pranks: List<PrankItem>,
    viewModel: PrankViewModel,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Live clock ticker for countdowns
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    val scheduledList = remember(pranks, currentTimeMillis) {
        pranks.filter { it.status == "SCHEDULED" && it.scheduledTimeMillis > currentTimeMillis }
    }

    val pastList = remember(pranks, currentTimeMillis) {
        pranks.filter { it.status != "SCHEDULED" || it.scheduledTimeMillis <= currentTimeMillis }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "শিডিউল ও মেসেজ হিস্ট্রি",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "কোন মেসেজগুলো কখন ফোনে আসবে দেখুন",
                    fontSize = 12.sp,
                    color = Slate600
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${scheduledList.size} অপেক্ষমাণ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (pranks.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Slate100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Slate600,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "এখনও কোনো মেসেজ শিডিউল করা হয়নি",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "প্রথম ট্যাবে গিয়ে বন্ধুদের চমকে দেওয়ার মতো একটি প্র্যাঙ্ক মেসেজ সেট করুন!",
                        fontSize = 13.sp,
                        color = Slate600,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onNavigateToCreate,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "নতুন প্র্যাঙ্ক তৈরি করুন",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Active Section
                if (scheduledList.isNotEmpty()) {
                    item {
                        Text(
                            text = "⏳ নির্ধারিত অপেক্ষমাণ মেসেজ (Active Countdown)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(scheduledList, key = { it.id }) { item ->
                        ScheduledPrankCard(
                            item = item,
                            currentTimeMillis = currentTimeMillis,
                            onCancel = { viewModel.cancelPrank(context, item.id) },
                            onDelete = { viewModel.deletePrank(item.id) },
                            onResend = { viewModel.resendPrank(context, item) }
                        )
                    }
                }

                // History Section
                if (pastList.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "📜 পূর্বের মেসেজসমূহ (History)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate700,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(pastList, key = { it.id }) { item ->
                        ScheduledPrankCard(
                            item = item,
                            currentTimeMillis = currentTimeMillis,
                            onCancel = { viewModel.cancelPrank(context, item.id) },
                            onDelete = { viewModel.deletePrank(item.id) },
                            onResend = { viewModel.resendPrank(context, item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduledPrankCard(
    item: PrankItem,
    currentTimeMillis: Long,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    onResend: () -> Unit
) {
    val preset = remember(item.appType) {
        PresetData.presets.find { it.id == item.appType } ?: PresetData.presets.first()
    }

    val isPending = item.status == "SCHEDULED" && item.scheduledTimeMillis > currentTimeMillis
    val diffSec = Math.max(0, (item.scheduledTimeMillis - currentTimeMillis) / 1000)

    val timeFormatted = remember(item.scheduledTimeMillis) {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        sdf.format(Date(item.scheduledTimeMillis))
    }

    val internalBitmap = remember(item.imagePath) {
        if (!item.imagePath.isNullOrEmpty()) {
            val f = File(item.imagePath)
            if (f.exists()) BitmapFactory.decodeFile(f.absolutePath) else null
        } else null
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (isPending) MaterialTheme.colorScheme.primaryContainer else Slate200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: App icon + Sender + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(preset.brandColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = preset.iconResId),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.senderName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.appLabel} • সময়: $timeFormatted",
                        fontSize = 11.sp,
                        color = Slate600
                    )
                }

                // Status Chip
                if (isPending) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${diffSec}s বাকি",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                        }
                    }
                } else {
                    Surface(
                        color = if (item.status == "CANCELLED") Color(0xFFFEE2E2) else Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (item.status == "CANCELLED") "বাতিল" else "পাঠানো হয়েছে ✅",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (item.status == "CANCELLED") BrandAlert else BrandSuccess,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body & Thumbnail
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (item.title.isNotBlank() && item.title != item.senderName) {
                        Text(
                            text = item.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate800,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = item.message,
                        fontSize = 12.sp,
                        color = Slate700,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Thumbnail if attached
                val hasPhoto = internalBitmap != null || item.presetImageResId != null
                if (hasPhoto) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                    ) {
                        when {
                            internalBitmap != null -> {
                                Image(
                                    bitmap = internalBitmap.asImageBitmap(),
                                    contentDescription = "Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            item.presetImageResId != null -> {
                                Image(
                                    painter = painterResource(id = item.presetImageResId),
                                    contentDescription = "Preset Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Card Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isPending) {
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandAlert),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel",
                            tint = BrandAlert,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "বাতিল করুন", fontSize = 11.sp, color = BrandAlert)
                    }
                } else {
                    OutlinedButton(
                        onClick = onResend,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandPrimary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Re-send",
                            tint = BrandPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "পুনরায় পাঠান (১০s)", fontSize = 11.sp, color = BrandPrimary)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Slate600,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
