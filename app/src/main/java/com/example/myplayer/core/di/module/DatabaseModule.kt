package com.example.myplayer.core.di.module

import android.content.Context
import androidx.room.Room
import com.example.myplayer.model.MyPlayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MyPlayerDatabase::class.java,
        "myPlayer database"
    ).build()

    @Singleton
    @Provides
    fun provideDao(db: MyPlayerDatabase) =
        db.playlistDao()
}