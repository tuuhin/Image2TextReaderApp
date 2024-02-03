package com.eva.image2textreader.presentation.feature_recognizer.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.RecognizedTextModel

@Composable
fun RecognizedTextMetaData(
	model: RecognizedTextModel,
	modifier: Modifier = Modifier,
	contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = containerColor,
			contentColor = contentColor
		),
		shape = MaterialTheme.shapes.large,
		modifier = modifier,
	) {
		Column(
			modifier = Modifier.padding(all = dimensionResource(id = R.dimen.card_internal_padding))
		) {
			Text(
				text = buildAnnotatedString {
					withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
						append("Language")
					}
					append("         ")
					append(model.languageCode)
				},
			)
		}
	}
}