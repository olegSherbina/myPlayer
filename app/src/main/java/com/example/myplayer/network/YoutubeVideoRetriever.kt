package com.example.myplayer.network

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*


/**
 * Represents youtube video information retriever.
 */
class YouTubeVideoInfoRetriever {
    //TODO dunno wtf is this, delete later if not needed
    private val kvpList =
        TreeMap<String, String>()

    @Throws(IOException::class)
    fun retrieve(targetUrl: String) {
        val client = SimpleHttpClient()
        val output = client.execute(
            targetUrl,
            SimpleHttpClient.HTTP_GET,
            SimpleHttpClient.DEFAULT_TIMEOUT
        )
        if (output != null) {
            parse(output)
        }
    }

    fun getInfo(key: String?): String? {
        return kvpList[key]
    }

    fun printAll() {
        println("TOTAL VARIABLES=" + kvpList.size)
        for ((key, value) in kvpList) {
            print("$key=")
            println("" + value + "")
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun parse(data: String) {
        val splits = data.split("&".toRegex()).toTypedArray()
        var kvpStr = ""
        if (splits.size < 1) {
            return
        }
        kvpList.clear()
        for (i in splits.indices) {
            kvpStr = splits[i]
            try {
                // Data is encoded multiple times
                kvpStr =
                    URLDecoder.decode(kvpStr, SimpleHttpClient.ENCODING_UTF_8)
                kvpStr =
                    URLDecoder.decode(kvpStr, SimpleHttpClient.ENCODING_UTF_8)
                val kvpSplits =
                    kvpStr.split("=".toRegex(), 2).toTypedArray()
                if (kvpSplits.size == 2) {
                    kvpList[kvpSplits[0]] = kvpSplits[1]
                } else if (kvpSplits.size == 1) {
                    kvpList[kvpSplits[0]] = ""
                }
            } catch (ex: UnsupportedEncodingException) {
                throw ex
            }
        }
    }

    class SimpleHttpClient {
        @Throws(IOException::class)
        fun execute(
            urlStr: String,
            httpMethod: String,
            timeout: Int
        ): String? {
            var url: URL? = null
            var conn: HttpURLConnection? = null
            var inStream: InputStream? = null
            val outStream: OutputStream? = null
            var response: String? = null
            try {
                url = URL(urlStr)
                conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = timeout
                conn.requestMethod = httpMethod
                inStream = BufferedInputStream(conn.inputStream)
                response = getInput(inStream)
            } finally {
                if (conn != null && conn.errorStream != null) {
                    var errorResponse = " : "
                    errorResponse = errorResponse + getInput(conn.errorStream)
                    response = response + errorResponse
                }
                conn?.disconnect()
            }
            return response
        }

        @Throws(IOException::class)
        private fun getInput(`in`: InputStream): String {
            val sb = StringBuilder(8192)
            val b = ByteArray(1024)
            var bytesRead = 0
            while (true) {
                bytesRead = `in`.read(b)
                if (bytesRead < 0) {
                    break
                }
                val s =
                    String(b, 0, bytesRead, Charset.forName(ENCODING_UTF_8))
                sb.append(s)
            }
            return sb.toString()
        }

        companion object {
            const val ENCODING_UTF_8 = "UTF-8"
            const val DEFAULT_TIMEOUT = 10000
            const val HTTP_GET = "GET"
        }
    }

    companion object {
        private const val URL_YOUTUBE_GET_VIDEO_INFO =
            "http://www.youtube.com/get_video_info?&video_id="
        const val KEY_DASH_VIDEO = "dashmpd"
        const val KEY_HLS_VIDEO = "hlsvp"
    }
}