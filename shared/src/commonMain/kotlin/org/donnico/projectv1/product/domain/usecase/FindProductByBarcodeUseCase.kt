package org.donnico.projectv1.product.domain.usecase

import org.donnico.projectv1.product.domain.model.ProductSearchResult
import org.donnico.projectv1.product.domain.repository.ProductRepository

class FindProductByBarcodeUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(rawBarcode: String): ProductSearchResult {
        val barcode = rawBarcode.trim().replace(" ", "")
        if (barcode.isBlank()) return ProductSearchResult.InvalidBarcode

        return try {
            val product = repository.findByBarcode(barcode)
            if (product == null) ProductSearchResult.NotFound else ProductSearchResult.Found(product)
        } catch (_: Exception) {
            ProductSearchResult.Error("No se pudo consultar el producto intenta nuevamente")
        }
    }
}
