package com.example.gitsimpleproject.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gitsimpleproject.api.GithubApi
import com.example.gitsimpleproject.api.model.GithubRepo
import com.example.gitsimpleproject.extensions.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException

class SearchViewModel(
    application: Application,
    val api: GithubApi
) : AndroidViewModel(application) {
   /* private val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData(false)
    private val _isError = MutableLiveData(false)
    private val _isErrorMessage = MutableLiveData("")
    private val _isClearResult = MutableLiveData(false)
    private val _gitHubRepoItems = MutableLiveData<MutableList<GithubRepo>>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    val isError: LiveData<Boolean> get() = _isError
    val isErrorMessage: MutableLiveData<String> get() = _isErrorMessage
    val isClearResult : MutableLiveData<Boolean> get() = _isClearResult
    val gitHubRepoItems : MutableLiveData<MutableList<GithubRepo>> get() = _gitHubRepoItems

    private fun searchRepository(query: String) {
        compositeDisposable += api.searchRepository(query)
            .flatMap {
                if (0 == it.totalCount) {
                    Single.error(IllegalStateException("No search result"))
                } else {
                    Single.just(it.items)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // clearResults()
                _isClearResult.value = true
                // hideError()
                _isError.value = false
                _isErrorMessage.value = ""
                //showProgress()
                _isLoading.value = true
            }
            .doOnTerminate {
                // hideProgress()
                _isLoading.value = false
            }
            .subscribe({ items ->
                _gitHubRepoItems.value = items.toMutableList()
            }) {
                _isError.value = true
                _isErrorMessage.value = it.message ?: "No Message"
            }
    }*/
}