package org.donnico.projectv1.product.domain.usecase

import org.donnico.projectv1.product.domain.model.Product
import org.donnico.projectv1.product.domain.repository.ProductRepository

class SaveProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.save(product)
    }
}
