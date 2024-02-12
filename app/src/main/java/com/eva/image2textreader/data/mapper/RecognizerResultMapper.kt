package com.eva.image2textreader.data.mapper

import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.util.now
import kotlinx.datetime.LocalDateTime

object RecognizerResultMapper {

	fun toResultModel(model: RecognizedTextModel, image: String? = null): ResultsModel {
		return ResultsModel(
			id = null,
			text = model.wholeText,
			lastUpdated = LocalDateTime.now(),
			imageUri = image,
			languageCode = model.languageCode
		)
	}
}