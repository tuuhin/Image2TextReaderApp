package com.eva.image2textreader.presentation.feature_recognizer.composables

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.image2textreader.R

@Composable
fun ContentWithImagePreview(
	modifier: Modifier = Modifier,
	borderWidth: Dp = 2.dp,
	imageUri: String? = null,
	context: Context = LocalContext.current,
	borderColor: Color = MaterialTheme.colorScheme.primary,
	previewShape: Shape = MaterialTheme.shapes.medium,
	content: @Composable () -> Unit = {},
) {
	Box(
		modifier = modifier,
	) {
		content()
		Box(
			modifier = Modifier
				.padding(
					bottom = dimensionResource(id = R.dimen.image_preview_padding),
					end = dimensionResource(id = R.dimen.image_preview_padding)
				)
				.size(size = dimensionResource(id = R.dimen.image_preview_size))
				.aspectRatio(.8f)
				.clip(shape = MaterialTheme.shapes.medium)
				.border(
					width = borderWidth,
					color = borderColor,
					shape = previewShape
				)
				.graphicsLayer {
					spotShadowColor = borderColor
					shadowElevation = 10f
				}
				.align(Alignment.BottomEnd),
			contentAlignment = Alignment.Center
		) {
			imageUri?.let { uri ->
				AsyncImage(
					model = ImageRequest.Builder(context)
						.data(uri)
						.crossfade(true)
						.build(),
					contentDescription = stringResource(R.string.image_for_content_uri),
					imageLoader = context.imageLoader,
					contentScale = ContentScale.Crop,
					filterQuality = FilterQuality.Low,
				)
			}
		}
	}
}

