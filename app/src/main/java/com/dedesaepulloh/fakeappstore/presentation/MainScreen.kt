package com.dedesaepulloh.fakeappstore.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dedesaepulloh.fakeappstore.presentation.cart.CartViewModel
import com.dedesaepulloh.fakeappstore.presentation.components.BottomBar
import com.dedesaepulloh.fakeappstore.presentation.components.BottomSheetWrapper
import com.dedesaepulloh.fakeappstore.presentation.components.DetailTopBar
import com.dedesaepulloh.fakeappstore.presentation.components.FakeTopBar
import com.dedesaepulloh.fakeappstore.presentation.components.GeneralTopBar
import com.dedesaepulloh.fakeappstore.presentation.navigation.AppNavGraph
import com.dedesaepulloh.fakeappstore.presentation.navigation.Screen
import com.dedesaepulloh.fakeappstore.presentation.profile.ProfileScreen

@Composable
fun MainScreen(isLoggedIn: Boolean) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showBottomSheet by remember { mutableStateOf(false) }

    val cartViewModel = hiltViewModel<CartViewModel>()
    val cartCount by cartViewModel.totalCart.collectAsState()

    LaunchedEffect(Unit) {
        cartViewModel.getTotalCart()
    }

    Scaffold(
        bottomBar = {
            when (currentRoute) {
                Screen.Login.route, Screen.Register.route, Screen.ProductDetail.route, Screen.Cart.route, Screen.OrderDetail.route, Screen.ProductCategory.route -> {}
                else -> {
                    BottomBar(navController = navController, cartCount)
                }
            }
        },
        topBar = {
            when (currentRoute) {
                Screen.Login.route, Screen.Register.route -> {
                }

                Screen.Home.route, Screen.Category.route, Screen.Wishlist.route -> {
                    FakeTopBar(
                        title = "",
                        onSearchClick = {

                        },
                        onNotificationClick = {

                        },
                        onProfileClick = {
                            showBottomSheet = true
                        })
                }

                Screen.ProductDetail.route -> {
                    DetailTopBar(
                        onBackClick = { navController.navigateUp() },
                        onCartClick = {
                            navController.navigate(Screen.Cart.route)
                        })
                }

                else -> {
                    GeneralTopBar {
                        navController.navigateUp()
                    }
                }
            }
        }
    ) { padding ->
        AppNavGraph(padding, navController = navController, cartViewModel = cartViewModel, isLoggedIn = isLoggedIn)
    }

    BottomSheetWrapper(
        showSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false }
    ) {
        ProfileScreen(
            onLogoutSuccess = {
                showBottomSheet = false
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }

}