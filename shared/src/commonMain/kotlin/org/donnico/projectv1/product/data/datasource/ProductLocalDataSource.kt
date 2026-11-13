package org.donnico.projectv1.product.data.datasource

import org.donnico.projectv1.data.local.DonNicoDatabase
import org.donnico.projectv1.product.data.mapper.toDomain
import org.donnico.projectv1.product.domain.model.Product

class ProductLocalDataSource(
    private val database: DonNicoDatabase
) {
    fun findByBarcode(barcode: String): Product? =
        database.productQueries.selectByBarcode(barcode).executeAsOneOrNull()?.toDomain()

    fun save(product: Product) {
        database.productQueries.insertOrReplaceProduct(
            id = product.id,
            barcode = product.barcode,
            name = product.name,
            description = product.description,
            price = product.price,
            cost_price = product.costPrice,
            stock = product.stock,
            min_stock = product.minStock
        )
    }

    fun seedProductsIfEmpty() {
        if (database.productQueries.countProducts().executeAsOne() > 0L) return

        sampleProducts.forEach(::save)
    }

    private companion object {
        val sampleProducts = listOf(
            Product(
                id = "product-1",
                barcode = "7790000000011",
                name = "Zapatilla urbana",
                description = "Zapatilla de uso diario",
                price = 350.0,
                costPrice = 190.0,
                stock = 12,
                minStock = 3
            ),
            Product(
                id = "product-2",
                barcode = "7790000000028",
                name = "Sandalia dama",
                description = "Sandalia de tiras para temporada de verano.",
                price = 220.0,
                costPrice = 115.0,
                stock = 8,
                minStock = 3
            ),
            Product(
                id = "product-3",
                barcode = "7790000000035",
                name = "Bota caballero",
                description = "Bota de cuero con suela de goma.",
                price = 480.0,
                costPrice = 265.0,
                stock = 5,
                minStock = 2
            )
        )
    }
}
