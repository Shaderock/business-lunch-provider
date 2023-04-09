package com.shaderock.lunch.blpmini.presentation.navigation

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.shaderock.lunch.blpmini.R

@Composable
fun SetupTopAppBar(
    navController: NavHostController
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search)
                )
            }
            IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = stringResource(id = R.string.notifications)
                )
            }
        })
}