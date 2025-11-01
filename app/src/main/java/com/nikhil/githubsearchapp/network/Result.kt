package com.nikhil.githubsearchapp.network

import android.util.Log
import com.nikhil.githubsearchapp.data.GHRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class NetworkService @Inject constructor() {

    private val TAG = "NetworkService"

    private val api: GitHubApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }

    suspend fun fetchRepos(username: String): Result<List<GHRepo>> {
        Log.d(TAG, "Fetching repos for user: $username")

        return try {
            val response = api.getUserRepos(username)
            Log.d(TAG, "Successfully fetched ${response.size} repos for $username")
            Result.Success(response)
        } catch (e: IOException) {
            Log.e(TAG, "Network error while fetching repos: ${e.message}", e)
            Result.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}", e)
            Result.Error(e)
        }
    }
}
