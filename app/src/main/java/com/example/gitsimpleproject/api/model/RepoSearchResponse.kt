package com.example.gitsimpleproject.api.model

import com.google.gson.annotations.SerializedName

class RepoSearchResponse {
    @SerializedName("total_count")
    val totalCount = 0

    val items: MutableList<GithubRepo>? = null

}