package com.example.gitsimpleproject.ui.signin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.AuthApi
import com.example.gitsimpleproject.data.AuthTokenProvider
import com.example.gitsimpleproject.ui.main.MainActivity
import com.example.gitsimpleproject.api.provideAuthApi
import com.example.gitsimpleproject.base.BaseActivity
import com.example.gitsimpleproject.databinding.ActivitySigninBinding
import com.example.gitsimpleproject.extensions.plusAssign
import com.example.gitsimpleproject.rx.AutoClearedDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SignInActivity : BaseActivity<ActivitySigninBinding>(R.layout.activity_signin), View.OnClickListener {

    internal val api: AuthApi by lazy { provideAuthApi() }
    private val disposable = AutoClearedDisposable(this)
    private val authTokenProvider: AuthTokenProvider by lazy { AuthTokenProvider(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnStart.setOnClickListener(this)

        // Lifecycle.addObserver() 함수를 사용하여
        // AutoClearedDisposable 객체를 옵서버로 등록
        lifecycle += disposable
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        showProgress()
        val uri = intent.data ?: throw IllegalArgumentException("No data exists")
        val code = uri.getQueryParameter("code") ?: throw IllegalStateException("No code exists")
        getAccessToken(code)
    }

    private fun getAccessToken(code: String) {

        //REST API를 통해 엑세스 토큰을 요청합니다.
        disposable += api.getAccessToken("3e954a3ab814f9b253f9", "6cc7d4769582dd2b1bb53f56d5611345a5f508b2", code)
            /**
             * 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행됩니다.
             * RxAndroid에서 제공하는 스케즐러인
             * AndroidSchedulers.mainThread()
             */
            .map { it.accessToken!!}
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() } // 구독할 때 수행할 작업을 구현한다.
            .doOnTerminate { hideProgress() } // 스트림이 종료될 떄 수행할 작업을 구현한다.
            .subscribe({ token ->
                // API를 통해 액세스 토큰을 정상적으로 받았을 때 처리할 작업을 구현한다.
                // 작업중 오류가 발생하면 이 블록은 호출하지 않는다.
                authTokenProvider.updateToken(token)
                launchMainActivity()
            }) {
                // 에러 블록
                // 네트워크 오류나 데이터 처리 오류 등
                // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                showError(it)
            }
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

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_start -> {
                // 사용자 인증을 처리하는 URL 구성
                // 형식 : https://github.com/login/oauth/authorize?client_id={애플리케이션 Client ID}
                val authUri = Uri.Builder().scheme("https")
                    .authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", "3e954a3ab814f9b253f9")
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
}