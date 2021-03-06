package com.teamnexters.android.mealdiary.di

import android.content.res.Resources
import com.teamnexters.android.mealdiary.ui.main.DiaryAdapter
import com.teamnexters.android.mealdiary.ui.main.MainViewModel
import com.teamnexters.android.mealdiary.ui.write.WriteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { androidContext().resources as Resources }

    viewModel { MainViewModel.ViewModel(get(), get()) }

    viewModel { WriteViewModel.ViewModel(get(), get()) }

    factory { DiaryAdapter() }
}