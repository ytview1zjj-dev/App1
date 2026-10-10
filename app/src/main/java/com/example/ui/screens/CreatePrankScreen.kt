package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.AppPreset
import com.example.model.PresetData
import com.example.ui.CreatePrankUiState
import com.example.ui.DelayOption
import com.example.ui.PrankViewModel
import com.example.ui.components.LiveNotificationPreview
import com.example.ui.components.PermissionBanner
import com.example.ui.components.TimePickerModal
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSuccess
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun CreatePrankScreen(
    state: CreatePrankUiState,
    viewModel: PrankViewModel,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showTimePickerModal by remember { mutableStateOf(false) }

    // Android 13+ Zero-Permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onImagePicked(context, uri)
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Hero Visual Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(130.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Hero Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC0F172A))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "🎭 প্র্যাঙ্ক মেসেজ ক্রিয়েটর",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "আসল নোটিফিকেশনের মতো মেসেজ শিডিউল করে বন্ধুদের বোকা বানান!",
                        fontSize = 11.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Permission Banner
        PermissionBanner(
            isGranted = state.isPermissionGranted,
            onRequestPermission = onRequestPermission
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Live Notification Preview
        LiveNotificationPreview(
            presetId = state.selectedPresetId,
            senderName = state.customSenderName,
            title = state.customTitle,
            message = state.messageText,
            imageUri = state.attachedImageUri,
            imagePath = state.attachedInternalPath,
            presetImageResId = state.selectedPresetImageResId
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Select App & Logo Section
        Text(
            text = "১. মেসেজের লোগো ও অ্যাপ সিলেক্ট করুন",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
        Text(
            text = "কোন অ্যাপের মতো নোটিফিকেশন দেখাতে চান?",
            fontSize = 12.sp,
            color = Slate600
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(PresetData.presets) { preset ->
                val isSelected = preset.id == state.selectedPresetId
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { viewModel.selectPreset(preset) },
                    color = if (isSelected) Color(preset.brandColorHex).copy(alpha = 0.12f) else Slate100,
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(preset.brandColorHex) else Slate200
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(84.dp)
                            .padding(vertical = 10.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                contentDescription = preset.nameEn,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = preset.nameEn,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(preset.brandColorHex) else Slate900,
                            maxLines = 1
                        )

                        Text(
                            text = preset.nameBn,
                            fontSize = 9.sp,
                            color = Slate600,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Compose Message Content Section
        Text(
            text = "২. মেসেজের বিবরণ লিখুন",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
        Text(
            text = "কে পাঠিয়েছে এবং কী লেখা থাকবে তা লিখুন",
            fontSize = 12.sp,
            color = Slate600
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Sender Name Input
        OutlinedTextField(
            value = state.customSenderName,
            onValueChange = { viewModel.updateSenderName(it) },
            label = { Text("প্রেরকের নাম (Sender Name)") },
            placeholder = { Text("যেমন: WhatsApp, আম্মু, Crush ❤️, পুলিশ") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Slate100,
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Slate200
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Title / Subject Input
        OutlinedTextField(
            value = state.customTitle,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("মেসেজ টাইটেল / বিষয় (Message Title)") },
            placeholder = { Text("যেমন: জরুরি কল, টাকা জমা হয়েছে, Where are you?") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Slate100,
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Slate200
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Message Body Input
        OutlinedTextField(
            value = state.messageText,
            onValueChange = { viewModel.updateMessage(it) },
            label = { Text("মূল মেসেজ (Message Content)") },
            placeholder = { Text("যা লিখে প্র্যাঙ্ক করতে চান এখানে লিখুন...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Slate100,
                focusedBorderColor = BrandPrimary,
                unfocusedBorderColor = Slate200
            )
        )

        // Quick Emoji Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val quickEmojis = listOf("💰", "❤️", "🚨", "🎉", "🌹", "😂", "🔥", "😱", "💼", "😡")
            quickEmojis.forEach { emoji ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate100,
                    modifier = Modifier.clickable {
                        viewModel.updateMessage(state.messageText + " " + emoji)
                    }
                ) {
                    Text(
                        text = emoji,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Optional Photo Attachment Section
        // User requested: "মেসেজ দিয়ে কোন ছবি দিতে চাইলে ইচ্ছা হলেই দিব না দিলে নাই"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "৩. ছবি যুক্ত করুন (ঐচ্ছিক)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "ইচ্ছা হলে ছবি দিবেন, না দিলে নেই",
                    fontSize = 12.sp,
                    color = Slate600
                )
            }

            val hasImage = state.attachedImageUri != null || state.selectedPresetImageResId != null
            if (hasImage) {
                OutlinedButton(
                    onClick = { viewModel.clearAttachedImage() },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "ছবি মুছুন", fontSize = 11.sp, color = Color(0xFFEF4444))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Image Selection Chips and Options
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Pick from Gallery Button
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Slate100,
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Pick Image",
                        tint = BrandPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "গ্যালারি থেকে নিন",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900
                    )
                }
            }

            // Fun Prank Preset Photos
            // Preset 1: Money
            val isMoneySelected = state.selectedPresetImageResId == R.drawable.img_prank_money
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isMoneySelected) Color(0xFFECFDF5) else Slate100,
                border = BorderStroke(
                    width = if (isMoneySelected) 2.dp else 1.dp,
                    color = if (isMoneySelected) BrandSuccess else Slate200
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.selectPresetImage(R.drawable.img_prank_money) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_prank_money),
                        contentDescription = "Money",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "টাকার বান্ডিল 💰", fontSize = 12.sp, color = Slate900)
                }
            }

            // Preset 2: Rose
            val isRoseSelected = state.selectedPresetImageResId == R.drawable.img_prank_rose
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isRoseSelected) Color(0xFFFFF1F2) else Slate100,
                border = BorderStroke(
                    width = if (isRoseSelected) 2.dp else 1.dp,
                    color = if (isRoseSelected) Color(0xFFF43F5E) else Slate200
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.selectPresetImage(R.drawable.img_prank_rose) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_prank_rose),
                        contentDescription = "Rose",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "লাল গোলাপ 🌹", fontSize = 12.sp, color = Slate900)
                }
            }

            // Preset 3: Police Siren
            val isPoliceSelected = state.selectedPresetImageResId == R.drawable.img_prank_police
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isPoliceSelected) Color(0xFFFEF2F2) else Slate100,
                border = BorderStroke(
                    width = if (isPoliceSelected) 2.dp else 1.dp,
                    color = if (isPoliceSelected) Color(0xFFEF4444) else Slate200
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.selectPresetImage(R.drawable.img_prank_police) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_prank_police),
                        contentDescription = "Police",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "পুলিশ সাইরেন 🚨", fontSize = 12.sp, color = Slate900)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5. Schedule Delivery Time Section
        // User requested: "কয়টার দিকে মেসেজটা আমাদের মোবাইলে আসবে সেটা দিতে পারব"
        Text(
            text = "৪. কয়টার দিকে মেসেজ আসবে নির্ধারণ করুন",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
        Text(
            text = "মেসেজটি আসার নির্ধারিত সময় বা কাউন্টডাউন নির্বাচন করুন",
            fontSize = 12.sp,
            color = Slate600
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Delay Option Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DelayOption.values().forEach { option ->
                val isSelected = state.selectedDelayOption == option
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (option == DelayOption.CUSTOM) {
                            showTimePickerModal = true
                        } else {
                            viewModel.selectDelayOption(option)
                        }
                    },
                    label = {
                        Text(
                            text = if (option == DelayOption.CUSTOM) {
                                if (state.selectedDelayOption == DelayOption.CUSTOM) {
                                    String.format("নির্দিষ্ট সময় (%02d:%02d)", state.customHour, state.customMinute)
                                } else {
                                    "নির্দিষ্ট সময় ⏰"
                                }
                            } else {
                                "${option.labelBn} (${option.labelEn})"
                            },
                            fontSize = 12.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Action Buttons
        // Primary Button: Schedule
        Button(
            onClick = { viewModel.schedulePrank(context) },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "প্র্যাঙ্ক মেসেজ শিডিউল করুন 🚀",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Instant Test Button
        OutlinedButton(
            onClick = { viewModel.triggerInstantTest(context) },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, BrandPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = BrandPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "তাৎক্ষণিক টেস্ট দেখুন (৩ সেকেন্ডে নোটিফিকেশন আসবে)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandPrimary
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    if (showTimePickerModal) {
        TimePickerModal(
            initialHour = state.customHour,
            initialMinute = state.customMinute,
            onTimeSelected = { hour, minute ->
                viewModel.setCustomTime(hour, minute)
                showTimePickerModal = false
            },
            onDismiss = { showTimePickerModal = false }
        )
    }
}
