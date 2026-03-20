package com.csiw.directsharekit.domain

import android.net.Uri
import com.csiw.directsharekit.data.FileRepository
import java.io.File

/**
 * Use case layer for preparing files and metadata for sharing.
 */
class ShareUseCase(private val repository: FileRepository) {

    fun prepareFile(filePath: String): Pair<File, Uri>? {
        val file = repository.getFile(filePath) ?: return null
        val uri = repository.getFileUri(file)
        return Pair(file, uri)
    }

    fun prepareMultiple(files: List<String>): ArrayList<Uri> {
        val uris = ArrayList<Uri>()
        files.forEach { filePath ->
            repository.getFile(filePath)?.let { file ->
                uris.add(repository.getFileUri(file))
            }
        }
        return uris
    }

    fun validateFiles(files: List<String>): Boolean {
        return files.isNotEmpty() && files.all { repository.fileExists(it) }
    }

    fun getFileInfo(filePath: String): FileInfo? {
        val file = repository.getFile(filePath) ?: return null
        return FileInfo(
            name = file.name,
            size = file.length(),
            sizeFormatted = repository.getFileSizeFormatted(filePath),
            path = file.absolutePath,
            mimeType = getMimeType(filePath)
        )
    }

    private fun getMimeType(filePath: String): String {
        val extension = filePath.substringAfterLast(".", "")
        return when (extension.lowercase()) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "txt" -> "text/plain"
            "doc", "docx" -> "application/msword"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "ppt", "pptx" -> "application/vnd.ms-powerpoint"
            "zip" -> "application/zip"
            "mp3" -> "audio/mpeg"
            "mp4" -> "video/mp4"
            else -> "*/*"
        }
    }

    data class FileInfo(
        val name: String,
        val size: Long,
        val sizeFormatted: String,
        val path: String,
        val mimeType: String
    )
}

