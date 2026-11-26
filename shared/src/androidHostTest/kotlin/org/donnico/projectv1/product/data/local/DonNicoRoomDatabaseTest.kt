package org.donnico.projectv1.product.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
class DonNicoRoomDatabaseTest {
    @Test
    fun savesAndFindsProductByBarcode() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val database = Room.inMemoryDatabaseBuilder(context, DonNicoRoomDatabase::class.java).build()

        try {
            val expected = ProductEntity(
                id = "test-1",
                barcode = "7790000000010",
                name = "Zapatilla urbana",
                description = "Zapatilla de uso diario",
                price = 350.0,
                costPrice = 190.0,
                stock = 12,
                minStock = 3
            )

            database.productDao().insertOrReplace(expected)
            val result = database.productDao().findByBarcode(expected.barcode)

            assertNotNull(result)
            assertEquals(expected, result)
        } finally {
            database.close()
        }
    }
}
