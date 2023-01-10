package com.example.gitsimpleproject.api

import androidx.annotation.NonNull
import com.example.gitsimpleproject.api.model.GithubAccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface AuthApi {
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
        @NonNull @Field("client_id") clientId: String?,
        @NonNull @Field("client_secret") clientSecret: String?,
        @NonNull @Field("code") code: String?
    ): Call<GithubAccessToken>?
}