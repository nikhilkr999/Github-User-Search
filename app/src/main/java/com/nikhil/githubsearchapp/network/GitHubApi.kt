package com.nikhil.githubsearchapp.network

import com.nikhil.githubsearchapp.data.GHRepo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("sort") sort: String = "stargazers_count",
        @Query("direction") direction: String = "desc"
    ): List<GHRepo>
}