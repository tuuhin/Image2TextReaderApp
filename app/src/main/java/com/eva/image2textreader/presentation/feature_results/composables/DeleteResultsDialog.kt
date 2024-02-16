package com.eva.image2textreader.presentation.feature_results.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.DialogProperties
import com.eva.image2textreader.R
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun DeleteResultsDialog(
	isVisible: Boolean,
	title: String,
	text: String,
	onConfirm: () -> Unit,
	onDisMiss: () -> Unit,
	modifier: Modifier = Modifier,
	properties: DialogProperties = DialogProperties()
) {
	if (!isVisible) return

	AlertDialog(
		onDismissRequest = onDisMiss,
		confirmButton = {
			TextButton(
				onClick = onConfirm,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.onErrorContainer)
			) {
				Text(text = stringResource(id = R.string.delete_text))
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDisMiss,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
			) {
				Text(text = stringResource(id = R.string.dialog_cancel_text))
			}
		},
		title = { Text(text = title) },
		text = { Text(text = text) },
		textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
		titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
		modifier = modifier,
		properties = properties,
		shape = MaterialTheme.shapes.extraLarge
	)
}

@PreviewLightDark
@Composable
fun DeleteResultsDialogPreview() = Image2TextReaderTheme {
	DeleteResultsDialog(
		isVisible = true,
		title = stringResource(id = R.string.delete_current_confirm_title),
		text = stringResource(id = R.string.delete_current_confirm_text),
		onConfirm = { },
		onDisMiss = { },
	)
}