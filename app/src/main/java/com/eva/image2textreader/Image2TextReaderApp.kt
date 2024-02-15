package com.eva.image2textreader

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.getSystemService
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.eva.image2textreader.di.appModule
import com.eva.image2textreader.di.dbModule
import com.eva.image2textreader.di.filesModule
import com.eva.image2textreader.di.mlModule
import com.eva.image2textreader.util.NotificationConstants
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class Image2TextReaderApp : Application(), ImageLoaderFactory {

	private val notificationManager by lazy { getSystemService<NotificationManager>() }

	override fun onCreate() {
		super.onCreate()

		//Create notification channel
		createNotificationChannel()

		val modulesToInstall = listOf(appModule, mlModule, dbModule, filesModule)
		startKoin {
			androidLogger()
			androidContext(this@Image2TextReaderApp)
			modules(modulesToInstall)
		}
	}

	override fun newImageLoader(): ImageLoader {
		// In Memory Cache for the images
		val memoryCache = MemoryCache.Builder(this)
			.maxSizeBytes(2048)
			.maxSizePercent(.25)
			.build()

		val debugLogger = DebugLogger()

		return ImageLoader(this).newBuilder()
			.crossfade(true)
			.crossfade(600)
			.decoderDispatcher(Dispatchers.Default)
			.memoryCachePolicy(CachePolicy.ENABLED)
			.diskCachePolicy(CachePolicy.DISABLED)
			.memoryCache(memoryCache)
			.logger(debugLogger)
			.build()
	}

	private fun createNotificationChannel() {
		val channel = NotificationChannel(
			NotificationConstants.CHANNEL_ID,
			NotificationConstants.CHANNEL_TITLE,
			NotificationManager.IMPORTANCE_DEFAULT
		).apply {
			description = NotificationConstants.CHANNEL_DESC
			setShowBadge(false)
		}
		notificationManager?.createNotificationChannel(channel)
	}

}