package com.shaderock.lunch.blpmini.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shaderock.lunch.blpmini.feature.food.FoodScreen
import com.shaderock.lunch.blpmini.feature.login.LoginScreen
import com.shaderock.lunch.blpmini.feature.lunch.LunchScreen
import com.shaderock.lunch.blpmini.feature.network.NetworkViewModel
import com.shaderock.lunch.blpmini.feature.orders.OrdersScreen
import com.shaderock.lunch.blpmini.feature.profile.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    networkViewModel: NetworkViewModel
) {
    NavHost(navController = navController,
        startDestination = Screen.Login.route,
        builder = {
            composable(route = Screen.Login.route) {
                LoginScreen(
                    bottomBarState = bottomBarState,
                    authViewModel = hiltViewModel(),
                    networkViewModel = networkViewModel,
                    navController = navController
                )
            }
            composable(route = Screen.Food.route) {
                FoodScreen()
            }
            composable(route = Screen.Orders.route) {
                OrdersScreen(bottomBarState = bottomBarState)
            }
            composable(route = Screen.Lunch.route) {
                LunchScreen(bottomBarState = bottomBarState)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(bottomBarState = bottomBarState)
            }
        })

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        Screen.Login.route -> {
            bottomBarState.value = false
        }
        else -> {
            bottomBarState.value = true
        }
    }

    val isAuthorized: Boolean? by networkViewModel.isUnauthorized.observeAsState()
    LaunchedEffect(key1 = isAuthorized, block = {
        when (isAuthorized) {
            false -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = false
                    }
                }
            }
            else -> {

            }
        }
    })

}
