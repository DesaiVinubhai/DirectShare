package com.csiw.directsharekit.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

/**
 * Utility object for app-related operations.
 */
object AppUtils {

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun isWhatsAppInstalled(context: Context): Boolean {
        return isAppInstalled(context, "com.whatsapp") || isAppInstalled(context, "com.whatsapp.w4b")
    }

    fun isGmailInstalled(context: Context): Boolean {
        return isAppInstalled(context, "com.google.android.gm")
    }

    fun isGoogleDriveInstalled(context: Context): Boolean {
        return isAppInstalled(context, "com.google.android.apps.docs")
    }

    fun openPlayStore(context: Context, packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = "https://play.google.com/store/apps/details?id=$packageName".toUri()
            }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }
}

