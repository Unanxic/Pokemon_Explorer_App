package com.example.pokemonexplorerapp.base.theme.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

const val TransitionAnimationDuration = 300

val fadeInTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        fadeIn(
            animationSpec = tween(
                durationMillis = 300,
            )
        )
    }

val fadeOutTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        fadeOut(
            animationSpec = tween(
                durationMillis = 300,
            )
        )
    }

val slideInHorizontallyTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(TransitionAnimationDuration),
            initialOffset = { it })
    }

val slideOutHorizontallyTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(TransitionAnimationDuration),
            targetOffset = { it })
    }

val slideInOnReEnterHorizontallyTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(TransitionAnimationDuration),
            initialOffset = { -it / 2 })
    }

val slideOutOnReEnterHorizontallyTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(TransitionAnimationDuration),
            targetOffset = { -it / 2 })
    }

val slideInVerticallyTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(TransitionAnimationDuration),
            initialOffset = { it })
    }

val slideOutVerticallyTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(TransitionAnimationDuration),
            targetOffset = { it })
    }