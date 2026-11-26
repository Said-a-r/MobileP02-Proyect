package org.donnico.projectv1.scanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.donnico.projectv1.product.domain.model.Product
import org.donnico.projectv1.product.domain.model.ProductSearchResult
import org.donnico.projectv1.product.domain.usecase.FindProductByBarcodeUseCase
import org.donnico.projectv1.product.domain.usecase.SaveProductUseCase
import org.donnico.projectv1.scanner.presentation.state.ProductScannerState

class ProductScannerViewModel(
    private val findProductByBarcode: FindProductByBarcodeUseCase,
    private val saveProduct: SaveProductUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductScannerState())
    val state = _state.asStateFlow()

    fun onEvent(event: ProductScannerEvent) {
        when (event) {
            is ProductScannerEvent.BarcodeChanged -> {
                _state.value = _state.value.copy(
                    barcode = event.value,
                    isCameraScanning = false,
                    product = null,
                    message = null,
                    canAddProduct = false
                )
            }

            is ProductScannerEvent.BarcodeScanned -> {
                _state.value = _state.value.copy(
                    barcode = event.value,
                    isCameraScanning = false,
                    product = null,
                    message = null,
                    canAddProduct = false
                )
                searchProduct(event.value, canAddProduct = true)
            }

            ProductScannerEvent.SearchRequested -> searchProduct()
            ProductScannerEvent.AddProductRequested -> addProduct()
            ProductScannerEvent.StartCameraScan -> _state.value = ProductScannerState()
        }
    }

    private fun searchProduct(
        barcodeToSearch: String = _state.value.barcode,
        canAddProduct: Boolean = false
    ) {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                barcode = barcodeToSearch,
                isLoading = true,
                isCameraScanning = false,
                product = null,
                message = null,
                canAddProduct = false
            )

            _state.value = when (val result = findProductByBarcode(barcodeToSearch)) {
                is ProductSearchResult.Found -> ProductScannerState(
                    barcode = barcodeToSearch,
                    isCameraScanning = false,
                    product = result.product,
                    canAddProduct = false
                )

                ProductSearchResult.InvalidBarcode -> ProductScannerState(
                    barcode = barcodeToSearch,
                    isCameraScanning = false,
                    message = "Ingresa un codigo de barras para realizar la consulta",
                    canAddProduct = false
                )

                ProductSearchResult.NotFound -> ProductScannerState(
                    barcode = barcodeToSearch,
                    isCameraScanning = false,
                    message = "No se encontro un producto con ese codigo",
                    canAddProduct = canAddProduct
                )

                is ProductSearchResult.Error -> ProductScannerState(
                    barcode = barcodeToSearch,
                    isCameraScanning = false,
                    message = result.message,
                    canAddProduct = false
                )
            }
        }
    }

    private fun addProduct() {
        val barcode = _state.value.barcode.trim()
        if (!_state.value.canAddProduct || barcode.isBlank() || _state.value.isLoading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                message = null,
                canAddProduct = false
            )

            val product = Product(
                id = "product-$barcode",
                barcode = barcode,
                name = "Producto sin nombre",
                description = "Producto agregado desde el escaner",
                price = 0.0,
                costPrice = 0.0,
                stock = 0,
                minStock = 0
            )

            try {
                saveProduct(product)
                _state.value = ProductScannerState(
                    barcode = barcode,
                    isCameraScanning = false,
                    product = product,
                    message = "Producto agregado al catalogo"
                )
            } catch (_: Exception) {
                _state.value = ProductScannerState(
                    barcode = barcode,
                    isCameraScanning = false,
                    message = "No se pudo agregar el producto"
                )
            }
        }
    }
}
