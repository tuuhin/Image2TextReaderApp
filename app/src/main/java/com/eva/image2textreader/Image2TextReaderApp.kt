package com.eva.image2textreader

import android.app.Application
import com.eva.image2textreader.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class Image2TextReaderApp : Application() {

	override fun onCreate() {
		super.onCreate()

		val modulesToInstall = listOf(appModule)

		startKoin {
			androidLogger()
			androidContext(this@Image2TextReaderApp)
			modules(modulesToInstall)
		}
	}

}