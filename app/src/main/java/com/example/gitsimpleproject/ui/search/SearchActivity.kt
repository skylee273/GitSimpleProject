package com.example.gitsimpleproject.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.model.GithubRepo
import com.example.gitsimpleproject.api.model.RepoSearchResponse
import com.example.gitsimpleproject.api.provideGithubApi
import com.example.gitsimpleproject.databinding.ActivitySearchBinding
import com.example.gitsimpleproject.ui.repo.RepositoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(), SearchAdapter.ItemClickListener {
    private lateinit var binding: ActivitySearchBinding
    private var searchCall: Call<RepoSearchResponse>? = null
    private lateinit var menuSearch: MenuItem
    private var searchView: SearchView? = null

    internal val adapter by lazy {
        // apply() 함수를 사용하여 객체 생성과 함수 호출을 한번에 수행한다.
        SearchAdapter().apply {
            setItemClickListener(this@SearchActivity)
        }
    }

    internal val api by lazy {
        provideGithubApi(this)
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menuSearch = menu!!.findItem(R.id.menu_activity_search_query)

        searchView = (menuSearch.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    updateTitle(query ?: "")
                    hideSoftKeyboard()
                    collapseSearchView()
                    searchRepository(query ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        // with() 함수를 사용하여 menuSearch 범위 내에서 작업을 수행한다.
        with(menuSearch) {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                    if ("" == searchView!!.query) {
                        finish()
                    }
                    return true
                }
            })
        }

        menuSearch.expandActionView()
        return true
    }

    override fun onStop() {
        super.onStop()
        // 액티비티가 화면에서 사라지는 시점에 API호출 객체가 생성되어 있다면
        // API 요청을 취소한다.
        searchCall?.run { cancel() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_activity_search_query == item.itemId) {
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchRepository(query: String) {
        clearResults()
        hideError()
        showProgress()

        /*
            앞에서 API 호출에 필요한 객체를 받았으므로, 이 시점에서 searchCall 객체의 값은 널이 아닙니다.
            따라서 비 널 값 보증(!!)을 사용하여 이 객체를 사용합니다.
         */
        searchCall = api.searchRepository(query)
        searchCall!!.enqueue(object : Callback<RepoSearchResponse?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<RepoSearchResponse?>,
                response: Response<RepoSearchResponse?>
            ) {
                hideProgress()
                val searchResult: RepoSearchResponse? = response.body()
                if (response.isSuccessful && null != searchResult) {
                    // with() 함수를 사용하여 adapter 범위 내에서 작업을 수행한다.
                    with(adapter) {
                        setItems(searchResult.items)
                        notifyDataSetChanged()
                    }
                    if (0 == searchResult.totalCount) {
                        showError(getString(R.string.no_search_result))
                    }
                } else {
                    showError("Not successful: " + response.message())
                }
            }

            override fun onFailure(call: Call<RepoSearchResponse?>, t: Throwable) {
                hideProgress()
                showError(t.message!!)
            }
        })
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

    override fun onItemClick(repository: GithubRepo?) {
        // apply() 함수를 사용하여 객체 생성과 extra를 추가하는 작업을 동시에 수행한다.
        val intent = Intent(this, RepositoryActivity::class.java).apply {
            putExtra(RepositoryActivity.KEY_USER_LOGIN, repository!!.owner.login)
            putExtra(RepositoryActivity.KEY_REPO_NAME, repository.name)
        }
        startActivity(intent)
    }
}