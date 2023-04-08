package com.shaderock.lunch.blpmini.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.shaderock.lunch.blpmini.R

sealed class BottomNavigationItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val route: String
) {
    object Food : BottomNavigationItem(
        label = R.string.food,
        icon = Icons.Filled.Home,
        route = Screen.Food.route
    )

    object Orders : BottomNavigationItem(
        label = R.string.orders,
        icon = Icons.Filled.ShoppingCart,
        route = Screen.Orders.route
    )

    object Lunch : BottomNavigationItem(
        label = R.string.lunch,
        icon = Icons.Filled.MailOutline,
        route = Screen.Lunch.route
    )

    object Profile : BottomNavigationItem(
        label = R.string.profile,
        icon = Icons.Filled.Person,
        route = Screen.Profile.route
    )
}
