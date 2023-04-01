package com.shaderock.lunch.blpmini.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart

object Navigation {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Order",
            icon = Icons.Filled.Home,
            route = "order"
        ),
        BottomNavItem(
            label = "Cart",
            icon = Icons.Filled.ShoppingCart,
            route = "cart"
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}