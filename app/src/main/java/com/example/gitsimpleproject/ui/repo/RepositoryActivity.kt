package com.example.gitsimpleproject.ui.repo

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.GithubApi
import com.example.gitsimpleproject.api.GithubApiProvider
import com.example.gitsimpleproject.api.model.GithubRepo
import com.example.gitsimpleproject.databinding.ActivityRepositoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class RepositoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRepositoryBinding
    var api: GithubApi? = null

    var repoCall: Call<GithubRepo>? = null
    val KEY_USER_LOGIN = "user_login"
    val KEY_REPO_NAME = "repo_name"
    var dateFormatInResponse: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()
    )
    var dateFormatToShow: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
    )
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GithubApiProvider.provideGithubApi(this)

        val login = intent.getStringExtra(KEY_USER_LOGIN)
            ?: throw IllegalArgumentException("No login info exists in extras")
        val repo = intent.getStringExtra(KEY_REPO_NAME)
            ?: throw IllegalArgumentException("No repo info exists in extras")

        showRepositoryInfo(login, repo)
    }


    private fun showRepositoryInfo(login: String, repoName: String) {
        showProgress()
        repoCall = api!!.getRepository(login, repoName)
        repoCall!!.enqueue(object : Callback<GithubRepo?> {
            override fun onResponse(call: Call<GithubRepo?>, response: Response<GithubRepo?>) {
                hideProgress(true)
                val repo: GithubRepo? = response.body()
                if (response.isSuccessful && null != repo) {
                    Glide.with(this@RepositoryActivity)
                        .load(repo.owner!!.avatarUrl)
                        .into(binding.ivActivityRepositoryProfile)
                    binding.tvActivityRepositoryName.text = repo.fullName
                    binding.tvActivityRepositoryStars.text = resources
                        .getQuantityString(R.plurals.star, repo.stars, repo.stars)

                    binding.tvActivityRepositoryDescription.text = repo.description ?: getString(R.string.no_description_provided)
                    binding.tvActivityRepositoryLanguage.text = repo.language ?: getString(R.string.no_language_specified)

                    try {
                        val lastUpdate: Date = dateFormatInResponse.parse(repo.updatedAt!!)!!
                        binding.tvActivityRepositoryLastUpdate.text = dateFormatToShow.format(lastUpdate)
                    } catch (e: ParseException) {
                        binding.tvActivityRepositoryLastUpdate.text = getString(R.string.unknown)
                    }
                } else {
                    showError("Not successful: " + response.message())
                }
            }

            override fun onFailure(call: Call<GithubRepo?>, t: Throwable) {
                hideProgress(false)
                showError(t.message)
            }


        })
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
        binding.tvActivityRepositoryMessage.text = message
        binding.tvActivityRepositoryMessage.visibility = View.VISIBLE
    }
}