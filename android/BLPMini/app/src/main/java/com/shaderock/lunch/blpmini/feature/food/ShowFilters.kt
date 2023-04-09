package com.shaderock.lunch.blpmini.feature.food

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShowFilters(
) {
    ShowFiltersContent()
}

@Composable
@Preview
fun ShowFiltersPreview() {
    ShowFiltersContent()
}

@Composable
fun ShowFiltersContent() {
    val subscriptionSelection = remember { mutableStateOf("") }
    val favoriteSelection = remember { mutableStateOf("") }
    val priceSelection = remember { mutableStateOf("") }

    Column {
        RadioButtonGroup(
            title = "Subscriptions:",
            options = listOf("Any", "Only subscribed", "Only not subscribed"),
            onSelectionChange = { subscriptionSelection.value = it },
            selectedOption = subscriptionSelection.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        RadioButtonGroup(
            title = "Favorites:",
            options = listOf("Any", "Only favorite", "Only not favorite"),
            onSelectionChange = { favoriteSelection.value = it },
            selectedOption = favoriteSelection.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        RadioButtonGroup(
            title = "Prices:",
            options = listOf("Any", "By Option", "By Category"),
            onSelectionChange = { priceSelection.value = it },
            selectedOption = priceSelection.value
        )
    }
}

@Composable
fun RadioButtonGroup(
    title: String,
    options: List<String>,
    onSelectionChange: (String) -> Unit,
    selectedOption: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column(verticalArrangement = Arrangement.SpaceBetween,
                content = {
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 25.dp),
                        style = MaterialTheme.typography.subtitle2
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    )
                    options.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = option == selectedOption,
                                onClick = { onSelectionChange(option) }
                            )
                            Text(text = option, textAlign = TextAlign.Center)
                        }
                    }
                })
        })
}

//@Composable
//fun ShowFiltersContent() {
//    val foodCategoryLabels = listOf("Pizza", "Salad", "Soup", "Pasta")
//    var expanded = remember { mutableStateOf(false) }
//
//    val subscriptionSelection = remember { mutableStateOf("") }
//    val favoriteSelection = remember { mutableStateOf("") }
//    val priceSelection = remember { mutableStateOf("") }
//
//    Column {
//        Text("Subscriptions:")
//        RadioButtonGroup(
//            options = listOf("Any", "Yes", "No"),
//            onSelectionChange = { subscriptionSelection.value = it },
//            selectedOption = subscriptionSelection.value
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Favorites:")
//        RadioButtonGroup(
//            options = listOf("Any", "Yes", "No"),
//            onSelectionChange = { favoriteSelection.value = it },
//            selectedOption = favoriteSelection.value
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Prices:")
//        RadioButtonGroup(
//            options = listOf("Any", "By Option", "By Category"),
//            onSelectionChange = { priceSelection.value = it },
//            selectedOption = priceSelection.value
//        )
//    }
//}
//
//@Composable
//fun RadioButtonGroup(
//    options: List<String>,
//    onSelectionChange: (String) -> Unit,
//    selectedOption: String
//) {
//    options.forEach { option ->
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(
//                selected = option == selectedOption,
//                onClick = { onSelectionChange(option) }
//            )
//            Text(text = option, textAlign = TextAlign.Center)
//        }
//    }
//}



