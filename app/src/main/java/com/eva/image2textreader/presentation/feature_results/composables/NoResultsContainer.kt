package com.eva.image2textreader.presentation.feature_results.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun NoResultsContainer(
	modifier: Modifier = Modifier
) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.sizeIn(maxWidth = dimensionResource(id = R.dimen.results_not_found_container_width)),
	) {
		Image(
			painter = painterResource(id = R.drawable.ic_no_files),
			contentDescription = stringResource(id = R.string.no_history_found),
			colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
		)
		Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.results_spacing)))
		Text(
			text = stringResource(id = R.string.no_results),
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center
		)
		Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.results_spacing)))
		Text(
			text = stringResource(id = R.string.no_results_desc),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
		)
	}
}

@PreviewLightDark
@Composable
fun NoResultsContainerPreview() = Image2TextReaderTheme {
	Surface(color = MaterialTheme.colorScheme.surface) {
		NoResultsContainer(modifier = Modifier.padding(horizontal = 16.dp))
	}
}