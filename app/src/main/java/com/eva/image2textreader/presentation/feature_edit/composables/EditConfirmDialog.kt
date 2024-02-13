package com.eva.image2textreader.presentation.feature_edit.composables

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
fun EditConfirmDialog(
	isVisible: Boolean,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
) {
	if (!isVisible) return

	AlertDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			TextButton(
				onClick = {
					onConfirm()
					onDismiss()
				},
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
			) {
				Text(text = stringResource(id = R.string.dialog_done_text))
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
		title = { Text(text = stringResource(id = R.string.edit_confirm_dialog_title)) },
		text = { Text(text = stringResource(id = R.string.edit_confirm_dialog_text)) },
		modifier = modifier,
		shape = MaterialTheme.shapes.large,
		properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
	)
}

@PreviewLightDark
@Composable
fun EditConfirmDialogPreview() = Image2TextReaderTheme(dynamicColor = false) {
	EditConfirmDialog(isVisible = true, onConfirm = {}, onDismiss = {})
}