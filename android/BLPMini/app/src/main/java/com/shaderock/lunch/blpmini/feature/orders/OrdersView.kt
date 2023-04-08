package com.shaderock.lunch.blpmini.feature.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun OrdersScreen(bottomBarState: MutableState<Boolean>) {
    bottomBarState.value = true
}