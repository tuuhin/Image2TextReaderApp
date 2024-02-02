package com.eva.image2textreader.presentation.util.preview

import com.eva.image2textreader.domain.models.ResultsModel
import java.time.LocalDateTime

object PreviewFakes {
	val fakeResultModel = ResultsModel(
		id = 0,
		text = "This is some random preview string. These were the content of the model to be analysed",
		lastUpdated = LocalDateTime.now()
	)

	val fakeResultsModelList = List(20) { fakeResultModel.copy(id = it) }

}