package com.shaderock.lunch.blpmini.feature.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties

@Composable
fun MonitorNetwork() {
    val connectivityManager =
        LocalContext.current.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isNetworkAvailable = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isNetworkAvailable.value = true
            }

            override fun onLost(network: Network) {
                isNetworkAvailable.value = false
            }
        }
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            networkCallback
        )
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    if (!isNetworkAvailable.value) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "No network connection") },
            text = {
                Text(
                    text = "Please check your internet connection and try again. " +
                            "Network connection will be detected automatically"
                )
            },
            shape = MaterialTheme.shapes.medium,
            buttons = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        )
    }
}