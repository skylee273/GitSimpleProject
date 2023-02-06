package com.example.gitsimpleproject.api

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import com.example.gitsimpleproject.data.AuthTokenProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.IllegalStateException
import java.io.IOException

// 싱글톤 클래스를 제거하고 패키지 단위 함수로 다시 선언

fun provideAuthApi(): AuthApi = Retrofit.Builder()
    .baseUrl("https://github.com/")
    .client(provideOkHttpClient(provideLoggingInterceptor(), null))
    .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(AuthApi::class.java)


fun provideGithubApi(context: Context): GithubApi = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .client(
        provideOkHttpClient(
            provideLoggingInterceptor(),
            provideAuthInterceptor(provideAuthTokenProvider(context))
        )
    )
    .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(GithubApi::class.java)


private fun provideOkHttpClient(
    interceptor: HttpLoggingInterceptor,
    authInterceptor: AuthInterceptor?
): OkHttpClient = OkHttpClient.Builder()
    .run {
        if (null != authInterceptor) {
            addInterceptor(authInterceptor)
        }
        addInterceptor(interceptor)
        build()
    }

private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
    .apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

private fun provideAuthInterceptor(provider: AuthTokenProvider): AuthInterceptor {
    val token = provider.getToken() ?: throw IllegalStateException("authToken cannot be null.")
    return AuthInterceptor(token)
}

private fun provideAuthTokenProvider(context: Context): AuthTokenProvider =
    AuthTokenProvider(context.applicationContext)

internal class AuthInterceptor(private val token: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response =
        with(chain) {
            val newRequest = request().newBuilder().run {
                addHeader("Authorization", "token$token")
                build()
            }
            proceed(newRequest)
        }
}