package com.example.gitsimpleproject.extensions

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    // CompositeDisposable.add() 함수 호출
    this.add(disposable)
}