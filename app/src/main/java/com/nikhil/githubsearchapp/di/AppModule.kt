package com.nikhil.githubsearchapp.di

import android.content.Context
import com.nikhil.githubsearchapp.db.Cacher
import com.nikhil.githubsearchapp.network.NetworkService
import com.nikhil.githubsearchapp.repo.RepoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService = NetworkService()

    @Provides
    @Singleton
    fun provideCacher(@ApplicationContext context: Context): Cacher = Cacher(context)

    @Provides
    fun provideRepoRepository(networkService: NetworkService, cacher: Cacher): RepoRepository =
        RepoRepository(networkService, cacher)
}