package com.shaderock.lunch.blpmini.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun ProfileScreen(bottomBarState: MutableState<Boolean>) {
    bottomBarState.value = true
}