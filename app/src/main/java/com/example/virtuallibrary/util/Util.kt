package com.example.virtuallibrary.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

object Util {
    fun downloadPdf(context: Context, downloadUrl: String?) {
        downloadUrl?.let {
            val request = DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle("Downloading PDF")
                .setDescription("Download in progress")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "book.pdf")

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }
    }
}