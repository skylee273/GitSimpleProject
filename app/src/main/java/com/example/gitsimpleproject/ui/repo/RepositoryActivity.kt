package com.example.gitsimpleproject.ui.repo

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.provideGithubApi
import com.example.gitsimpleproject.base.BaseActivity
import com.example.gitsimpleproject.databinding.ActivityRepositoryBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.example.gitsimpleproject.extensions.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class RepositoryActivity : BaseActivity<ActivityRepositoryBinding>(R.layout.activity_repository) {

    companion object {
        const val KEY_USER_LOGIN = "user_login"
        const val KEY_REPO_NAME = "repo_name"
    }

    internal val api by lazy { provideGithubApi(this) }

    // internal var repoCall: Call<GithubRepo>? = null

    internal val dateFormatInResponse: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()
    )
    internal val dateFormatToShow: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val login = intent.getStringExtra(KEY_USER_LOGIN)
            ?: throw IllegalArgumentException("No login info exists in extras")
        val repo = intent.getStringExtra(KEY_REPO_NAME)
            ?: throw IllegalArgumentException("No repo info exists in extras")

        showRepositoryInfo(login, repo)
    }


    private fun showRepositoryInfo(login: String, repoName: String) {
        compositeDisposable += api.getRepository(login, repoName)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnError { hideProgress(false) }
            .doAfterTerminate { hideProgress(true) }
            .subscribe({ repo ->
                // API를 통해 저장소 정보를 정상적으로 받았을 때 처리할 작업을 구현합니다.
                // 작업중 오류가 발생하면 이 블록을 호출하지 않는다.
                Glide.with(this@RepositoryActivity)
                    .load(repo.owner.avatarUrl)
                    .into(binding.ivActivityRepositoryProfile)
                binding.tvActivityRepositoryName.text = repo.fullName
                binding.tvActivityRepositoryStars.text = resources
                    .getQuantityString(R.plurals.star, repo.stars, repo.stars)
                binding.tvActivityRepositoryDescription.text =
                    repo.description ?: getString(R.string.no_description_provided)
                binding.tvActivityRepositoryLanguage.text =
                    repo.language ?: getString(R.string.no_language_specified)

                try {
                    val lastUpdate: Date = dateFormatInResponse.parse(repo.updatedAt)!!
                    binding.tvActivityRepositoryLastUpdate.text =
                        dateFormatToShow.format(lastUpdate)
                } catch (e: ParseException) {
                    binding.tvActivityRepositoryLastUpdate.text = getString(R.string.unknown)
                }

            }) {
                showError(it.message)
            }
    }

    private fun showProgress() {
        binding.llActivityRepositoryContent.visibility = View.GONE
        binding.pbActivityRepository.visibility = View.VISIBLE
    }

    private fun hideProgress(isSucceed: Boolean) {
        binding.llActivityRepositoryContent.visibility = if (isSucceed) View.VISIBLE else View.GONE
        binding.pbActivityRepository.visibility = View.GONE
    }

    private fun showError(message: String?) {
        with(binding.tvActivityRepositoryMessage) {
            text = message ?: "Unexpected error"
            visibility = View.VISIBLE
        }
    }
}