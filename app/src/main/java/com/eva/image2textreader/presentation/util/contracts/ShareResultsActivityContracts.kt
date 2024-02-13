package com.eva.image2textreader.presentation.util.contracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel

class ShareResultsActivityContracts : ActivityResultContract<ResultsModel, Unit>() {

	override fun createIntent(context: Context, input: ResultsModel): Intent {
		val intent = Intent(Intent.ACTION_SEND).apply {
			putExtra(Intent.EXTRA_TEXT, input.text)
			input.imageUri?.let {
				val uri = Uri.parse(it)
				setDataAndType(uri, "text/plain")
				putExtra(Intent.EXTRA_STREAM, uri)
				flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
			} ?: run { type = "text/plain" }
		}
		return Intent.createChooser(intent, context.getString(R.string.share_results_title))
	}

	override fun parseResult(resultCode: Int, intent: Intent?): Unit = Unit

}