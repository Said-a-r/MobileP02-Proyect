package org.donnico.projectv1.scanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.donnico.projectv1.product.domain.model.ProductSearchResult
import org.donnico.projectv1.product.domain.usecase.FindProductByBarcodeUseCase
import org.donnico.projectv1.scanner.presentation.state.ProductScannerState

class ProductScannerViewModel(
    private val findProductByBarcode: FindProductByBarcodeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductScannerState())
    val state = _state.asStateFlow()

    fun onEvent(event: ProductScannerEvent) {
        when (event) {
            is ProductScannerEvent.BarcodeChanged -> {
                _state.value = _state.value.copy(
                    barcode = event.value,
                    product = null,
                    message = null
                )
            }

            ProductScannerEvent.SearchRequested -> searchProduct()
            ProductScannerEvent.ClearRequested -> _state.value = ProductScannerState()
        }
    }

    private fun searchProduct() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            val barcode = _state.value.barcode
            _state.value = _state.value.copy(isLoading = true, product = null, message = null)

            _state.value = when (val result = findProductByBarcode(barcode)) {
                is ProductSearchResult.Found -> ProductScannerState(
                    barcode = barcode,
                    product = result.product
                )

                ProductSearchResult.InvalidBarcode -> ProductScannerState(
                    barcode = barcode,
                    message = "Ingresa un codigo de barras para realizar la consulta"
                )

                ProductSearchResult.NotFound -> ProductScannerState(
                    barcode = barcode,
                    message = "No se encontro un producto con ese codigo"
                )

                is ProductSearchResult.Error -> ProductScannerState(
                    barcode = barcode,
                    message = result.message
                )
            }
        }
    }
}
