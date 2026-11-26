package org.donnico.projectv1.product.domain.model

data class Product(
    val id: String,
    val barcode: String,
    val name: String,
    val description: String,
    val price: Double,
    val costPrice: Double,
    val stock: Long,
    val minStock: Long
)
