package com.eva.image2textreader.di


import com.eva.image2textreader.presentation.feature_results.ResultsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
	// Results ViewModel
	viewModelOf(::ResultsViewModel)

}