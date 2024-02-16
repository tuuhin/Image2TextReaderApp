package com.eva.image2textreader.presentation.feature_results.composables

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import com.eva.image2textreader.R
import com.eva.image2textreader.presentation.util.preview.BooleanPreviewParams
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@Composable
fun SelectableCardImage(
	imageUri: String?,
	isSelected: Boolean,
	modifier: Modifier = Modifier,
	context: Context = LocalContext.current,
	selectedColor: Color = MaterialTheme.colorScheme.primaryContainer,
	onSelectedColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
	val transition = updateTransition(targetState = isSelected, label = "Flip Transition")

	val rotateYAnimation by transition.animateFloat(
		label = "Rotate Y Animation",
		targetValueByState = { selected -> if (selected) 0f else 180f },
		transitionSpec = { tween(durationMillis = 400, easing = FastOutSlowInEasing) },
	)

	Box(
		modifier = modifier
			.sizeIn(
				minHeight = dimensionResource(id = R.dimen.card_image_min_size),
				maxHeight = dimensionResource(id = R.dimen.card_image_max_size),
			)
			.aspectRatio(1f)
			.graphicsLayer { rotationY = rotateYAnimation },
		contentAlignment = Alignment.Center
	) {
		if (rotateYAnimation >= 90f) {
			imageUri?.let { uri ->
				AsyncImage(
					model = uri,
					contentDescription = null,
					imageLoader = context.imageLoader,
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.clip(MaterialTheme.shapes.medium)
						.align(Alignment.Center),
				)
			} ?: run {
				Image(
					painter = painterResource(id = R.drawable.ic_image_secondary),
					contentDescription = null,
					contentScale = ContentScale.Fit,
					colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
					modifier = Modifier.align(Alignment.Center),
				)
			}
		} else {
			Box(
				modifier = Modifier
					.aspectRatio(1f)
					.background(color = selectedColor, shape = CircleShape),
				contentAlignment = Alignment.Center
			) {
				Icon(
					imageVector = Icons.Rounded.Check,
					contentDescription = null,
					tint = onSelectedColor,
					modifier = Modifier.size(32.dp)
				)
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun SelectableCardImagePreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isSelected: Boolean
) = Image2TextReaderTheme {
	SelectableCardImage(
		imageUri = null,
		isSelected = isSelected,
		modifier = Modifier.size(120.dp)
	)
}