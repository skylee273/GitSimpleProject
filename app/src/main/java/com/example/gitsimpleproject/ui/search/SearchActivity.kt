package com.example.gitsimpleproject.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.model.GithubRepo
import com.example.gitsimpleproject.api.provideGithubApi
import com.example.gitsimpleproject.base.BaseActivity
import com.example.gitsimpleproject.databinding.ActivitySearchBinding
import com.example.gitsimpleproject.ui.repo.RepositoryActivity
import com.example.gitsimpleproject.extensions.plusAssign
import com.example.gitsimpleproject.rx.AutoClearedDisposable
import com.jakewharton.rxbinding4.widget.queryTextChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException


class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search), SearchAdapter.ItemClickListener {
    private lateinit var menuSearch: MenuItem
    private lateinit var searchView: SearchView


    private val adapter by lazy {
        // apply() 함수를 사용하여 객체 생성과 함수 호출을 한번에 수행한다.

        SearchAdapter().apply {
            setItemClickListener(this@SearchActivity)
        }
    }

    internal val api by lazy {
        provideGithubApi(this)
    }

    internal val disposables = AutoClearedDisposable(this)

    internal val viewDisposables
            = AutoClearedDisposable(lifecycleOwner = this, alwaysClearOnStop = false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle += disposables
        lifecycle += viewDisposables


        with(binding.rvActivitySearchList) {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menuSearch = menu!!.findItem(R.id.menu_activity_search_query)

        searchView = (menuSearch.actionView as SearchView)
        viewDisposables += searchView.queryTextChangeEvents()
            .filter { it.isSubmitted }
            .map { it.queryText }
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ query ->
                updateTitle(query)
                hideSoftKeyboard()
                collapseSearchView()
                searchRepository(query)
            }


        // with() 함수를 사용하여 menuSearch 범위 내에서 작업을 수행한다.
        with(menuSearch) {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                    if ("" == searchView.query) {
                        finish()
                    }
                    return true
                }
            })
        }
        menuSearch.expandActionView()
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_activity_search_query == item.itemId) {
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchRepository(query: String) {
        disposables += api.searchRepository(query)
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
                clearResults()
                hideError()
                showProgress()
            }
            .doOnTerminate { hideProgress() }
            .subscribe({ items ->
                with(adapter) {
                    setItems(items)
                    notifyDataSetChanged()
                }
            }) {
                showError(it.message!!)
            }
    }

    private fun updateTitle(query: String) {
        // 별도의 변수 선언 없이
        // getSupportActionBar()의 반환값이 널이 아닌 경우에만 작업을 수행한다.
        supportActionBar?.run {
            subtitle = query
        }
    }

    private fun hideSoftKeyboard() {
        // 별도의 변수 선언 없이 획득한 인스턴스의 범위 내에서 작업을 수행한다.
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(searchView!!.windowToken, 0)
        }
    }

    private fun collapseSearchView() {
        menuSearch.collapseActionView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearResults() {
        // with() 함수를 사용하여 adapter 범위 내에서 작업을 수행한다.
        with(adapter) {
            clearItems()
            notifyDataSetChanged()
        }
    }

    private fun showProgress() {
        binding.pbActivitySearch.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.pbActivitySearch.visibility = View.GONE
    }

    private fun showError(message: String) {
        with(binding.tvActivitySearchMessage) {
            text = message ?: "Unexpected error."
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        with(binding.tvActivitySearchMessage) {
            text = ""
            visibility = View.GONE
        }
    }

    override fun onItemClick(repository: GithubRepo) {
        // apply() 함수를 사용하여 객체 생성과 extra를 추가하는 작업을 동시에 수행한다.
        val intent = Intent(this, RepositoryActivity::class.java).apply {
            putExtra(RepositoryActivity.KEY_USER_LOGIN, repository.owner.login)
            putExtra(RepositoryActivity.KEY_REPO_NAME, repository.name)
        }
        startActivity(intent)
    }


}