package org.donnico.projectv1.scanner.presentation.state

import org.donnico.projectv1.product.domain.model.Product

data class ProductScannerState(
    val barcode: String = "",
    val isLoading: Boolean = false,
    val isCameraScanning: Boolean = true,
    val product: Product? = null,
    val message: String? = null,
    val canAddProduct: Boolean = false
)
