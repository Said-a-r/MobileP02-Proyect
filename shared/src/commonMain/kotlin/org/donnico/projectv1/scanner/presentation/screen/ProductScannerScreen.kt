package org.donnico.projectv1.scanner.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.donnico.projectv1.product.domain.model.Product
import org.donnico.projectv1.scanner.presentation.viewmodel.ProductScannerEvent
import org.donnico.projectv1.scanner.presentation.viewmodel.ProductScannerViewModel
import org.koin.compose.viewmodel.koinViewModel

private val OrangeColor = Color(0xFFD4703A)
private val CreamColor = Color(0xFFF5EFE8)

@Composable
fun ProductScannerScreen(
    viewModel: ProductScannerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamColor)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Consulta de productos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.barcode,
            onValueChange = { viewModel.onEvent(ProductScannerEvent.BarcodeChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Código de barras") },
            placeholder = { Text("7790000000011") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangeColor,
                focusedLabelColor = OrangeColor,
                cursorColor = OrangeColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.onEvent(ProductScannerEvent.SearchRequested) },
                modifier = Modifier.weight(1f),
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = OrangeColor)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Consultar producto")
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { viewModel.onEvent(ProductScannerEvent.ClearRequested) },
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF725547))
            ) {
                Text("Limpiar")
            }
        }

        state.message?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        state.product?.let { product ->
            Spacer(modifier = Modifier.height(20.dp))
            ProductResultCard(product)
        }
    }
}

@Composable
private fun ProductResultCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Producto encontrado",
                color = OrangeColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = product.description, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Codigo ${product.barcode}")
            Text(text = "Precio Bs ${product.price}")
            Text(text = "Stock disponible ${product.stock}")
        }
    }
}
