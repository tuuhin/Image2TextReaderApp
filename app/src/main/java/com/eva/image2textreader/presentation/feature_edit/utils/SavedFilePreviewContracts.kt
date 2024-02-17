package com.eva.image2textreader.presentation.feature_edit.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class SavedFilePreviewContracts : ActivityResultContract<FileSaverState, Unit>() {

	override fun createIntent(context: Context, input: FileSaverState): Intent {
		return Intent(Intent.ACTION_VIEW).apply {
			setDataAndType(input.fileUri, "text/plain")
			putExtra(Intent.EXTRA_TITLE, "RESULTS")
			putExtra(Intent.EXTRA_TEXT, input.result?.text)
			flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
		}
	}

	override fun parseResult(resultCode: Int, intent: Intent?) = Unit
}