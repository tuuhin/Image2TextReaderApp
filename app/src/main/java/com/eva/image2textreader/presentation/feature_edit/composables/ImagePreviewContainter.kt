package com.eva.image2textreader.presentation.feature_edit.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ImagePreviewContainer(
	modifier: Modifier = Modifier,
	onPreviewClick: () -> Unit,
	imageContent: @Composable BoxScope.() -> Unit,
) {
	Box(
		modifier = modifier
			.border(
				2.dp,
				MaterialTheme.colorScheme.secondary,
				shape = MaterialTheme.shapes.medium
			)
			.clip(MaterialTheme.shapes.medium)
	) {
		imageContent()
		SuggestionChip(
			onClick = onPreviewClick,
			label = { Text(text = "Preview") },
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.offset((-20).dp, (-10).dp),
		)

	}
}