package com.example.gitsimpleproject.api

import androidx.annotation.NonNull
import com.example.gitsimpleproject.api.model.GithubAccessToken
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import java.util.*


interface AuthApi {
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
        @NonNull @Field("client_id") clientId: String?,
        @NonNull @Field("client_secret") clientSecret: String?,
        @NonNull @Field("code") code: String?
    ): Single<GithubAccessToken>
}