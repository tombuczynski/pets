package com.packt.pets

import com.packt.pets.data.Cat
import com.packt.pets.data.NetworkResult
import com.packt.pets.data.PetsRepository
import com.packt.pets.viewmodel.PetsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.*
import io.mockk.coVerify
import io.mockk.confirmVerified
import kotlinx.coroutines.test.runTest

/**
 * Created by Tom Buczynski on 27.03.2025.
 */
class ViewModelTests {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PetsViewModel;
    private lateinit var petsRepository: PetsRepository;

    @Before
    fun setup() {
        petsRepository = mockk<PetsRepository>(relaxed = true)
        viewModel = PetsViewModel(petsRepository, null)
    }

    @Test
    fun testPetsRepository() = runTest {

        val cats = listOf(
            Cat(
                "GUOunrpLPB6Jw0zE",
                listOf(
                    "handicapped",
                    "tabby",
                    "double",
                    "multiple",
                ),
                true,
            ),
            Cat("dmjubveyQIbCFA2v", listOf("orange"))
        )


        val catsResult =
            NetworkResult.Success(cats)

        coEvery { petsRepository.getAllPets() } returns flowOf(catsResult)
        coEvery { petsRepository.getFavoritePets() } returns flowOf(cats)

        viewModel.getPets()

        coVerify { petsRepository.getAllPets() }

        val retrievedPets = viewModel.petListUISTate.value.pets
        assertThat(retrievedPets).isEqualTo(catsResult.data)

        viewModel.getFavoritePets()

        coVerify { petsRepository.getFavoritePets() }

        val retrievedFavPets = viewModel.favoritePetList.value
        assertThat(retrievedFavPets).isEqualTo(cats)

        confirmVerified(petsRepository)

    }
}