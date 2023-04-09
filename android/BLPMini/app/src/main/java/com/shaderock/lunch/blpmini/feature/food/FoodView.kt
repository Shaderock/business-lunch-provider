package com.shaderock.lunch.blpmini.feature.food

import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaderock.lunch.blpmini.R

@Composable
fun FoodScreen() {
    FoodScreenContent()
}

@Composable
@Preview
fun FoodScreenPreview() {
    FoodScreenContent()
}

@Composable
fun FoodScreenContent() {
    val selectedTabIndex = remember { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.SpaceBetween,
        content = {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                tabs = {
                    Tab(text = { Text(text = stringResource(R.string.suppliersTab)) },
                        selected = selectedTabIndex.value == 0,
                        onClick = { selectedTabIndex.value = 0 })
                    Tab(text = { Text(text = stringResource(R.string.filtersTab)) },
                        selected = selectedTabIndex.value == 1,
                        onClick = { selectedTabIndex.value = 1 })
                }
            )
            Spacer(modifier = Modifier.size(10.dp))

            Column(modifier = Modifier.padding(10.dp)) {
                when (selectedTabIndex.value) {
                    0 -> {
                        ShowSuppliers()
                    }
                    1 -> {
                        ShowFilters()
                    }
                }
            }
        })
}

internal data class Supplier(
    val supplierName: String,
    val subscriptionEnabled: Boolean,
    val foodCategories: List<String>,
    val logo: Painter
)