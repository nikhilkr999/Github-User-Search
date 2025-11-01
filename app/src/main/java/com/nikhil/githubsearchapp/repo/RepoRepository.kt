package com.nikhil.githubsearchapp.repo

import android.util.Log
import com.nikhil.githubsearchapp.data.GHRepo
import com.nikhil.githubsearchapp.db.Cacher
import com.nikhil.githubsearchapp.network.NetworkService
import com.nikhil.githubsearchapp.network.Result
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class RepoRepository @Inject constructor(
    private val networkService: NetworkService,
    private val cacher: Cacher
) {
    private val TAG = "RepoRepository"

    suspend fun getRepos(username: String): Result<List<GHRepo>> {
        Log.d(TAG, "Fetching repos for user: $username")

        // Try cache first
        val cached = cacher.getCachedRepos(username)
        if (cached.isNotEmpty()) {
            Log.d(TAG, "Cache hit for user: $username, repos found: ${cached.size}")
            return Result.Success(cached)
        } else {
            Log.d(TAG, "Cache miss for user: $username")
        }

        // Fetch from network
        val result = networkService.fetchRepos(username)
        when (result) {
            is Result.Success -> {
                Log.d(TAG, "Network success: fetched ${result.data.size} repos for user: $username")
                cacher.cacheRepos(username, result.data, username)
                Log.d(TAG, "Repos cached for user: $username")
            }
            is Result.Error -> {
                Log.e(TAG, "Network error while fetching repos for user: $username, error: ${result.exception.message}")
            }
        }

        return result
    }
}
