package com.eva.image2textreader.presentation.feature_results.composables

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.image2textreader.R
import com.eva.image2textreader.presentation.feature_results.util.ResultsState
import com.eva.image2textreader.presentation.util.ShowContent
import com.eva.image2textreader.presentation.util.preview.ResultsShowContentPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun ResultsContent(
	results: ShowContent<List<ResultsState>>,
	modifier: Modifier = Modifier,
	successContent: @Composable (List<ResultsState>) -> Unit,
) {
	Crossfade(
		targetState = results.isLoading,
		label = "Loading Transition",
		modifier = modifier,
		animationSpec = tween(durationMillis = 400, easing = LinearEasing)
	) { isLoading ->
		when {
			isLoading -> Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				CircularProgressIndicator(
					color = MaterialTheme.colorScheme.secondary,
					trackColor = MaterialTheme.colorScheme.surfaceVariant,
				)
			}

			results.content.isEmpty() -> NoResultsContainer(
				modifier = Modifier
					.fillMaxSize()
					.padding(horizontal = dimensionResource(R.dimen.scaffold_padding))
			)

			else -> successContent(results.content)
		}

	}
}

@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
	apiLevel = 33,
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ResultsContentPreview(
	@PreviewParameter(ResultsShowContentPreviewParams::class)
	content: ShowContent<List<ResultsState>>,
) = Image2TextReaderTheme {
	Surface(color = MaterialTheme.colorScheme.surface) {
		ResultsContent(results = content) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "With content")
			}
		}
	}
}