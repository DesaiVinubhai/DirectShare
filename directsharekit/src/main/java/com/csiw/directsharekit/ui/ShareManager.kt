package com.csiw.directsharekit.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import java.net.URLEncoder

/**
 * Manager for all sharing operations.
 */
object ShareManager {

    private const val WHATSAPP_PACKAGE = "com.whatsapp"
    private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
    private const val TELEGRAM_PACKAGE = "org.telegram.messenger"
    private const val MESSENGER_PACKAGE = "com.facebook.orca"
    private const val GMAIL_PACKAGE = "com.google.android.gm"
    private const val DRIVE_PACKAGE = "com.google.android.apps.docs"

    // ==================== WhatsApp Sharing ====================
    fun shareToWhatsApp(
        context: Context,
        uri: Uri,
        mimeType: String,
        phoneNumber: String,
        message: String? = null
    ) {
        // Step 1: Resolve WhatsApp package (normal or business)
        val packageName = resolveWhatsAppPackage(context)
            ?: run {
                // WhatsApp not installed
                openWhatsAppWeb(context, phoneNumber, message)
                return
            }

        // Step 2: Clean and validate phone number
        val cleanNumber = phoneNumber.filter { it.isDigit() }
        if (cleanNumber.isEmpty()) return

        // Step 3: Build WhatsApp JID (internal format)
        val jid = "$cleanNumber@s.whatsapp.net"

        // Step 4: Create SEND intent (for file sharing)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType

            // Attach file
            putExtra(Intent.EXTRA_STREAM, uri)

            // Attach message (optional)
            message?.let { putExtra(Intent.EXTRA_TEXT, it) }

            // Force WhatsApp
            setPackage(packageName)

            // Required for file sharing
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Try to force direct chat (may fail on some devices)
            putExtra("jid", jid)
        }

        try {
            // Step 5: Grant URI permission explicitly
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // Step 6: Try direct share
            context.startActivity(sendIntent)

        } catch (e: Exception) {
            e.printStackTrace()

            // Step 7: Fallback → Open chat via wa.me (100% reliable)
            openWhatsAppWeb(context, cleanNumber, message)
        }
    }

    private fun openWhatsAppWeb(
        context: Context,
        phoneNumber: String,
        message: String?
    ) {
        try {
            val url = "https://wa.me/$phoneNumber?text=${
                URLEncoder.encode(message ?: "", "UTF-8")
            }"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = url.toUri()
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareMultipleToWhatsApp(
        context: Context,
        uris: ArrayList<Uri>,
        phoneNumber: String? = null,
        message: String? = null
    ) {
        val targetPackage = resolveWhatsAppPackage(context) ?: return
        uris.forEach { uri ->
            context.grantUriPermission(targetPackage, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            setPackage(targetPackage)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            buildJid(phoneNumber)?.let { putExtra("jid", it) }
            message?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    // ==================== Telegram Sharing ====================

    fun shareToTelegram(
        context: Context,
        uri: Uri,
        mimeType: String,
        message: String? = null
    ) {
        if (!isAppInstalled(context, TELEGRAM_PACKAGE)) return
        context.grantUriPermission(TELEGRAM_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage(TELEGRAM_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            message?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    fun shareMultipleToTelegram(
        context: Context,
        uris: ArrayList<Uri>,
        message: String? = null
    ) {
        if (!isAppInstalled(context, TELEGRAM_PACKAGE)) return
        uris.forEach { uri ->
            context.grantUriPermission(TELEGRAM_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            setPackage(TELEGRAM_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            message?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    // ==================== Messenger Sharing ====================

    fun shareToMessenger(
        context: Context,
        uri: Uri,
        mimeType: String
    ) {
        if (!isAppInstalled(context, MESSENGER_PACKAGE)) return
        context.grantUriPermission(MESSENGER_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage(MESSENGER_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    fun shareMultipleToMessenger(
        context: Context,
        uris: ArrayList<Uri>
    ) {
        if (!isAppInstalled(context, MESSENGER_PACKAGE)) return
        uris.forEach { uri ->
            context.grantUriPermission(
                MESSENGER_PACKAGE,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            setPackage(MESSENGER_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    // ==================== Gmail Sharing ====================

    fun shareViaGmail(
        context: Context,
        uri: Uri,
        recipient: String,
        subject: String = "",
        body: String = ""
    ) {
        if (!isAppInstalled(context, GMAIL_PACKAGE)) return
        context.grantUriPermission(GMAIL_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(GMAIL_PACKAGE)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    fun shareMultipleViaGmail(
        context: Context,
        uris: ArrayList<Uri>,
        recipient: String,
        subject: String = "",
        body: String = ""
    ) {
        if (!isAppInstalled(context, GMAIL_PACKAGE)) return
        uris.forEach { uri ->
            context.grantUriPermission(GMAIL_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setPackage(GMAIL_PACKAGE)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    // ==================== Google Drive Sharing ====================

    fun shareViaDrive(
        context: Context,
        uri: Uri
    ) {
        if (!isAppInstalled(context, DRIVE_PACKAGE)) return
        context.grantUriPermission(DRIVE_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage(DRIVE_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    fun shareMultipleViaDrive(
        context: Context,
        uris: ArrayList<Uri>
    ) {
        if (!isAppInstalled(context, DRIVE_PACKAGE)) return
        uris.forEach { uri ->
            context.grantUriPermission(DRIVE_PACKAGE, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            setPackage(DRIVE_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    // ==================== Generic Sharing ====================

    fun shareGeneric(
        context: Context,
        uri: Uri,
        mimeType: String,
        title: String = "Share via"
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(Intent.createChooser(intent, title))
        } catch (_: Exception) {
        }
    }

    fun shareMultiple(
        context: Context,
        uris: ArrayList<Uri>,
        title: String = "Share files"
    ) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(Intent.createChooser(intent, title))
        } catch (_: Exception) {
        }
    }

    // ==================== App Detection ====================

    fun isWhatsAppInstalled(context: Context): Boolean {
        return resolveWhatsAppPackage(context) != null
    }

    fun isTelegramInstalled(context: Context): Boolean {
        return isAppInstalled(context, TELEGRAM_PACKAGE)
    }

    fun isMessengerInstalled(context: Context): Boolean {
        return isAppInstalled(context, MESSENGER_PACKAGE)
    }

    fun isGmailInstalled(context: Context): Boolean {
        return isAppInstalled(context, GMAIL_PACKAGE)
    }

    fun isDriveInstalled(context: Context): Boolean {
        return isAppInstalled(context, DRIVE_PACKAGE)
    }

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun resolveWhatsAppPackage(context: Context): String? {
        return when {
            isAppInstalled(context, WHATSAPP_PACKAGE) -> WHATSAPP_PACKAGE
            isAppInstalled(context, WHATSAPP_BUSINESS_PACKAGE) -> WHATSAPP_BUSINESS_PACKAGE
            else -> null
        }
    }

    private fun buildJid(phoneNumber: String?): String? {
        if (phoneNumber.isNullOrBlank()) return null
        val normalized = phoneNumber.filter { it.isDigit() }
        if (normalized.length < 8) return null
        return "$normalized@s.whatsapp.net"
    }
}




