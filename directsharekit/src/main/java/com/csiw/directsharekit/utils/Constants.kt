package com.csiw.directsharekit.utils

/**
 * Constants used throughout the sharing library.
 */
object Constants {

    object Apps {
        const val WHATSAPP = "com.whatsapp"
        const val WHATSAPP_BUSINESS = "com.whatsapp.w4b"
        const val GMAIL = "com.google.android.gm"
        const val GOOGLE_DRIVE = "com.google.android.apps.docs"
        const val TELEGRAM = "org.telegram.messenger"
        const val MESSENGER = "com.facebook.orca"
        const val GOOGLE_PHOTOS = "com.google.android.apps.photos"
        const val DROPBOX = "com.dropbox.android"
    }

    object MimeTypes {
        const val TEXT = "text/plain"
        const val PDF = "application/pdf"
        const val IMAGE = "image/*"
        const val VIDEO = "video/*"
        const val AUDIO = "audio/*"
        const val ANY = "*/*"
        const val EMAIL = "message/rfc822"
    }

    object FileProviderConfig {
        const val AUTHORITY_SUFFIX = ".directshare.provider"
    }
}

