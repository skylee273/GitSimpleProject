package com.example.gitsimpleproject.api.model

import com.google.gson.annotations.SerializedName

class GithubRepo(
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    var owner: GithubOwner,

    val description: String? = null,

    val language: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("stargazers_count")
    val stars: Int

)