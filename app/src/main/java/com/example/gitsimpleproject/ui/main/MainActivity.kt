package com.example.gitsimpleproject.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.databinding.ActivityMainBinding
import com.example.gitsimpleproject.ui.search.SearchActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnActivityMainSearch.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.btnActivityMainSearch -> {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }

    }
}