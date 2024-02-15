package com.eva.image2textreader.presentation.feature_edit.utils

import android.net.Uri
import com.eva.image2textreader.domain.models.ResultsModel

data class FileSaverState(
	val isPreparing: Boolean = false,
	val fileUri: Uri? = null,
	val result: ResultsModel? = null,
)
