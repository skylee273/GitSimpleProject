package com.example.gitsimpleproject.api.model

import com.google.gson.annotations.SerializedName

class GithubRepo {
    val name: String? = null

    @SerializedName("full_name")
    val fullName: String? = null

    var owner: GithubOwner? = null

    val description: String? = null

    val language: String? = null

    @SerializedName("updated_at")
    val updatedAt: String? = null

    @SerializedName("stargazers_count")
    val stars = 0

}