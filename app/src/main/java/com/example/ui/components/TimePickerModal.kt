package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun TimePickerModal(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }
    var isAm by remember { mutableStateOf(initialHour < 12) }

    var display12Hour by remember {
        val h12 = if (initialHour % 12 == 0) 12 else initialHour % 12
        mutableIntStateOf(h12)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "মেসেজ আসার সময় নির্ধারণ করুন",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "কখন মেসেজটি মোবাইলের পর্দায় আসবে?",
                    fontSize = 13.sp,
                    color = Slate600
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hour Box
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = {
                                display12Hour = if (display12Hour >= 12) 1 else display12Hour + 1
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Hour Up")
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Slate100,
                            modifier = Modifier.size(width = 64.dp, height = 56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = String.format("%02d", display12Hour),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                display12Hour = if (display12Hour <= 1) 12 else display12Hour - 1
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Hour Down")
                        }

                        Text(text = "ঘণ্টা (Hour)", fontSize = 11.sp, color = Slate600)
                    }

                    Text(
                        text = ":",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 24.dp)
                    )

                    // Minute Box
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = {
                                minute = (minute + 1) % 60
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Minute Up")
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Slate100,
                            modifier = Modifier.size(width = 64.dp, height = 56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = String.format("%02d", minute),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                minute = if (minute <= 0) 59 else minute - 1
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Minute Down")
                        }

                        Text(text = "মিনিট (Min)", fontSize = 11.sp, color = Slate600)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // AM / PM Toggle
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isAm) MaterialTheme.colorScheme.primary else Slate100,
                            modifier = Modifier
                                .clickable { isAm = true }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = "AM",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAm) Color.White else Slate700,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (!isAm) MaterialTheme.colorScheme.primary else Slate100,
                            modifier = Modifier
                                .clickable { isAm = false }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = "PM",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isAm) Color.White else Slate700,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val final24Hour = when {
                        isAm && display12Hour == 12 -> 0
                        !isAm && display12Hour != 12 -> display12Hour + 12
                        else -> display12Hour
                    }
                    onTimeSelected(final24Hour, minute)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "সেট করুন (Set Time)", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "বাতিল", color = Slate600)
            }
        }
    )
}
