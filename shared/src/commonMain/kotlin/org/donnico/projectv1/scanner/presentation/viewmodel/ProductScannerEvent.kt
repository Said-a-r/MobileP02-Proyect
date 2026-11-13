package org.donnico.projectv1.scanner.presentation.viewmodel

sealed interface ProductScannerEvent {
    data class BarcodeChanged(val value: String) : ProductScannerEvent
    data object SearchRequested : ProductScannerEvent
    data object ClearRequested : ProductScannerEvent
}
