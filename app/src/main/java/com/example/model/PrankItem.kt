package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

data class PrankItem(
    val id: String,
    val senderName: String,
    val appType: String,
    val appLabel: String,
    val title: String,
    val message: String,
    val imagePath: String? = null,
    val presetImageResId: Int? = null,
    val scheduledTimeMillis: Long,
    val status: String = "SCHEDULED", // "SCHEDULED", "DELIVERED", "CANCELLED"
    val createdAt: Long = System.currentTimeMillis()
)

data class AppPreset(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val defaultSender: String,
    val brandColorHex: Long,
    @DrawableRes val iconResId: Int,
    val defaultTitle: String,
    val defaultMessage: String
)

object PresetData {
    val presets = listOf(
        AppPreset(
            id = "whatsapp",
            nameEn = "WhatsApp",
            nameBn = "হোয়াটসঅ্যাপ",
            defaultSender = "WhatsApp",
            brandColorHex = 0xFF25D366,
            iconResId = R.drawable.ic_notification_prank,
            defaultTitle = "Mahi",
            defaultMessage = "Can you talk right now? Call me please urgent!"
        ),
        AppPreset(
            id = "messenger",
            nameEn = "Messenger",
            nameBn = "মেসেঞ্জার",
            defaultSender = "Messenger",
            brandColorHex = 0xFF0084FF,
            iconResId = R.drawable.ic_notification_prank,
            defaultTitle = "Crush ❤️",
            defaultMessage = "I have something really important to tell you..."
        ),
        AppPreset(
            id = "bank",
            nameEn = "Bank / Cash",
            nameBn = "ব্যাংক / টাকা জমা",
            defaultSender = "City Bank Alert",
            brandColorHex = 0xFF10B981,
            iconResId = R.drawable.ic_notification_bank,
            defaultTitle = "A/C Credited!",
            defaultMessage = "Tk 50,000.00 credited to your A/C. Ref: Bonus Reward."
        ),
        AppPreset(
            id = "love",
            nameEn = "Love / Crush",
            nameBn = "ভালোবাসা / ক্রাশ",
            defaultSender = "My Love ❤️",
            brandColorHex = 0xFFF43F5E,
            iconResId = R.drawable.ic_notification_love,
            defaultTitle = "I Love You",
            defaultMessage = "অনেক মিস করছি তোমাকে, তুমি কোথায় আছো এখন? ❤️"
        ),
        AppPreset(
            id = "police",
            nameEn = "Police / Bureau",
            nameBn = "পুলিশ ডিপার্টমেন্ট",
            defaultSender = "Police Special Task",
            brandColorHex = 0xFFEF4444,
            iconResId = R.drawable.ic_notification_police,
            defaultTitle = "EMERGENCY NOTICE",
            defaultMessage = "আপনার মোবাইল লোকেশন ট্র্যাকিং শুরু হয়েছে। প্রস্তুত থাকুন! 🚨"
        ),
        AppPreset(
            id = "boss",
            nameEn = "Boss / Office",
            nameBn = "অফিস বস",
            defaultSender = "Office Boss",
            brandColorHex = 0xFF334155,
            iconResId = R.drawable.ic_notification_prank,
            defaultTitle = "Urgent Notice",
            defaultMessage = "কালকে থেকে আর অফিসে আসতে হবে না! জরুরি দেখা করো।"
        ),
        AppPreset(
            id = "lottery",
            nameEn = "Lottery Prize",
            nameBn = "লটারি বিজয়ী",
            defaultSender = "Lucky Mega Draw",
            brandColorHex = 0xFF8B5CF6,
            iconResId = R.drawable.ic_notification_bank,
            defaultTitle = "Winner Notification",
            defaultMessage = "Congratulations! You just won the first prize of $10,000! 🎉"
        ),
        AppPreset(
            id = "custom",
            nameEn = "Custom / নিজস্ব",
            nameBn = "কাস্টম মেসেজ",
            defaultSender = "Secret Friend",
            brandColorHex = 0xFF4F46E5,
            iconResId = R.drawable.ic_notification_prank,
            defaultTitle = "Surprise",
            defaultMessage = "তোমার জন্য একটা বড় চমক অপেক্ষা করছে!"
        )
    )

    data class PrankTemplate(
        val category: String,
        val appPresetId: String,
        val sender: String,
        val title: String,
        val message: String,
        val presetImageResId: Int? = null
    )

    val templates = listOf(
        PrankTemplate(
            category = "টাকা ও ব্যাংক / Money",
            appPresetId = "bank",
            sender = "Bank A/C Transfer",
            title = "টাকা জমা হয়েছে 💰",
            message = "অভিনন্দন! আপনার ব্যাংক একাউন্টে ১,০০,০০০ টাকা জমা করা হয়েছে।",
            presetImageResId = R.drawable.img_prank_money
        ),
        PrankTemplate(
            category = "প্রেম ও ভালোবাসা / Love",
            appPresetId = "love",
            sender = "Crush ❤️",
            title = "মনের কথা...",
            message = "আমি তোমাকে অনেক দিন ধরে ভালোবাসি! তুমি কি আমার হবে? 🌹",
            presetImageResId = R.drawable.img_prank_rose
        ),
        PrankTemplate(
            category = "পুলিশ ও বিপদ / Alert",
            appPresetId = "police",
            sender = "CID Cyber Crime",
            title = "সতর্কবার্তা 🚨",
            message = "সাইবার ক্রাইম বিভাগ: আপনার ডিভাইসটি নজরদারিতে আছে। অবিলম্বে অবস্থান জানান!",
            presetImageResId = R.drawable.img_prank_police
        ),
        PrankTemplate(
            category = "হোয়াটসঅ্যাপ আড্ডা / WhatsApp",
            appPresetId = "whatsapp",
            sender = "Rifat (Best Friend)",
            title = "জরুরি মেসেজ",
            message = "দোস্ত এখনই তোর বাড়ির সামনে আয়, একটা অবিশ্বাস্য ঘটনা ঘটে গেছে!",
            presetImageResId = null
        ),
        PrankTemplate(
            category = "অফিস ও বস / Job",
            appPresetId = "boss",
            sender = "Managing Director",
            title = "Promotion Letter 👔",
            message = "অভিনন্দন! আপনার বেতন দ্বিগুণ করে সিনিয়র ম্যানেজার পদে প্রমোশন দেওয়া হলো।",
            presetImageResId = null
        ),
        PrankTemplate(
            category = "মা ও পরিবার / Family",
            appPresetId = "custom",
            sender = "আম্মু ❤️",
            title = "বাসায় আসো!",
            message = "তোর জন্য বিরিয়ানি রান্না করেছি, আড্ডা মারা বাদ দিয়ে জলদি বাড়ি আয়!",
            presetImageResId = null
        )
    )
}
