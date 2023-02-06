package com.example.gitsimpleproject.api

import com.example.gitsimpleproject.api.model.GithubRepo
import com.example.gitsimpleproject.api.model.RepoSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubApi {
    @GET("search/repositories")
    fun searchRepository(@Query("q") query: String?): Single<RepoSearchResponse>

    @GET("repos/{owner}/{name}")
    fun getRepository(
        @Path("owner") ownerLogin: String?,
        @Path("name") repoName: String?
    ): Single<GithubRepo>
}