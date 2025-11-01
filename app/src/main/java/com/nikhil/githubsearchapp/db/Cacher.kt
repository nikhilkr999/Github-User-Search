package com.nikhil.githubsearchapp.db

import android.content.Context
import androidx.room.Room
import com.nikhil.githubsearchapp.data.GHRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cacher @Inject constructor(@ApplicationContext private val context: Context) {
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "github-db")
            .build()
    }

    suspend fun getCachedRepos(username: String): List<GHRepo> {
        return db.repoDao().getReposForUser(username).map { it.toGHRepo() }
    }

    suspend fun cacheRepos(username: String, repos: List<GHRepo>, ownerLogin: String) {
        val entities = repos.map { it.toEntity(username, ownerLogin) }
        db.repoDao().deleteForUser(username)  // Clear old cache
        db.repoDao().insertAll(entities)
    }
}