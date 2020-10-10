package com.example.myplayer.network

import android.content.Context
import com.example.myplayer.R
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File


class CacheDataSourceFactory(
    private val context: Context
) :
    DataSource.Factory {

    private val defaultDataSourceFactory: DefaultDataSourceFactory
    override fun createDataSource(): DataSource {
        return CacheDataSource(
            getSimpleCacheInstance(),
            defaultDataSourceFactory.createDataSource(),
            FileDataSource(),
            CacheDataSink(getSimpleCacheInstance(), CacheDataSink.DEFAULT_FRAGMENT_SIZE),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
            null
        )
    }

    init {
        val userAgent: String =
            Util.getUserAgent(context, context.getString(R.string.app_name))
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDataSourceFactory = DefaultDataSourceFactory(
            this.context,
            bandwidthMeter,
            OkHttpDataSourceFactory(
                Retrofit.Builder()
                    .baseUrl("http://baseurl.com")
                    .client(
                        OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor()
                                .apply {
                                    level = HttpLoggingInterceptor.Level.BASIC

                                })
                            .build()
                    )
                    .build().callFactory()
            )
        )
    }

    private val simpleCacheInstance =
        SimpleCache(
            File(context.cacheDir, "media"),
            LeastRecentlyUsedCacheEvictor(MAX_VIDEO_CACHE_SIZE_IN_BYTES),
            ExoDatabaseProvider(context)
        )

    fun getSimpleCacheInstance(): SimpleCache {
        return simpleCacheInstance
    }

    companion object {
        const val MAX_VIDEO_CACHE_SIZE_IN_BYTES: Long = 100 * 1024 * 1024
    }
}