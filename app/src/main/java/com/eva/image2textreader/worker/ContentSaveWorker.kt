package com.eva.image2textreader.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.fs.ExternalFileSaver
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.util.NotificationConstants
import com.eva.image2textreader.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ContentSaveWorker(
	private val context: Context,
	private val params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

	private val fileSaver by inject<ExternalFileSaver>()

	override suspend fun doWork(): Result {
		setForeground(foregroundInfo)
		// downloading the file
		val textContent = params.inputData.getString(WorkParamsKeys.FILE_TEXT_CONTENT)
			?: return Result.failure()

		val datetime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
		val fileName = "file-export.txt$datetime"

		return try {
			val res = fileSaver.saveFile(fileName, textContent)
				?: return Result.failure(
					workDataOf(WorkParamsKeys.FAILED_KEY to "Saver returned null for some reason")
				)
			Result.success(workDataOf(WorkParamsKeys.SUCCESS_KEY to res))
		} catch (e: Exception) {
			Result.failure(workDataOf(WorkParamsKeys.FAILED_KEY to e.message))
		}
	}


	private val foregroundInfo: ForegroundInfo
		get() {

			val appName = context.getString(R.string.notification_title)
			val tickerText = context.getString(R.string.notification_ticker_text)
			val text = context.getString(R.string.notification_content_text)

			val cancellationIntent = WorkManager.getInstance(context)
				.createCancelPendingIntent(id)

			val cancelAction = NotificationCompat.Action.Builder(0, "Cancel", cancellationIntent)
				.build()


			val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
				.setContentTitle(appName)
				.setTicker(tickerText)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentText(text)
				.setOngoing(true)
				.addAction(cancelAction)
				.build()

			return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
				ForegroundInfo(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
			else ForegroundInfo(1, notification)
		}

	companion object {

		private val constrains = Constraints.Builder()
			.setRequiresStorageNotLow(false)
			.build()

		fun startSaveWorker(context: Context, result: ResultsModel): UUID {
			val resultsId = "SAVE_ID_FOR_${result.id}"

			val workData = workDataOf(
				WorkParamsKeys.FILE_TEXT_CONTENT to result.text,
			)

			val worker = OneTimeWorkRequestBuilder<ContentSaveWorker>()
				.setInputData(workData)
				.setConstraints(constrains)
				.addTag(tag = WorkParamsKeys.CONTENT_SAVE_WORKER_TAG)
				.setInitialDelay(Duration.ofSeconds(1))
				.setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(5))
				.build()

			WorkManager.getInstance(context)
				.enqueueUniqueWork(resultsId, ExistingWorkPolicy.REPLACE, worker)

			return worker.id
		}

		fun observeWorkerState(context: Context, workerId: UUID): Flow<Resource<String>?> {
			return WorkManager.getInstance(context)
				.getWorkInfoByIdFlow(workerId)
				.cancellable()
				.map { workInfo ->
					if (workInfo == null) return@map null

					val successData = workInfo.outputData
						.getString(WorkParamsKeys.SUCCESS_KEY)

					val failedData = workInfo.outputData
						.getString(WorkParamsKeys.FAILED_KEY)

					when (workInfo.state) {
						WorkInfo.State.SUCCEEDED -> successData?.let { Resource.Success(data = it) }
						WorkInfo.State.FAILED -> failedData?.let { Resource.Error(message = it) }
						WorkInfo.State.RUNNING -> Resource.Loading
						else -> null
					}
				}
		}
	}
}