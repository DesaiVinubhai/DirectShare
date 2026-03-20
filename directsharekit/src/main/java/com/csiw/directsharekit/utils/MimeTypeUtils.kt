package com.csiw.directsharekit.utils

import android.webkit.MimeTypeMap

/**
 * Utility object for MIME type detection.
 */
object MimeTypeUtils {

    fun getMimeType(path: String): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        return if (extension.isNotEmpty()) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase()) ?: "*/*"
        } else {
            "*/*"
        }
    }

    fun getMimeTypeFromExtension(extension: String): String {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase()) ?: "*/*"
    }

    fun isImage(mimeType: String): Boolean = mimeType.startsWith("image/")

    fun isVideo(mimeType: String): Boolean = mimeType.startsWith("video/")

    fun isAudio(mimeType: String): Boolean = mimeType.startsWith("audio/")

    fun isDocument(mimeType: String): Boolean {
        return mimeType.contains("pdf") ||
            mimeType.contains("word") ||
            mimeType.contains("document") ||
            mimeType.contains("spreadsheet") ||
            mimeType.contains("presentation") ||
            mimeType == "text/plain"
    }

    fun getFileCategory(mimeType: String): String {
        return when {
            isImage(mimeType) -> "Image"
            isVideo(mimeType) -> "Video"
            isAudio(mimeType) -> "Audio"
            isDocument(mimeType) -> "Document"
            else -> "File"
        }
    }
}

