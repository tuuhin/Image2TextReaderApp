package com.eva.image2textreader.presentation.feature_recognizer.composables

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eva.image2textreader.R
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun LoadingContentDialog(
	isVisible: Boolean,
	modifier: Modifier = Modifier,
	onDismissRequest: () -> Unit = {},
	shape: Shape = MaterialTheme.shapes.medium,
) {
	if (!isVisible) return

	val transition = rememberInfiniteTransition(label = "Image Transition")

	val alphaTransition by transition.animateFloat(
		initialValue = 1f,
		targetValue = 0f,
		label = "Alpha transition",
		animationSpec = infiniteRepeatable(
			animation = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
			repeatMode = RepeatMode.Reverse
		)
	)


	Dialog(
		onDismissRequest = onDismissRequest,
		properties = DialogProperties(
			dismissOnBackPress = false,
			dismissOnClickOutside = false
		)
	) {
		Card(
			modifier = modifier,
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.surfaceVariant,
				contentColor = MaterialTheme.colorScheme.onSurfaceVariant
			),
			shape = shape,
		) {
			Column(
				modifier = Modifier
					.padding(dimensionResource(id = R.dimen.loading_dialog_padding)),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Image(
					painter = painterResource(id = R.drawable.ic_scanner),
					contentDescription = null,
					colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
					modifier = Modifier
						.sizeIn(maxHeight = 120.dp, maxWidth = 120.dp)
						.graphicsLayer { alpha = alphaTransition }
				)
				Text(
					text = stringResource(id = R.string.extracting_text),
					style = MaterialTheme.typography.titleLarge
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
fun LoadingContentDialogPreview() = Image2TextReaderTheme {
	LoadingContentDialog(isVisible = true)
}