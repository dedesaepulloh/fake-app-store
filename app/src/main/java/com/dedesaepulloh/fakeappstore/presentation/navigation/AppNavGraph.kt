package com.dedesaepulloh.fakeappstore.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dedesaepulloh.fakeappstore.presentation.cart.CartScreen
import com.dedesaepulloh.fakeappstore.presentation.cart.CartViewModel
import com.dedesaepulloh.fakeappstore.presentation.category.CategoryScreen
import com.dedesaepulloh.fakeappstore.presentation.category.ProductCategoryScreen
import com.dedesaepulloh.fakeappstore.presentation.detail.OrderDetailScreen
import com.dedesaepulloh.fakeappstore.presentation.detail.ProductDetailScreen
import com.dedesaepulloh.fakeappstore.presentation.home.HomeScreen
import com.dedesaepulloh.fakeappstore.presentation.login.LoginScreen
import com.dedesaepulloh.fakeappstore.presentation.register.RegisterScreen
import com.dedesaepulloh.fakeappstore.presentation.wishlist.WishlistScreen

@Composable
fun AppNavGraph(
    padding: PaddingValues,
    navController: NavHostController,
    cartViewModel: CartViewModel,
    isLoggedIn: Boolean
) {
    NavHost(
        navController,
        startDestination = if (!isLoggedIn) Screen.Login.route else Screen.Home.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                modifier = Modifier.padding(padding),
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {

                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                modifier = Modifier.padding(padding),
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = Modifier.padding(padding),
                onItemClick = { product ->
                    navController.navigate(Screen.ProductDetail.create(product.id))
                },
                onOrderNow = {
                    cartViewModel.setCheckoutProducts(listOf(it))
                    navController.navigate(Screen.OrderDetail.route)
                }
            )
        }
        composable(Screen.Category.route) {
            CategoryScreen(
                modifier = Modifier.padding(padding),
                onCategoryClick = {
                    navController.navigate(Screen.ProductCategory.create(it))
                }
            )
        }
        composable(Screen.Cart.route) {
            CartScreen(
                modifier = Modifier.padding(padding),
                viewModel = cartViewModel,
                onCheckoutClick = {
                    cartViewModel.setCheckoutProducts(it)
                    navController.navigate(Screen.OrderDetail.route)
                }
            )
        }
        composable(Screen.Wishlist.route) { WishlistScreen(modifier = Modifier.padding(padding)) }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            ProductDetailScreen(
                modifier = Modifier.padding(padding),
                id = id,
                cartViewModel = cartViewModel,
                onOrderNow = {
                    navController.navigate(Screen.OrderDetail.route)
                }
            )
        }
        composable(Screen.OrderDetail.route) {
            OrderDetailScreen(
                modifier = Modifier.padding(padding),
                onPayClick = {

                },
                cartViewModel = cartViewModel
            )
        }

        composable(
            route = Screen.ProductCategory.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductCategoryScreen(
                modifier = Modifier.padding(padding),
                category = category,
                onOrderNow = {
                    navController.navigate(Screen.OrderDetail.route)
                },
                onItemClick = {
                    navController.navigate(Screen.ProductDetail.create(it.id))
                }
            )
        }

    }
}
