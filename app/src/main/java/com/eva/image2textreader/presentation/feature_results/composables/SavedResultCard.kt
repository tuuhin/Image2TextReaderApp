package com.eva.image2textreader.presentation.feature_results.composables

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
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
	onShare: () -> Unit,
	onDelete: () -> Unit,
	onLongClick: () -> Unit,
	modifier: Modifier = Modifier,
	contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	errorColor: Color = MaterialTheme.colorScheme.onErrorContainer,
	containerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
	selectedContainerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
	shape: Shape = MaterialTheme.shapes.medium,
	density: Density = LocalDensity.current,
) {
	val isActionEnabled by remember(isSelected) {
		derivedStateOf { !isSelected }
	}

	var showDialog by remember { mutableStateOf(false) }


	DeleteResultsDialog(
		isVisible = showDialog,
		title = stringResource(id = R.string.delete_current_confirm_title),
		text = stringResource(id = R.string.delete_current_confirm_text),
		onConfirm = {
			onDelete()
			showDialog = false
		},
		onDisMiss = { showDialog = false },
	)


	val isSelectedTransition = updateTransition(
		targetState = isSelected,
		label = "Is Card Selected"
	)

	val cardContainerColor by isSelectedTransition.animateColor(label = "Card Container Color") {
		if (it) containerColor else selectedContainerColor
	}

	val cardElevation by isSelectedTransition.animateFloat(label = "Card Elevation") {
		with(density) {
			if (it) 8.dp.toPx() else 2.dp.toPx()
		}
	}

	Card(
		colors = CardDefaults.cardColors(
			containerColor = cardContainerColor,
			contentColor = contentColor,
		),
		shape = shape,
		modifier = modifier
			.clip(shape)
			.combinedClickable(onClick = onClick, onLongClick = onLongClick)
			.graphicsLayer {
				shadowElevation = cardElevation
			},
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			verticalAlignment = Alignment.Top,
			modifier = Modifier.padding(all = dimensionResource(id = R.dimen.card_internal_padding)),
		) {
			SelectableCardImage(
				imageUri = model.imageUri,
				isSelected = isSelected,
				modifier = Modifier.weight(.2f)
			)
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
				SavedResultsCardActions(
					isActionEnabled = isActionEnabled,
					onShareAction = onShare,
					onDeleteAction = { showDialog = !showDialog },
					errorColor = errorColor,
					modifier = Modifier.align(Alignment.End)
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
fun SavedResultsCardPreview(
	@PreviewParameter(BooleanPreviewParams::class) isSelected: Boolean
) = Image2TextReaderTheme {
	SavedResultsCard(
		isSelected = isSelected,
		model = PreviewFakes.fakeResultModel,
		modifier = Modifier.fillMaxWidth(),
		onClick = {},
		onShare = {},
		onDelete = {},
		onLongClick = {},
	)
}