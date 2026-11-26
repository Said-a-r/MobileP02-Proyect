package org.donnico.projectv1.product.domain.usecase

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.donnico.projectv1.product.domain.model.Product
import org.donnico.projectv1.product.domain.model.ProductSearchResult
import org.donnico.projectv1.product.domain.repository.ProductRepository

class FindProductByBarcodeUseCaseTest {
    private val product = Product(
        id = "product-1",
        barcode = "7790000000010",
        name = "Zapatilla urbana",
        description = "Producto de prueba",
        price = 350.0,
        costPrice = 190.0,
        stock = 12,
        minStock = 3
    )

    @Test
    fun `returns product when barcode exists`() = runBlocking {
        val useCase = FindProductByBarcodeUseCase(FakeProductRepository(product))

        val result = useCase(" 7790000000010 ")

        val found = assertIs<ProductSearchResult.Found>(result)
        assertEquals(product, found.product)
    }

    @Test
    fun `returns invalid result when barcode is blank`() = runBlocking {
        val useCase = FindProductByBarcodeUseCase(FakeProductRepository(product))

        val result = useCase("   ")

        assertEquals(ProductSearchResult.InvalidBarcode, result)
    }

    @Test
    fun `returns not found result when barcode does not exist`() = runBlocking {
        val useCase = FindProductByBarcodeUseCase(FakeProductRepository(product))

        val result = useCase("7790000000097")

        assertEquals(ProductSearchResult.NotFound, result)
    }

    private class FakeProductRepository(
        private val product: Product
    ) : ProductRepository {
        override suspend fun findByBarcode(barcode: String): Product? =
            product.takeIf { it.barcode == barcode }

        override suspend fun save(product: Product) = Unit
    }
}
