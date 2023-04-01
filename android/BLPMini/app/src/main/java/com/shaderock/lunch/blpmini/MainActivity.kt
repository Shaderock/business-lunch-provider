package com.shaderock.lunch.blpmini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shaderock.lunch.blpmini.navigation.Navigation
import com.shaderock.lunch.blpmini.ui.components.HomeScreen
import com.shaderock.lunch.blpmini.ui.components.ProfileScreen
import com.shaderock.lunch.blpmini.ui.components.SearchScreen
import com.shaderock.lunch.blpmini.ui.theme.BLPMiniTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BLPMiniTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }, content = { padding ->
                        NavHostContainer(navController = navController, padding = padding)
                    })
                }
            }
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController, padding: PaddingValues
) {
    NavHost(navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            composable("home") {
                HomeScreen()
            }
            composable("search") {
                SearchScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
        })
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Navigation.BottomNavItems.forEach { navItem ->
            BottomNavigationItem(selected = currentRoute == navItem.route, onClick = {
                navController.navigate(navItem.route)
            }, icon = {
                Icon(imageVector = navItem.icon, contentDescription = navItem.label)
            }, label = {
                Text(text = navItem.label)
            }, alwaysShowLabel = false
            )
        }
    }
}