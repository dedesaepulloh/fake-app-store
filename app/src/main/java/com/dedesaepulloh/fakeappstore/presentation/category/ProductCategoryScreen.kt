package com.dedesaepulloh.fakeappstore.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.dedesaepulloh.fakeappstore.presentation.category.state.ProductCategoryState
import com.dedesaepulloh.fakeappstore.presentation.components.ErrorScreen
import com.dedesaepulloh.fakeappstore.presentation.components.LoadingScreen
import com.dedesaepulloh.fakeappstore.presentation.components.ProductCard

@Composable
fun ProductCategoryScreen(
    modifier: Modifier = Modifier,
    category: String,
    onItemClick: (Product) -> Unit = {},
    onOrderNow: (Product) -> Unit = {},
    viewModel: CategoryViewModel = hiltViewModel()
) {

    val state by viewModel.productCategoryState.collectAsState()

    LaunchedEffect(category) {
        viewModel.getProductCategory(category)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val result = state) {
            is ProductCategoryState.Loading -> {
                LoadingScreen()
            }

            is ProductCategoryState.Success -> {
                ProductContent(result.data, category, onItemClick, onOrderNow)
            }

            is ProductCategoryState.Error -> {
                ErrorScreen(message = result.message, onRetry = {
                    viewModel.getProductCategory(category)
                })
            }

            else -> {
                // Nothing
            }
        }
    }

}

@Composable
fun ProductContent(
    data: List<Product>,
    category: String,
    onItemClick: (Product) -> Unit,
    onOrderNow: (Product) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Category - $category",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(data) { product ->
                ProductCard(product = product,
                    onItemClick = { onItemClick(product) },
                    onOrderNow = { onOrderNow(product) })
            }
        }
    }
}
