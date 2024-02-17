package com.eva.image2textreader.presentation.util.contracts

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel

class ShareResultsActivityContracts : ActivityResultContract<ResultsModel, Unit>() {

	override fun createIntent(context: Context, input: ResultsModel): Intent {
		val intent = Intent(Intent.ACTION_SEND).apply {
			val imageUri = input.imageUri?.toUri()
			setDataAndType(imageUri, "text/plain")
			putExtra(Intent.EXTRA_TEXT, input.text)
			putExtra(Intent.EXTRA_STREAM, imageUri)
			flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
		}
		return Intent.createChooser(intent, context.getString(R.string.share_results_title))
	}

	override fun parseResult(resultCode: Int, intent: Intent?): Unit = Unit

}