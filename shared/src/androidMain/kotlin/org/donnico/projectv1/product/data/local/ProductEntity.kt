package org.donnico.projectv1.product.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product",
    indices = [Index(value = ["barcode"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey val id: String,
    val barcode: String,
    val name: String,
    val description: String,
    val price: Double,
    val costPrice: Double,
    val stock: Long,
    val minStock: Long
)
