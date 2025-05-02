package com.dedesaepulloh.fakeappstore.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dedesaepulloh.fakeappstore.presentation.profile.ProfileViewModel
import com.dedesaepulloh.fakeappstore.presentation.theme.FakeAppStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FakeAppStoreTheme {
                val profileViewModel = hiltViewModel<ProfileViewModel>()
                val isLoggedIn by profileViewModel.isLoginState.collectAsState()
                LaunchedEffect(Unit) {
                    profileViewModel.isLoggedIn()
                }
                MainScreen(isLoggedIn)
            }
        }
    }
}