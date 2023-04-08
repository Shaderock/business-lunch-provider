package com.shaderock.lunch.blpmini.feature.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shaderock.lunch.blpmini.R
import com.shaderock.lunch.blpmini.feature.network.NetworkViewModel
import com.shaderock.lunch.blpmini.infrastructure.Resource
import com.shaderock.lunch.blpmini.presentation.navigation.Screen

@Composable
fun LoginScreen(
    bottomBarState: MutableState<Boolean>,
    authViewModel: AuthViewModel,
    networkViewModel: NetworkViewModel,
    navController: NavHostController
) {
    bottomBarState.value = false

    val authenticationState by authViewModel.authenticationState.observeAsState()
    val authorizationState by authViewModel.authorizationState.observeAsState()

    val focusManager = LocalFocusManager.current
    val currentLocalContext = LocalContext.current

    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val isLoginBtnEnabled = rememberSaveable { (mutableStateOf(true)) }
    val isProgressIndicatorVisible = rememberSaveable { (mutableStateOf(false)) }

    LaunchedEffect(key1 = authenticationState, block = {
        when (authenticationState) {
            is Resource.Loading -> {
                isLoginBtnEnabled.value = false
                isProgressIndicatorVisible.value = true
            }
            is Resource.Success -> {
                authViewModel.onAuthorize()
            }
            is Resource.Failure -> {
                isProgressIndicatorVisible.value = false
                isLoginBtnEnabled.value = true
                Toast.makeText(currentLocalContext, R.string.login_failed, Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                isProgressIndicatorVisible.value = false
                isLoginBtnEnabled.value = true
            }
        }
    })

    LaunchedEffect(key1 = authorizationState, block = {
        when (authorizationState) {
            is Resource.Loading -> {
                isLoginBtnEnabled.value = false
                isProgressIndicatorVisible.value = true
            }
            is Resource.Success -> {
                isProgressIndicatorVisible.value = false
                Toast.makeText(currentLocalContext, R.string.login_success, Toast.LENGTH_SHORT)
                    .show()
                networkViewModel.onAuthorized()
                navController.navigate(Screen.Food.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = false
                    }
                }
            }
            is Resource.Failure -> {
                isProgressIndicatorVisible.value = false
                isLoginBtnEnabled.value = true
                Toast.makeText(currentLocalContext, R.string.login_failed, Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                isProgressIndicatorVisible.value = false
                isLoginBtnEnabled.value = true
            }
        }
    })

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()

    ) {
        Card(
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.padding(20.dp),
            elevation = 50.dp,
            backgroundColor = MaterialTheme.colors.background
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Login",
                    fontWeight = FontWeight.W700,
                    fontFamily = FontFamily.Cursive,
                    fontSize = 35.sp,
                    modifier = Modifier.padding(10.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    value = email.value,
                    leadingIcon = {
                        Icon(
                            imageVector = (Icons.Filled.Email),
                            contentDescription = "Email icon",
                        )
                    },
                    onValueChange = { email.value = it },
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "example@email.com") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    value = password.value,
                    onValueChange = { password.value = it },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = (Icons.Filled.Lock),
                            contentDescription = "Password icon"
                        )
                    },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "********") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Divider(thickness = 2.dp)

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(20.dp),
                    shape = RoundedCornerShape(30.dp),
                    enabled = isLoginBtnEnabled.value,
                    onClick = {
                        authViewModel.onAuthenticate(
                            email = email.value.text,
                            password = password.value.text
                        )
                    }) {
                    Text(text = "LOGIN")
                }
            }
        }

        AnimatedVisibility(visible = isProgressIndicatorVisible.value) {
            CircularProgressIndicator()
        }
    }

}