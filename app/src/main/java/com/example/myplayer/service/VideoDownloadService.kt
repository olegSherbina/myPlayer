package com.example.myplayer.service

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.myplayer.R
import com.example.myplayer.core.base.MyApplication.Companion.CHANNEL_NOTIFICATION_ID
import com.example.myplayer.ui.mainactivity.activity.MainActivity.Companion.POSITION
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2.Fetch.Impl.getInstance
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File


class VideoDownloadService : IntentService(VideoDownloadService::class.java.simpleName) {

    override fun onHandleIntent(p0: Intent?) {
        //NOP
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(DOWNLOAD_URL) ?: ""
        val fileName =
            intent?.getIntExtra(POSITION, -1).toString() //fileName is a playlist position for now

        downloadFile(fileName, url)
        return START_NOT_STICKY
    }

    private fun downloadFile(fileName: String, url: String) {
        val dir = File(Environment.getExternalStorageDirectory(), "/MyPlayerVideos/downloads")
        dir.mkdirs()
        val file = File(dir, fileName + ".mp4")

        if (!file.exists()) {
            file.createNewFile()
            //TODO one client instance
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            level = HttpLoggingInterceptor.Level.BASIC

                        }).build()

            val request = Request(url, file.path)
            request.priority = Priority.HIGH
            request.networkType = NetworkType.ALL
            val fetchConfiguration =
                FetchConfiguration.Builder(this)
                    .setDownloadConcurrentLimit(10)
                    .setHttpDownloader(OkHttpDownloader(okHttpClient))
                    .build()

            val fetch = getInstance(fetchConfiguration)

            val fetchListener: FetchListener = object : FetchListener {
                override fun onQueued(
                    download: Download,
                    waitingOnNetwork: Boolean
                ) {
                    /*if (request.id == download.id) {
                        showDownloadInList(download)
                    }*/
                }

                override fun onRemoved(download: Download) {}

                override fun onResumed(download: Download) {}

                override fun onStarted(
                    download: Download,
                    downloadBlocks: List<DownloadBlock>,
                    totalBlocks: Int
                ) {
                }

                override fun onWaitingNetwork(download: Download) {}
                override fun onAdded(download: Download) {}
                override fun onCancelled(download: Download) {}
                override fun onCompleted(download: Download) {
                    Toast.makeText(
                        this@VideoDownloadService,
                        "Video downloaded: " + file.path,
                        Toast.LENGTH_SHORT
                    ).show()
                    stopForeground(true)
                }

                override fun onDeleted(download: Download) {}
                override fun onError(download: Download, error: Error, throwable: Throwable?) {
                    Toast.makeText(
                        this@VideoDownloadService,
                        "error when downloading file",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, error.throwable?.stackTrace.toString())
                    stopForeground(true)
                }

                override fun onPaused(download: Download) {}
                override fun onDownloadBlockUpdated(
                    download: Download,
                    downloadBlock: DownloadBlock,
                    totalBlocks: Int
                ) {
                }

                override fun onProgress(
                    download: Download,
                    etaInMilliSeconds: Long,
                    downloadedBytesPerSecond: Long
                ) {
                    /*if (request.id == download.id) {
                        updateDownload(download, etaInMilliSeconds)
                    }
                    val progress: Int = download.progress*/
                }
            }

            fetch.addListener(fetchListener)

            fetch.enqueue(request,
                Func { updatedRequest: Request? ->

                },
                Func { error: Error? ->
                    Toast.makeText(
                        this@VideoDownloadService,
                        "error when downloading file",
                        Toast.LENGTH_SHORT
                    ).show()
                    fetch.removeListener(fetchListener)
                    Log.e(TAG, error?.throwable?.stackTrace.toString())
                    stopForeground(true)
                }
            )

            showDownloadNotification()
        }
    }

    private fun showDownloadNotification() {
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_NOTIFICATION_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(getString(R.string.download_notification_title_placeholder))
                .setContentText(getString(R.string.download_notification_description_placeholder))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        startForeground(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build())
    }


    companion object {
        const val DOWNLOAD_URL =
            "com.example.myplayer.service.PlayerNotificationService.FROM_NOTIFICATION"
        const val DOWNLOAD_NOTIFICATION_ID = 2
        const val TAG = "VideoDownloadService"
    }
}