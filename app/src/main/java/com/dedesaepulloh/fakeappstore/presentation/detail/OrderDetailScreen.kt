package com.dedesaepulloh.fakeappstore.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dedesaepulloh.fakeappstore.presentation.cart.CartViewModel

@Composable
fun OrderDetailScreen(
    modifier: Modifier = Modifier,
    address: String = "Jl. H. Soleh II No.2, RT.7/RW.2, Sukabumi Sel., Kec. Kb. Jeruk, Kota Jakarta Barat, Daerah Khusus Ibukota Jakarta 11560",
    paymentMethod: String = "Virtual Account BCA",
    onPayClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    val checkoutProducts by cartViewModel.checkoutProducts.collectAsState()

    val totalPrice = remember(checkoutProducts) {
        checkoutProducts.sumOf { it.price * it.quantity }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Shipping Address", style = MaterialTheme.typography.titleMedium)
        Text(address, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ordered Products", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        checkoutProducts.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("Rp${item.price} x${item.quantity}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Payment Method", style = MaterialTheme.typography.titleMedium)
        Text(paymentMethod, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Payment Details", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        RowItem(label = "Total Price", value = "Rp${totalPrice}")
        RowItem(label = "Shipping Fee", value = "Rp0")
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        RowItem(label = "Total Payment", value = "Rp${totalPrice}", bold = true)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onPayClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay Now")
        }
    }
}

@Composable
fun RowItem(label: String, value: String, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            style = if (bold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (bold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
        )
    }
}
