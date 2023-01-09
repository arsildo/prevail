package com.arsildo.prevail.di

import android.app.Application
import com.google.android.exoplayer2.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VideoPlayerModule {
    @Provides
    @ViewModelScoped
    fun provideExoPlayer(application: Application): ExoPlayer {
        return ExoPlayer.Builder(application).build()
    }
}