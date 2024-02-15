package com.eva.image2textreader.presentation.feature_recognizer.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.RecognizedTextModel
import com.eva.image2textreader.presentation.util.preview.PreviewFakes
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun RecognizedTextMetaData(
	model: RecognizedTextModel,
	modifier: Modifier = Modifier,
	contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
	cardShape: Shape = MaterialTheme.shapes.medium
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = containerColor,
			contentColor = contentColor
		),
		shape = cardShape,
		modifier = modifier,
	) {
		Column(
			modifier = Modifier.padding(all = dimensionResource(id = R.dimen.card_internal_padding)),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			model.languageCode?.let { code ->
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.fillMaxWidth(),
				) {
					Text(
						text = stringResource(id = R.string.text_metadata_recognized_language),
						style = MaterialTheme.typography.titleMedium
					)
					Text(text = code, style = MaterialTheme.typography.bodyMedium)
				}
			}
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth(),
			) {
				Text(
					text = stringResource(id = R.string.text_metadata_line_count),
					style = MaterialTheme.typography.titleMedium
				)
				Text(text = "${model.linesCount}", style = MaterialTheme.typography.bodyMedium)
			}
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth(),
			) {
				Text(
					text = stringResource(id = R.string.text_metadata_words_count),
					style = MaterialTheme.typography.titleMedium
				)
				Text(
					text = "${model.wordsCount}",
					style = MaterialTheme.typography.bodyMedium
				)

			}
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
fun RecognizedTextMetaDataPreview() = Image2TextReaderTheme {
	RecognizedTextMetaData(
		model = PreviewFakes.fakeRecognizedTextModel,
		modifier = Modifier.fillMaxWidth()
	)
}