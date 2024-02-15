package com.eva.image2textreader.presentation.util.preview

import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.util.now
import kotlinx.datetime.LocalDateTime

object PreviewFakes {
	val fakeResultModel = ResultsModel(
		id = 0,
		text = "This is some random preview string. These were the content of the model to be analysed",
		lastUpdated = LocalDateTime.now()
	)

	val fakeResultsModelList = List(20) { fakeResultModel.copy(id = it.toLong()) }

	val fakeRecognizedTextModel = RecognizedTextModel(
		wholeText = "This is a very long text",
		languageCode = null,
		linesText = emptyList(),
		textBlocksText = emptyList()
	)

}