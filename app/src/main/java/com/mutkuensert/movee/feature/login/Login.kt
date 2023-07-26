package com.mutkuensert.movee.feature.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mutkuensert.movee.R

@Composable
fun Login(viewModel: LoginViewModel = hiltViewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val loginEvent by viewModel.loginEvent.collectAsStateWithLifecycle(null)

    if (isLoggedIn != null) {
        if (!isLoggedIn!!) {
            LoggedOutScreen(login = viewModel::login)
        } else {
            LoggedInScreen(logout = viewModel::logout)
        }
    }

    val context = LocalContext.current

    LaunchedEffect(loginEvent) {
        if (loginEvent == LoginEvent.Login) {
            Toast.makeText(context, "Logged in successfully.", Toast.LENGTH_SHORT).show()
        } else if (loginEvent == LoginEvent.Logout) {
            Toast.makeText(context, "Logged out successfully.", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun LoggedInScreen(logout: () -> Unit) {
    IconButton(onClick = logout) {
        Icon(painter = painterResource(R.drawable.ic_logout), contentDescription = null)
    }
}

@Composable
private fun LoggedOutScreen(login: (user: String, password: String) -> Unit) {
    val (user, onUserChange) = remember { mutableStateOf("") }
    val (password, onPasswordChange) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = user, onValueChange = onUserChange)

        Spacer(Modifier.height(10.dp))

        PasswordTextField(value = password, onValueChange = onPasswordChange)

        Spacer(Modifier.height(10.dp))

        IconButton(onClick = { login.invoke(user, password) }) {
            Icon(painter = painterResource(R.drawable.ic_login), contentDescription = null)
        }
    }
}

@Composable
private fun PasswordTextField(
    value: String,
    modifier: Modifier = Modifier,
    labelText: String = "",
    hasError: Boolean = false,
    onValueChange: (text: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = labelText,
                color = Color.White,
                fontSize = 16.sp
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        isError = hasError,
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val (icon, iconColor) = if (showPassword.value) {
                Pair(
                    R.drawable.ic_visibility_on,
                    Color.Gray
                )
            } else {
                Pair(R.drawable.ic_visibility_off, Color.Gray)
            }

            IconButton(onClick = { showPassword.value = !showPassword.value }) {
                Icon(
                    painterResource(icon),
                    contentDescription = "Visibility",
                    tint = iconColor
                )
            }
        }/*,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            textColor = Color.White,
            cursorColor = Color.White,
        )*/
    )
}