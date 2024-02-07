package com.eva.image2textreader.presentation.feature_results.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.image2textreader.R
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun SavedResultsCardActions(
	onShareAction: () -> Unit,
	onDeleteAction: () -> Unit,
	isActionEnabled: Boolean,
	modifier: Modifier = Modifier,
	contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	errorColor: Color = MaterialTheme.colorScheme.onErrorContainer,
) {
	AnimatedVisibility(
		visible = isActionEnabled,
		enter = expandVertically() + fadeIn(),
		exit = shrinkVertically() + fadeOut(),
		modifier = modifier.wrapContentWidth()
	) {
		Row {
			TextButton(
				onClick = onShareAction,
				enabled = isActionEnabled,
				colors = ButtonDefaults
					.textButtonColors(contentColor = contentColor),
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_share),
					contentDescription = stringResource(id = R.string.share_icon_desc),
				)
				Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.text_button_space)))
				Text(
					text = stringResource(id = R.string.share_icon_text),
					style = MaterialTheme.typography.bodyMedium
				)
			}
			TextButton(
				onClick = onDeleteAction,
				enabled = isActionEnabled,
				colors = ButtonDefaults
					.textButtonColors(contentColor = errorColor)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_delete),
					contentDescription = stringResource(id = R.string.delete_icon_desc),
				)
				Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.text_button_space)))
				Text(
					text = stringResource(id = R.string.delete_icon_text),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
fun SavedResultsCardActionPreview() = Image2TextReaderTheme {
	Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)) {
		SavedResultsCardActions(
			isActionEnabled = true,
			onDeleteAction = {},
			onShareAction = {}

		)
	}
}