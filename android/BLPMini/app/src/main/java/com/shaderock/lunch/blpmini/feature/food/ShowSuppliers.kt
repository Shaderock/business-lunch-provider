package com.shaderock.lunch.blpmini.feature.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shaderock.lunch.blpmini.R

@Composable
fun ShowSuppliers() {
    val suppliers: List<Supplier> = listOf(
        Supplier(
            supplierName = "Oliva",
            subscriptionEnabled = true,
            foodCategories = listOf("Pizza", "Pasta", "Salads", "Soup"),
            logo = painterResource(id = R.drawable.oliva)
        ),
        Supplier(
            supplierName = "Bistro",
            subscriptionEnabled = true,
            foodCategories = listOf("Pasta", "Salads", "Meat", "Kebab"),
            logo = painterResource(id = R.drawable.bistro)
        ),
        Supplier(
            supplierName = "Granier",
            subscriptionEnabled = false,
            foodCategories = listOf("Pastry", "Deserts"),
            logo = painterResource(
                id = R.drawable.granier
            )
        )
    )

    LazyColumn {
        items(suppliers.size) { index ->
            val supplier = suppliers[index]
            ShowSupplier(
                supplierName = supplier.supplierName,
                subscriptionEnabled = supplier.subscriptionEnabled,
                foodCategories = supplier.foodCategories,
                logo = supplier.logo
            )
        }
    }
}

@Composable
fun ShowSupplier(
    supplierName: String,
    subscriptionEnabled: Boolean,
    foodCategories: List<String>,
    logo: Painter
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(250.dp),
        elevation = 4.dp,
        content =
        {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content =
                        {
                            Text(
                                text = supplierName,
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (subscriptionEnabled) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Subscription enabled",
                                    tint = MaterialTheme.colors.secondary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Subscription disabled",
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(content = {
                        Text(
                            text = foodCategories.joinToString(", "),
                            style = MaterialTheme.typography.body2
                        )
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = logo,
                        contentDescription = "Supplier logo",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                })
        })
}