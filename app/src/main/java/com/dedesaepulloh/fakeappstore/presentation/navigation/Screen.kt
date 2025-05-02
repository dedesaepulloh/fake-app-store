package com.dedesaepulloh.fakeappstore.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Category : Screen("category", "Category", Icons.AutoMirrored.Filled.List)
    data object Cart : Screen("cart", "Cart", Icons.Default.ShoppingCart)
    data object Wishlist : Screen("wishlist", "Wishlist", Icons.Default.Favorite)
    data object Profile : Screen("profile", "Profile", Icons.Default.Person)
    data object Login : Screen("login", "Login", Icons.Default.Person)
    data object Register : Screen("register", "Register", Icons.Default.Person)
    data object ProductDetail : Screen("detail/{id}", "Product Detail", Icons.Default.Person) {
        fun create(id: Int) = "detail/$id"
    }
    data object OrderDetail : Screen("order", "Order Detail", Icons.Default.Reorder)
    data object ProductCategory :
        Screen("product/{category}", "Product Category", Icons.AutoMirrored.Filled.List) {
        fun create(category: String) = "product/$category"
    }
}

