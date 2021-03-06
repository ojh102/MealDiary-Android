package com.teamnexters.android.mealdiary.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.teamnexters.android.mealdiary.MealDiaryConst
import com.teamnexters.android.mealdiary.ui.Screen
import com.teamnexters.android.mealdiary.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

internal abstract class BaseActivity<VB : ViewDataBinding, VM: BaseViewModel> : AppCompatActivity() {

    protected val disposables = CompositeDisposable()

    protected val schedulerProvider: SchedulerProvider by inject()

    abstract val layoutResId: Int

    abstract val viewModel: VM

    protected val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<VB>(this, layoutResId)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding.setLifecycleOwner(this)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        disposables.clear()
    }

    protected fun getScreen(): Screen {
        return intent.getBundleExtra(MealDiaryConst.KEY_ARGS).getSerializable(MealDiaryConst.KEY_ARGS) as Screen
    }
}