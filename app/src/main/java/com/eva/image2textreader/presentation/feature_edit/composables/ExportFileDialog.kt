package com.eva.image2textreader.presentation.feature_edit.composables

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.eva.image2textreader.R
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun ExportFileDialog(
	isVisible: Boolean,
	onDismiss: () -> Unit,
	onConfirm: () -> Unit,
	modifier: Modifier = Modifier
) {
	if (!isVisible) return

	AlertDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			TextButton(
				onClick = onConfirm,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
			) {
				Text(text = stringResource(id = R.string.export_as_text))
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDismiss,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.onErrorContainer)
			) {
				Text(text = stringResource(id = R.string.dialog_cancel_text))
			}
		},
		title = { Text(text = stringResource(id = R.string.export_text_dialog_title)) },
		text = { Text(text = stringResource(id = R.string.export_text_dialog_text)) },
		shape = MaterialTheme.shapes.extraLarge,
		properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
		modifier = modifier,
	)
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
fun ExportFileDialogPreview() = Image2TextReaderTheme(dynamicColor = false) {
	ExportFileDialog(isVisible = true, onDismiss = {}, onConfirm = {})
}