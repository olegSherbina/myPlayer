package com.example.myplayer.core.di.module

import com.example.myplayer.model.MyPlayerDatabase
import com.example.myplayer.repository.PlaylistRepository
import com.example.myplayer.repository.repositoryimpl.MyPlayerPlaylistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun getPlaylistRepository(database: MyPlayerDatabase):
            PlaylistRepository = MyPlayerPlaylistRepository(database)

}
