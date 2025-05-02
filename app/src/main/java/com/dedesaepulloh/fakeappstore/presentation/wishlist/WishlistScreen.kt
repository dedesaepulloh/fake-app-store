package com.dedesaepulloh.fakeappstore.presentation.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dedesaepulloh.fakeappstore.domain.model.Product
import com.dedesaepulloh.fakeappstore.presentation.components.ProductCard

@Composable
fun WishlistScreen(
    modifier: Modifier = Modifier,
    onItemClick: (Product) -> Unit = {},
    onOrderNow: (Product) -> Unit = {},
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val wishlist by viewModel.wishlist.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWishlist()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Wishlist",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(wishlist) { product ->
                ProductCard(
                    product = product,
                    onItemClick = { onItemClick(product) },
                    onOrderNow = { onOrderNow(product) }
                )
            }
        }
    }
}
