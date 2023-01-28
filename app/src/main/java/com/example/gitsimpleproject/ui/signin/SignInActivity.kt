package com.example.gitsimpleproject.ui.signin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.gitsimpleproject.BuildConfig
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.AuthApi
import com.example.gitsimpleproject.api.model.GithubAccessToken
import com.example.gitsimpleproject.data.AuthTokenProvider
import com.example.gitsimpleproject.databinding.ActivitySigninBinding
import com.example.gitsimpleproject.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.gitsimpleproject.api.provideAuthApi

class SignInActivity : AppCompatActivity(), View.OnClickListener {


    private val binding: ActivitySigninBinding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
    }
    internal val api: AuthApi by lazy { provideAuthApi() }
    internal val authTokenProvider: AuthTokenProvider by lazy { AuthTokenProvider(this) }
    internal var accessTokenCall : Call<GithubAccessToken>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnStart.setOnClickListener(this)

    }

    override fun onStop() {
        super.onStop()
        // 액티비티가 화면에서 사라지는 시점에 API 호출 객체가 생성되어 있다면
        // API 요청을 취소한다.
        accessTokenCall?.run {
            cancel()
        }
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnSubmit -> {
                // 사용자 인증을 처리하는 URL 구성
                // 형식 : https://github.com/login/oauth/authorize?client_id={애플리케이션 Client ID}
                val authUri = Uri.Builder().scheme("https")
                    .authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                    .build()

                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(this, authUri)

                // 저장된 엑세스 토큰이 있다면 메인 액티비티로 이동합니다.
                if (null != authTokenProvider.getToken()) {
                    launchMainActivity()
                }

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        showProgress()
        val uri = intent.data ?: throw IllegalArgumentException("No data exists")
        val code = uri.getQueryParameter("code") ?: throw IllegalStateException("No code exists")
        getAccessToken(code)
    }

    private fun getAccessToken(code: String) {
        showProgress()
        accessTokenCall = api.getAccessToken(
            BuildConfig.GITHUB_CLIENT_ID,
            BuildConfig.GITHUB_CLIENT_SECRET, code
        )

        // 앞에서 API 호출에 필요한 객체를 받았으므로,
        // 이 시점에서 accessTokenCall 객체의 값은 널이 아닙니다.
        // 따라서 비 널 값 보증(!!)을 사용하여 이 객체를 사용합니다.
        accessTokenCall!!.enqueue(object : Callback<GithubAccessToken?> {
            override fun onResponse(
                call: Call<GithubAccessToken?>,
                response: Response<GithubAccessToken?>
            ) {
                hideProgress()
                val token = response.body()
                if (response.isSuccessful && null != token) {
                    authTokenProvider.updateToken(token.accessToken)
                    launchMainActivity()
                } else {
                    showError(
                        IllegalStateException(
                            "Not successful: " + response.message()
                        )
                    )
                }
            }

            override fun onFailure(call: Call<GithubAccessToken?>, t: Throwable) {
                hideProgress()
                showError(t)
            }
        })
    }

    private fun showProgress() {
        binding.btnStart.visibility = View.GONE
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.btnStart.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.GONE
    }

    private fun showError(throwable: Throwable) {
        Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
    }

    private fun launchMainActivity() {
        startActivity(
            Intent(
                this@SignInActivity, MainActivity::class.java
            )
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}