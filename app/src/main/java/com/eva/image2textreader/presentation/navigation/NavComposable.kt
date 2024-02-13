package com.eva.image2textreader.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private typealias NavEnterTransition = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?

private typealias NavExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?

fun NavGraphBuilder.composableWithAnimation(
	route: String,
	navArguments: List<NamedNavArgument> = emptyList(),
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {

	val enterTransition: NavEnterTransition = {
		slideIntoContainer(
			towards = AnimatedContentTransitionScope.SlideDirection.Left,
			animationSpec = tween(
				durationMillis = 600,
				delayMillis = 100,
				easing = FastOutSlowInEasing
			)
		) + fadeIn(
			initialAlpha = .5f,
			animationSpec = tween(durationMillis = 600, delayMillis = 100)
		)
	}

	val exitTransition: NavExitTransition = {
		slideOutOfContainer(
			towards = AnimatedContentTransitionScope.SlideDirection.Left,
			animationSpec = tween(
				durationMillis = 600,
				delayMillis = 100,
				easing = FastOutSlowInEasing
			),
		) + fadeOut(
			animationSpec = tween(durationMillis = 600, delayMillis = 100),
		)
	}

	composable(
		route = route,
		arguments = navArguments,
		deepLinks = deepLinks,
		content = content,
		popEnterTransition = enterTransition,
		popExitTransition = exitTransition,
		enterTransition = enterTransition,
		exitTransition = exitTransition
	)
}