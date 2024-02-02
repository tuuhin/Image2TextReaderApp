package com.eva.image2textreader.presentation.feature_results.composables

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eva.image2textreader.R
import com.eva.image2textreader.domain.models.ResultsModel
import com.eva.image2textreader.presentation.util.preview.BooleanPreviewParams
import com.eva.image2textreader.presentation.util.preview.PreviewFakes
import com.eva.image2textreader.ui.theme.Image2TextReaderTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedResultsCard(
	isSelected: Boolean,
	model: ResultsModel,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	onLongClick: () -> Unit = {},
	context: Context = LocalContext.current,
	selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
	containerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
	contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	shape: Shape = MaterialTheme.shapes.medium,
	errorColor: Color = MaterialTheme.colorScheme.onErrorContainer,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) selectedContainerColor else containerColor,
			contentColor = contentColor,
		),
		shape = shape,
		elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
		modifier = modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick),
	) {
		Row(
			modifier = Modifier
				.padding(all = dimensionResource(id = R.dimen.card_internal_padding)),
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier.weight(.2f),
				contentAlignment = Alignment.Center
			) {
				model.imageUri?.let { uri ->
					AsyncImage(
						model = ImageRequest.Builder(context)
							.data(uri)
							.crossfade(true)
							.build(),
						contentDescription = null,
						placeholder = BrushPainter(brush = Brush.horizontalGradient()),
						fallback = painterResource(id = R.drawable.ic_image_secondary),
						contentScale = ContentScale.Crop,
						modifier = Modifier
							.align(Alignment.Center),
					)
				} ?: run {
					Image(
						painter = painterResource(id = R.drawable.ic_image_secondary),
						contentDescription = null,
						contentScale = ContentScale.Fit,
						modifier = Modifier
							.align(Alignment.Center),
						colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
					)
				}
			}
			Column(
				modifier = Modifier.weight(.8f),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Text(
					text = model.text,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyLarge
				)
				Row(
					modifier = Modifier.align(Alignment.End)
				) {
					TextButton(
						onClick = {},
						colors = ButtonDefaults
							.textButtonColors(contentColor = contentColor),
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_share),
							contentDescription = stringResource(id = R.string.share_icon_desc),
						)
						Spacer(modifier = Modifier.width(4.dp))
						Text(
							text = stringResource(id = R.string.share_icon_text),
							style = MaterialTheme.typography.bodySmall
						)
					}
					TextButton(
						onClick = {},
						colors = ButtonDefaults
							.textButtonColors(contentColor = errorColor)
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_delete),
							contentDescription = stringResource(id = R.string.delete_icon_desc),
						)
						Spacer(modifier = Modifier.width(2.dp))
						Text(
							text = stringResource(id = R.string.delete_icon_text),
							style = MaterialTheme.typography.bodySmall
						)
					}
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
fun SavedResultsCardPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isSelected: Boolean
) = Image2TextReaderTheme {
	SavedResultsCard(isSelected = isSelected,
		model = PreviewFakes.fakeResultModel,
		modifier = Modifier.fillMaxWidth(),
		onClick = {}
	)
}