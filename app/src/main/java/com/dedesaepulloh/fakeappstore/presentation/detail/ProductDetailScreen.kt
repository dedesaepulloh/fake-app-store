package com.dedesaepulloh.fakeappstore.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.dedesaepulloh.fakeappstore.domain.model.Product
import com.dedesaepulloh.fakeappstore.presentation.cart.CartViewModel
import com.dedesaepulloh.fakeappstore.presentation.components.ErrorScreen
import com.dedesaepulloh.fakeappstore.presentation.components.LoadingScreen
import com.dedesaepulloh.fakeappstore.presentation.detail.state.DetailState

@Composable
fun ProductDetailScreen(
    id: Int = 0,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    cartViewModel: CartViewModel,
    onOrderNow: (Product) -> Unit = {}
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.getDetail(id)
    }

    Box(modifier = modifier.fillMaxSize()) {

        when (state) {
            is DetailState.Loading -> {
                LoadingScreen()
            }

            is DetailState.Error -> {
                ErrorScreen(
                    message = (state as DetailState.Error).message,
                    onRetry = { viewModel.getDetail(id) }
                )
            }

            is DetailState.Success -> {
                val product = (state as DetailState.Success).data
                if (product != null) {
                    ProductDetailContent(product, cartViewModel, onOrderNow)
                }
            }

            else -> Unit
        }

    }
}

@Composable
fun ProductDetailContent(
    product: Product,
    cartViewModel: CartViewModel = hiltViewModel(),
    onOrderNow: (Product) -> Unit = {}
) {

    val context = LocalContext.current

    val onAddToCart: (Product) -> Unit = { item ->
        cartViewModel.addOrUpdateCartItem(item)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rp ${product.price}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Wishlist"
                )
            }

            IconButton(
                onClick = {
                    onAddToCart(product)
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Add To Cart"
                )
            }

            Button(
                onClick = {
                    cartViewModel.setCheckoutProducts(listOf(product))
                    onOrderNow(product)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Beli Sekarang")
            }

        }
    }
}

