package com.example.myplayer.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import java.lang.reflect.Type

@Entity
@TypeConverters(DataConverter::class)
data class Playlist(
    @PrimaryKey(autoGenerate = true) val playlistId: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "playlist") val playlist: List<Pair<String, String>>
) {
    constructor(name: String, playlist: List<Pair<String, String>>) : this(0, name, playlist)
}

@Database(entities = arrayOf(Playlist::class), version = 1)
abstract class MyPlayerDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): List<Playlist>

    @Query("SELECT * FROM playlist WHERE name LIKE :name LIMIT 1")
    fun loadPlayListLinks(name: String): Single<Playlist>

    @Insert
    fun savePlaylistLinks(vararg playlist: Playlist): Completable

    @Delete
    fun delete(Playlist: Playlist)
}

class DataConverter : java.io.Serializable {
    @TypeConverter
    fun fromPlaylist(playlist: List<Pair<String, String>>): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return gson.toJson(playlist, type)
    }

    @TypeConverter // note this annotation
    fun toPlaylist(playlistString: String?): List<Pair<String, String>> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Pair<String, String>>?>() {}.type
        return gson.fromJson(playlistString, type)
    }
}
