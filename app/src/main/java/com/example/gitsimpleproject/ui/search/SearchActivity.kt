package com.example.gitsimpleproject.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.GithubApi
import com.example.gitsimpleproject.api.GithubApiProvider
import com.example.gitsimpleproject.api.model.RepoSearchResponse
import com.example.gitsimpleproject.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    var api: GithubApi? = null
    private var searchCall: Call<RepoSearchResponse>? = null
    private var menuSearch: MenuItem? = null
    private var searchView: SearchView? = null
    var adapter: SearchAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GithubApiProvider.provideGithubApi(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menuSearch = menu!!.findItem(R.id.menu_activity_search_query)

        searchView = menuSearch!!.actionView as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateTitle(query ?: "")
                hideSoftKeyboard()
                collapseSearchView()
                searchRepository(query ?: "")
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        menuSearch!!.expandActionView()
        return true
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
        searchCall = api!!.searchRepository(query)
        searchCall!!.enqueue(object : Callback<RepoSearchResponse?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<RepoSearchResponse?>,
                response: Response<RepoSearchResponse?>
            ) {
                hideProgress()
                val searchResult: RepoSearchResponse? = response.body()
                if (response.isSuccessful && null != searchResult) {
                    adapter!!.setItems(searchResult.items)
                    adapter!!.notifyDataSetChanged()
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
        supportActionBar?.subtitle = query
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView!!.windowToken, 0)
    }

    private fun collapseSearchView() {
        menuSearch!!.collapseActionView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearResults() {
        adapter!!.clearItems()
        adapter!!.notifyDataSetChanged()
    }

    private fun showProgress() {
        binding.pbActivitySearch.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.pbActivitySearch.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.tvActivitySearchMessage.text = message
        binding.tvActivitySearchMessage.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.tvActivitySearchMessage.text = ""
        binding.tvActivitySearchMessage.visibility = View.GONE
    }
}