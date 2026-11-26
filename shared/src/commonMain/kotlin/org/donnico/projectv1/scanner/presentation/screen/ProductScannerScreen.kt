package org.donnico.projectv1.scanner.presentation.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.donnico.projectv1.product.domain.model.Product
import org.donnico.projectv1.scanner.presentation.camera.BarcodeCameraScanner
import org.donnico.projectv1.scanner.presentation.viewmodel.ProductScannerEvent
import org.donnico.projectv1.scanner.presentation.viewmodel.ProductScannerViewModel
import org.koin.compose.viewmodel.koinViewModel

private val OrangeColor = Color(0xFFD4703A)
private val CreamColor = Color(0xFFF5EFE8)
private val BrownColor = Color(0xFF4A3020)
private val CameraBackground = Color(0xFF2C1A0E)
private val SuccessColor = Color(0xFF3A6B45)
private val SoftOrange = Color(0xFFFDF0E0)

@Composable
fun ProductScannerScreen(
    viewModel: ProductScannerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamColor)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Escaner de productos",
            style = MaterialTheme.typography.headlineSmall,
            color = BrownColor,
            fontWeight = FontWeight.Bold
        )
        if (state.isCameraScanning) {
            ScannerCameraPreview(
                onBarcodeDetected = {
                    viewModel.onEvent(ProductScannerEvent.BarcodeScanned(it))
                }
            )
        } else {
            OutlinedButton(
                onClick = { viewModel.onEvent(ProductScannerEvent.StartCameraScan) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = OrangeColor)
            ) {
                Text("Escanear otro codigo")
            }
        }

        ScannerResult(
            isScanning = state.isCameraScanning || state.isLoading,
            product = state.product,
            message = state.message,
            canAddProduct = state.canAddProduct,
            onAddProduct = { viewModel.onEvent(ProductScannerEvent.AddProductRequested) }
        )

        ManualLookup(
            barcode = state.barcode,
            isLoading = state.isLoading,
            onBarcodeChanged = { viewModel.onEvent(ProductScannerEvent.BarcodeChanged(it)) },
            onSearchRequested = { viewModel.onEvent(ProductScannerEvent.SearchRequested) }
        )
    }
}

@Composable
private fun ScannerCameraPreview(onBarcodeDetected: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CameraBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            BarcodeCameraScanner(
                isScanning = true,
                onBarcodeDetected = onBarcodeDetected,
                modifier = Modifier.fillMaxSize()
            )
            ScanGuide(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun ScanGuide(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.82f)
                .height(150.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x224A3020))
        )
        Text(
            text = "Manten el codigo dentro del recuadro",
            modifier = Modifier.align(Alignment.BottomCenter),
            color = Color(0xFFFDF6EE),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ScannerResult(
    isScanning: Boolean,
    product: Product?,
    message: String?,
    canAddProduct: Boolean,
    onAddProduct: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFAF5))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Resultado de la lectura",
                style = MaterialTheme.typography.titleMedium,
                color = BrownColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isScanning -> ScannerWaitingState()
                product != null -> ProductResult(product)
                message != null -> ScannerMessage(
                    message = message,
                    canAddProduct = canAddProduct,
                    onAddProduct = onAddProduct
                )
                else -> Text(
                    text = "Escanea un codigo o ingresalo manualmente",
                    color = Color(0xFF725547),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ScannerWaitingState() {
    Text("Buscando codigo", color = BrownColor, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun ScannerMessage(
    message: String,
    canAddProduct: Boolean,
    onAddProduct: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDEA)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(14.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (canAddProduct) {
            Button(
                onClick = onAddProduct,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeColor)
            ) {
                Text("Añadir producto")
            }
        }
    }
}

@Composable
private fun ProductResult(product: Product) {
    Column {
        Text(
            text = "PRODUCTO ENCONTRADO",
            color = SuccessColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = product.name,
            color = BrownColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = product.description,
            color = Color(0xFF725547),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProductFact(
                label = "Precio",
                value = "Bs ${product.price.toInt()}",
                modifier = Modifier.weight(1f)
            )
            ProductFact(
                label = "Stock actual",
                value = "${product.stock} pares",
                valueColor = if (product.stock > 0) SuccessColor else MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Codigo ${product.barcode}",
            color = Color(0xFF8B6B4A),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ProductFact(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = BrownColor
) {
    Column(
        modifier = modifier
            .background(SoftOrange, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF8B6B4A),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = valueColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ManualLookup(
    barcode: String,
    isLoading: Boolean,
    onBarcodeChanged: (String) -> Unit,
    onSearchRequested: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Consulta manual",
            style = MaterialTheme.typography.titleMedium,
            color = BrownColor,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = barcode,
            onValueChange = onBarcodeChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Codigo de barras") },
            placeholder = { Text("7790000000010") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = OrangeColor,
                focusedLabelColor = OrangeColor,
                cursorColor = OrangeColor
            )
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onSearchRequested,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = OrangeColor)
            ) {
                Text("Consultar")
            }
        }
    }
}
