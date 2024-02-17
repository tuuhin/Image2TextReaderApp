package com.eva.image2textreader.presentation.util.contracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.eva.image2textreader.R

class ImagePreviewFromUriContracts : ActivityResultContract<Uri, Unit>() {

	override fun createIntent(context: Context, input: Uri): Intent {
		val chooserTitle = context.getString(R.string.image_preview_title)

		val intent = Intent(Intent.ACTION_VIEW)
			.apply {
				setDataAndType(input, "image/*")
				putExtra(Intent.EXTRA_TITLE, chooserTitle)
				flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
			}

		return Intent.createChooser(intent, chooserTitle)
	}

	override fun parseResult(resultCode: Int, intent: Intent?) = Unit
}