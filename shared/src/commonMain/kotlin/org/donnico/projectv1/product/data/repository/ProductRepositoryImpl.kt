package org.donnico.projectv1.product.data.repository

import org.donnico.projectv1.product.data.datasource.ProductLocalDataSource
import org.donnico.projectv1.product.domain.model.Product
import org.donnico.projectv1.product.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val localDataSource: ProductLocalDataSource
) : ProductRepository {
    override suspend fun findByBarcode(barcode: String): Product? {
        localDataSource.seedProductsIfEmpty()
        return localDataSource.findByBarcode(barcode)
    }

    override suspend fun save(product: Product) {
        localDataSource.save(product)
    }
}
