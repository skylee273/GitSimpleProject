package com.example.gitsimpleproject.rx

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class AutoClearedDisposable(
    // 생명주기를 참조할 액티비티
    private val lifecycleOwner: AppCompatActivity,
    /**
     * onStop()  콜백 함수가 호출되었을 때,
     * 관리하고 있는 디스포저블 객체를 해제할지 여부 지정
     * 기본값은 true
     */
    private val alwaysClearOnStop: Boolean = true,
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : LifecycleObserver {

    // 디스포저블 추가
    fun add(disposable: Disposable) {
        /**
         * LifecycleOwner.lifecycle을 사용하여
         * 참조하고 있는 컴포넌트의 Lifecycle 객체에 접근
         * Lifecycle.currentState를 사용하여 상태 정보인 Lifecycle.State 접근
         * Lifecycle.State.isAtLeast() 함수를 사영하여
         * 현재 상태가 특정 상태의 이후 상태인지 여부를 반환
         * 코틀린 표준 라이브러리에서 제공하는 check() 함수로
         * Lifecycle.State.isAtLeast() 함수의 반환값이 참인지 확인
         * 만약 참이 아닌경우 IllegalStateException 발생
         *
         */
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
        compositeDisposable.add(disposable)
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun cleanUp() {
        if (!alwaysClearOnStop && !lifecycleOwner.isFinishing) {
            return
        }
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachSelf() {
        compositeDisposable.clear()
        lifecycleOwner.lifecycle.removeObserver(this)
    }

}