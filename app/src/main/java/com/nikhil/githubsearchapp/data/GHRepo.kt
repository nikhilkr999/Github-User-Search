package com.nikhil.githubsearchapp.data

import com.google.gson.annotations.SerializedName


data class GHRepo(
    val id: Long,
    val name: String,

    @SerializedName("html_url")
    val repoURL: String
)
