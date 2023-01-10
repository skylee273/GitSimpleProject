package com.example.gitsimpleproject.api.model

import com.google.gson.annotations.SerializedName

class GithubAccessToken {
    @SerializedName("access_token")
    var accessToken: String? = null
    var scope: String? = null
    @SerializedName("token_type")
    var tokenType: String? = null
}