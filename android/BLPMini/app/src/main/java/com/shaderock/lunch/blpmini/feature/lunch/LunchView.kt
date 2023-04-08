package com.shaderock.lunch.blpmini.feature.lunch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun LunchScreen(bottomBarState: MutableState<Boolean>) {
    bottomBarState.value = true
}