package org.donnico.projectv1.product.domain.model

sealed interface ProductSearchResult {
    data class Found(val product: Product) : ProductSearchResult
    data object InvalidBarcode : ProductSearchResult
    data object NotFound : ProductSearchResult
    data class Error(val message: String) : ProductSearchResult
}
