package com.csiw.directsharekit.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.Locale

/**
 * Repository for file operations used by sharing flows.
 */
class FileRepository(private val context: Context) {

    fun getFile(filePath: String): File? {
        val file = File(filePath)
        return if (file.exists() && file.isFile) file else null
    }

    fun getFileUri(file: File): Uri {
        return FileProviderHelper.getUri(context, file)
    }

    fun getFileUri(filePath: String): Uri? {
        val file = getFile(filePath) ?: return null
        return getFileUri(file)
    }

    fun fileExists(filePath: String): Boolean {
        return getFile(filePath) != null
    }

    fun getFileSize(filePath: String): Long {
        return getFile(filePath)?.length() ?: -1L
    }

    fun getFileSizeFormatted(filePath: String): String {
        val size = getFileSize(filePath)
        return if (size <= 0) "Unknown" else formatBytes(size)
    }

    private fun formatBytes(bytes: Long): String {
        return try {
            when {
                bytes <= 0 -> "0 B"
                bytes < 1024 -> "$bytes B"
                bytes < 1024 * 1024 -> String.format(Locale.getDefault(), "%.2f KB", bytes / 1024.0)
                bytes < 1024 * 1024 * 1024 -> String.format(Locale.getDefault(), "%.2f MB", bytes / (1024.0 * 1024))
                else -> String.format(Locale.getDefault(), "%.2f GB", bytes / (1024.0 * 1024 * 1024))
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}

