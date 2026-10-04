package com.pemmob.sani.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pemmob.sani.R
import com.pemmob.sani.data.model.Product
import com.pemmob.sani.ui.viewmodel.ProductUiState
import com.pemmob.sani.ui.viewmodel.ProductViewModel
import com.pemmob.sani.util.JualanConstants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(productId: Int, navController: NavController?, viewModel: ProductViewModel) {
    val context = LocalContext.current
    var quantity by rememberSaveable { mutableStateOf(1) }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ProductUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ProductUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }

            is ProductUiState.Success -> {
                val product = state.products.find { it.id == productId }

                if (product == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Produk tidak ditemukan.")
                    }
                } else {
                    StatelessDetailProduct(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        onBackClick = { navController?.popBackStack() },
                        onAddToCartClick = {
                            Toast.makeText(context, "Membeli sebanyak $quantity", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun StatelessDetailProduct(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        val imageModel: Any = if (product.img == "dummy_product") {
            R.drawable.dummy_product
        } else {
            "${JualanConstants.BASE_URL}img/${product.img}"
        }

        AsyncImage(
            model = imageModel,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentScale = ContentScale.Fit
        )

        Column(modifier = Modifier.padding(all = 16.dp)) {
            if (product.category != null) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(size = 4.dp))
                        .background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = product.category.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                product.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("Rp ${product.price}", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Deskripsi", fontWeight = FontWeight.Bold)
            Text(product.description ?: "")
            Text("Stok Tersedia: ${product.stock}")

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Jumlah Beli")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                        enabled = quantity > 1
                    ) { Text("-") }

                    Text(quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))

                    FilledTonalIconButton(
                        onClick = { if (quantity < product.stock) onQuantityChange(quantity + 1) },
                        enabled = quantity < product.stock
                    ) { Text("+") }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAddToCartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = product.stock > 0 && quantity > 0
            ) {
                Text("Tambah ke Keranjang")
            }
        }
    }
}