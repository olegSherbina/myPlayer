package com.example.myplayer.core.utils

import java.lang.UnsupportedOperationException
import java.util.regex.Matcher
import java.util.regex.Pattern

class UrlUtils {
    companion object{
        public fun convertToThumbnailURL(videoURL: String): String{
            val pattern: Pattern = Pattern.compile("^[^=]*=(.*)$")
            val matcher: Matcher = pattern.matcher(videoURL)
            if (matcher.matches()) {
                return matcher.group(1)
            } else{
                throw UnsupportedOperationException("this URL is not convertible: $videoURL")
            }
        }
    }
}