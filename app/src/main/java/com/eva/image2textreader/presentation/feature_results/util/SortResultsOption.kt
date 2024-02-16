package com.eva.image2textreader.presentation.feature_results.util

import androidx.annotation.StringRes
import com.eva.image2textreader.R

enum class SortResultsOption(
	@StringRes val string: Int
) {
	TIME_OF_CREATE(R.string.time_of_create),
	TIME_OF_UPDATE(R.string.time_of_update)
}