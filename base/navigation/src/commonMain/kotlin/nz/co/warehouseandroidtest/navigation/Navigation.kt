package nz.co.warehouseandroidtest.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startingDestination: Any,
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        { slideInHorizontally { it } },
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        { slideOutHorizontally { -it } },
    popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        { slideInHorizontally { -it } },
    popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        { slideOutHorizontally { it } },
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startingDestination,
        builder = builder,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition
    )
}
