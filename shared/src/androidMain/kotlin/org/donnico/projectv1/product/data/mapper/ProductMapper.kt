package org.donnico.projectv1.product.data.mapper

import org.donnico.projectv1.product.data.local.ProductEntity
import org.donnico.projectv1.product.domain.model.Product

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    barcode = barcode,
    name = name,
    description = description,
    price = price,
    costPrice = costPrice,
    stock = stock,
    minStock = minStock
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    barcode = barcode,
    name = name,
    description = description,
    price = price,
    costPrice = costPrice,
    stock = stock,
    minStock = minStock
)
