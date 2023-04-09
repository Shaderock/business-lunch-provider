package com.shaderock.lunch.blpmini.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shaderock.lunch.blpmini.R

@Composable
fun ProfileScreen() {
    ProfileScreenContent()
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreenContent()
}

@Composable
fun ProfileScreenContent() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                content = {
                    Row(modifier = Modifier.fillMaxWidth(),
                        content = {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.large,
                                elevation = 15.dp,
                                content = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        content =
                                        {
                                            Spacer(modifier = Modifier.padding(15.dp))
                                            Icon(
                                                imageVector = Icons.Filled.AccountBox,
                                                contentDescription = stringResource(R.string.profile_image),
                                                modifier = Modifier.size(120.dp)
                                            )
                                            Text(
                                                text = "Firstname Lastname",
                                                style = MaterialTheme.typography.h5
                                            )
                                            Divider(modifier = Modifier.width(80.dp))
                                            Text(
                                                text = "email@dummy.com",
                                                style = MaterialTheme.typography.subtitle2
                                            )
                                            Spacer(modifier = Modifier.padding(15.dp))
                                        })
                                })
                        })

                    Divider(modifier = Modifier.padding(20.dp))

                    NotificationSettingsItem(stringResource(R.string.notifications_amount), "4")

                    Spacer(modifier = Modifier.padding(5.dp))

                    NotificationSettingsItem(stringResource(R.string.notifications_interval), "15m")
                    Spacer(modifier = Modifier.padding(5.dp))

                    NotificationSettingsItem(stringResource(R.string.notifications_start), "9am")

                    Divider(modifier = Modifier.padding(20.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = { /*TODO*/ }, content = {
                            Text(
                                text = stringResource(R.string.logout),
                                style = MaterialTheme.typography.subtitle1
                            )
                        })
                })

            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                content = {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(R.string.edit_profile)
                    )
                }
            )
        })
}

@Composable
fun NotificationSettingsItem(name: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {
            Column(
                horizontalAlignment = Alignment.Start,
                content = {
                    Card(
                        shape = MaterialTheme.shapes.small,
                        elevation = 3.dp,
                        content = {
                            Text(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .width(200.dp),
                                text = name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h6
                            )
                        }
                    )
                })
            Column(
                horizontalAlignment = Alignment.End,
                content = {
                    Card(
                        shape = CircleShape,
                        contentColor = MaterialTheme.colors.secondary,
                        content = {
                            Text(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .width(75.dp),
                                text = value,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h6
                            )
                        })
                })
        })
}

