package com.shaderock.lunch.blpmini.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.shaderock.lunch.blpmini.presentation.navigation.AppNavigation
import com.shaderock.lunch.blpmini.presentation.navigation.BottomNavigationBar

@Composable
internal fun MainScreen() {
    val bottomBarState = remember { (mutableStateOf(false)) }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavigationBar(navController = navController)
            }
        },
        content = { padding ->
            BoxWithConstraints(modifier = Modifier.padding(padding)) {
                AppNavigation(
                    navController = navController,
                    bottomBarState = bottomBarState,
                    networkViewModel = hiltViewModel()
                )
            }
        })
}