package org.donnico.projectv1.product.domain.repository

import org.donnico.projectv1.product.domain.model.Product

interface ProductRepository {
    suspend fun findByBarcode(barcode: String): Product?

    suspend fun save(product: Product)
}
