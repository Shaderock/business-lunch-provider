package com.shaderock.lunch.blpmini.presentation.navigation

sealed class Screen(val route: String) {
    object Food : Screen(route = "food_screen")
    object Orders : Screen(route = "orders_screen")
    object Lunch : Screen(route = "lunch_screen")
    object Profile : Screen(route = "profile_screen")
    object Login : Screen(route = "login_screen")
    object Notifications : Screen(route = "notifications_screen")
}
