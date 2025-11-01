package com.nikhil.githubsearchapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikhil.githubsearchapp.data.GHRepo

@Entity(tableName = "repos")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val repoURL: String,
    val username: String,  // Cache key
    val ownerLogin: String  // For UI display
) {
    fun toGHRepo(): GHRepo = GHRepo(id, name, repoURL)
}

fun GHRepo.toEntity(username: String, ownerLogin: String): RepoEntity =
    RepoEntity(id, name, repoURL, username, ownerLogin)
