package com.csiw.directsharekit.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Helper for safely creating sharable content URIs.
 */
object FileProviderHelper {

    private const val PROVIDER_SUFFIX = ".directshare.provider"

    fun getUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}$PROVIDER_SUFFIX",
            file
        )
    }

    fun getUriFromPath(context: Context, filePath: String): Uri? {
        val file = File(filePath)
        return if (file.exists()) {
            getUri(context, file)
        } else {
            null
        }
    }
}

