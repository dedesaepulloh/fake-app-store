package com.dedesaepulloh.fakeappstore.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dedesaepulloh.fakeappstore.presentation.components.LoadingScreen
import com.dedesaepulloh.fakeappstore.presentation.register.state.RegisterState

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val registerState by viewModel.registerState.collectAsState()
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val address by viewModel.address.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val addressError by viewModel.addressError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisibility by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val onRegisterClick = {
        viewModel.register(name, email, address, password, confirmPassword)
    }

    when (val state = registerState) {
        is RegisterState.Loading -> {
            isLoading = true
        }

        is RegisterState.Success -> {
            isLoading = false
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
            viewModel.resetState()
        }

        is RegisterState.Error -> {
            isLoading = false
            Toast.makeText(
                context,
                state.message ?: "Unknown error",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.resetState()
        }

        else -> Unit
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Full Name") },
                    isError = nameError != null,
                    supportingText = {
                        if (nameError != null) Text(
                            nameError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Email") },
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) Text(
                            emailError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = address,
                    onValueChange = viewModel::onAddressChange,
                    label = { Text("Address") },
                    isError = addressError != null,
                    supportingText = {
                        if (addressError != null) Text(
                            addressError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Password") },
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) Text(
                            passwordError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = { Text("Confirm Password") },
                    isError = confirmPasswordError != null,
                    supportingText = {
                        if (confirmPasswordError != null) Text(
                            confirmPasswordError!!, color = MaterialTheme.colorScheme.error
                        )
                    },
                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisibility = !confirmPasswordVisibility
                        }) {
                            Icon(
                                imageVector = if (confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle confirm password visibility"
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) onRegisterClick()
                    }),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onRegisterClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isFormValid
                ) {
                    Text("Register")
                }
            }
        }

        if (isLoading) {
            LoadingScreen()
        }
    }
}
