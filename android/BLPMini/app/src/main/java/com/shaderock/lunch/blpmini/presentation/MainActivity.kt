package com.shaderock.lunch.blpmini.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shaderock.lunch.blpmini.ui.main.MainScreen
import com.shaderock.lunch.blpmini.ui.theme.BLPMiniTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BLPMiniTheme {
                MainScreen()
            }
        }
    }
}