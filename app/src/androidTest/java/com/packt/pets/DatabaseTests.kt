package com.packt.pets

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.packt.pets.data.CatDao
import com.packt.pets.data.CatEntity
import com.packt.pets.data.PetsDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Tom Buczynski on 19.03.2025.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTests {

    private lateinit var database: PetsDatabase
    private lateinit var dao: CatDao

    @Before
    fun setup() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(appContext, PetsDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.getCatDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertItem() {

        runTest {
            val cat = CatEntity(
                id = "GUOunrpLPB6Jw0zE",
                tags = listOf(
                    "handicapped",
                    "tabby",
                    "double",
                    "multiple",
                ),
                createdAt = "2024-06-18T09:46:45.702Z",
                updatedAt = "",
                owner = ""
            )

            dao.insert(cat)
            val flow = dao.getAllCats()
            val list = flow.first()
            assertThat("There must be only one \"cat\"", list, allOf(hasSize(1), hasItem(cat)))
        }
    }

    @Test
    @Throws(Exception::class)
    fun testAddItemToFavorities() {
        runTest {
            val cat = CatEntity(
                id = "GUOunrpLPB6Jw0zE",
                tags = listOf(
                    "handicapped",
                    "tabby",
                    "double",
                    "multiple",
                ),
                createdAt = "2024-06-18T09:46:45.702Z",
                updatedAt = "",
                owner = "",
                isFavorite = false
            )

            val favoriteCat = cat.copy(isFavorite = true)

            dao.insert(cat)
            var flow = dao.getFavoriteCats()
            var list = flow.first()
            assertThat("There must be no favorities", list, hasSize(0))

            dao.update(favoriteCat)
            flow = dao.getFavoriteCats()
            list = flow.first()

            assertThat("There must be only one favorite \"cat\"", list, allOf(hasSize(1), hasItem(favoriteCat)))
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

}