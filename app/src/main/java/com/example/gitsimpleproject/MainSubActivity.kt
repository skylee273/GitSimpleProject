package com.example.gitsimpleproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gitsimpleproject.databinding.ActivityMainSubBinding

class MainSubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainSubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainSubBinding.inflate(layoutInflater)
        setContentView(binding.root) // contentView에 등록

        binding.btnSubmit.setOnClickListener{ view ->
            binding.tvMessage.text = "Hello"
        }
    }

}