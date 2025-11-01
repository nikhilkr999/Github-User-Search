package com.nikhil.githubsearchapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepoDao {
    @Query("SELECT * FROM repos WHERE username = :username")
    suspend fun getReposForUser(username: String): List<RepoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query("DELETE FROM repos WHERE username = :username")
    suspend fun deleteForUser(username: String)
}