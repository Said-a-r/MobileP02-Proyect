package org.donnico.projectv1.product.data.datasource

import org.donnico.projectv1.product.data.local.ProductDao
import org.donnico.projectv1.product.data.mapper.toDomain
import org.donnico.projectv1.product.data.mapper.toEntity
import org.donnico.projectv1.product.domain.model.Product

class ProductLocalDataSource(
    private val productDao: ProductDao
) {
    suspend fun findByBarcode(barcode: String): Product? =
        productDao.findByBarcode(barcode)?.toDomain()

    suspend fun save(product: Product) {
        productDao.insertOrReplace(product.toEntity())
    }

    suspend fun seedProductsIfEmpty() {
        if (productDao.countProducts() > 0L) return

        for (product in sampleProducts) {
            save(product)
        }
    }

    private companion object {
        val sampleProducts = listOf(
            Product(
                id = "product-1",
                barcode = "7790000000010",
                name = "Zapatilla urbana",
                description = "Zapatilla de uso diario",
                price = 350.0,
                costPrice = 190.0,
                stock = 12,
                minStock = 3
            ),
            Product(
                id = "product-2",
                barcode = "7790000000027",
                name = "Sandalia dama",
                description = "Sandalia de tiras para temporada de verano",
                price = 220.0,
                costPrice = 115.0,
                stock = 8,
                minStock = 3
            ),
            Product(
                id = "product-3",
                barcode = "7790000000034",
                name = "Bota caballero",
                description = "Bota de cuero con suela de goma",
                price = 480.0,
                costPrice = 265.0,
                stock = 5,
                minStock = 2
            )
        )
    }
}
