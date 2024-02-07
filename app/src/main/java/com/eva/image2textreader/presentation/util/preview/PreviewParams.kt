package com.eva.image2textreader.presentation.util.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.presentation.util.ShowContent

class BooleanPreviewParams :
	CollectionPreviewParameterProvider<Boolean>(listOf(true, false))

class ResultsModelPreviewParams : CollectionPreviewParameterProvider<List<ResultsState>>(
	listOf(
		emptyList(),
		PreviewFakes.fakeResultsModelList.map(::ResultsState)
	)
)

class ResultsShowContentPreviewParams :
	CollectionPreviewParameterProvider<ShowContent<List<ResultsState>>>(
		listOf(
			ShowContent(
				isLoading = false,
				content = emptyList()
			),
			ShowContent(
				isLoading = true,
				content = emptyList()
			),
			ShowContent(
				isLoading = false,
				content = PreviewFakes.fakeResultsModelList.map(::ResultsState)
			),
		)
	)