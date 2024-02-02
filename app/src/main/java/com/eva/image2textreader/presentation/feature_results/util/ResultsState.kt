package com.eva.image2textreader.presentation.feature_results.util

import com.eva.image2textreader.domain.models.ResultsModel

data class ResultsState(
	val isSelected: Boolean = false,
	val model: ResultsModel,
) {
	constructor(model: ResultsModel) : this(model = model, isSelected = false)
}
