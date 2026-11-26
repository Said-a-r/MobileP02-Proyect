package org.donnico.projectv1.product.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class DonNicoRoomDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
