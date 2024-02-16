package com.eva.image2textreader.presentation.feature_results.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.image2textreader.R
import com.eva.image2textreader.presentation.feature_results.util.SortResultsOption
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortResultsDialog(
	isVisible: Boolean,
	order: SortResultsOption,
	onOrderChange: (SortResultsOption) -> Unit,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
	properties: DialogProperties = DialogProperties()
) {
	if (!isVisible) return

	BasicAlertDialog(
		onDismissRequest = onDismiss,
		modifier = modifier,
		properties = properties
	) {
		Surface(
			color = AlertDialogDefaults.containerColor,
			shape = MaterialTheme.shapes.large
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(dimensionResource(id = R.dimen.dialog_internal_padding))
			) {
				Text(
					text = stringResource(id = R.string.sort_results_title),
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier.offset(x = 12.dp)
				)
				SortResultsOption.entries.forEach { sortOption ->
					Row(
						modifier = Modifier.wrapContentWidth(),
						verticalAlignment = Alignment.CenterVertically
					) {
						RadioButton(
							selected = sortOption == order,
							onClick = { onOrderChange(sortOption) },
						)
						Text(
							text = stringResource(id = sortOption.string),
							style = MaterialTheme.typography.bodyMedium,
							color = MaterialTheme.colorScheme.onSurfaceVariant
						)
					}
				}
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
fun SortResultsDialogPreview() = Image2TextReaderTheme {
	SortResultsDialog(
		isVisible = true,
		order = SortResultsOption.TIME_OF_CREATE,
		onOrderChange = {},
		onDismiss = { }
	)
}
