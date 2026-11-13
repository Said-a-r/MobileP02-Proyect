package org.donnico.projectv1.product.data.mapper

import org.donnico.projectv1.data.local.Product
import org.donnico.projectv1.product.domain.model.Product as DomainProduct

fun Product.toDomain(): DomainProduct = DomainProduct(
    id = id,
    barcode = barcode,
    name = name,
    description = description,
    price = price,
    costPrice = cost_price,
    stock = stock,
    minStock = min_stock
)
