package com.example.gitsimpleproject.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable


abstract class BaseActivity<B: ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {

    lateinit var binding: B
    //val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    override fun onStop() {
        super.onStop()
       // compositeDisposable.clear()
    }

}