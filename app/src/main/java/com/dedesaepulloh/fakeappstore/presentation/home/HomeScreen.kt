package com.dedesaepulloh.fakeappstore.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dedesaepulloh.fakeappstore.domain.model.Product
import com.dedesaepulloh.fakeappstore.presentation.components.ErrorScreen
import com.dedesaepulloh.fakeappstore.presentation.components.LoadingScreen
import com.dedesaepulloh.fakeappstore.presentation.components.ProductCard
import com.dedesaepulloh.fakeappstore.presentation.home.state.ProductState
import com.dedesaepulloh.fakeappstore.presentation.profile.ProfileViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onItemClick: (Product) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    onOrderNow: (Product) -> Unit = {}
) {
    val productState by viewModel.productState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }

    Column(modifier = modifier.fillMaxSize()) {

        when (productState) {
            is ProductState.Loading -> {
                LoadingScreen()
            }

            is ProductState.Error -> {
                ErrorScreen(
                    message = (productState as ProductState.Error).message,
                    onRetry = { viewModel.getAllProducts() }
                )
            }

            is ProductState.Success -> {
                val products = (productState as ProductState.Success).products
                InitContent(products, onItemClick, onOrderNow)
            }

            else -> Unit
        }
    }
}

@Composable
fun InitContent(
    products: List<Product>,
    onItemClick: (Product) -> Unit,
    onOrderNow: (Product) -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user by profileViewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.getUser()
    }

    val groupedProducts = remember(products) {
        products.groupBy { it.category }
    }
    val categories = groupedProducts.keys.toList()

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Welcome Back, ${user?.name}!", style = MaterialTheme.typography.titleMedium)
        Text(
            "Ready to find your next favorite product?",
            style = MaterialTheme.typography.bodyMedium
        )
    }

    PrimaryScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 16.dp
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        if (abs(pagerState.currentPage - index) <= 1) {
                            pagerState.animateScrollToPage(index)
                        } else {
                            pagerState.scrollToPage(index)
                        }
                    }
                },
                text = { Text(text = category) }
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        modifier = Modifier.fillMaxSize()
    ) { page ->

        val categoryName = categories[page]
        val filteredProducts = groupedProducts[categoryName] ?: emptyList()

        ProductList(products = filteredProducts, onItemClick = onItemClick, onOrderNow)

    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onItemClick: (Product) -> Unit,
    onOrderNow: (Product) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, onItemClick, onOrderNow)
        }
    }
}
