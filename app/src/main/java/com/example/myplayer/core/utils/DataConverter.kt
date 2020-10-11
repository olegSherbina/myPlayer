package com.example.myplayer.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter : java.io.Serializable {
    @TypeConverter
    fun fromPlaylist(playlist: List<Pair<String, String>>): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return gson.toJson(playlist, type)
    }

    @TypeConverter
    fun toPlaylist(playlistString: String?): List<Pair<String, String>> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Pair<String, String>>?>() {}.type
        return gson.fromJson(playlistString, type)
    }
}