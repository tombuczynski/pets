package com.packt.pets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.pets.data.Cat
import com.packt.pets.data.NetworkResult
import com.packt.pets.data.PetsRepository
import com.packt.pets.data.asNetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetsViewModel(
    private val petsRepository: PetsRepository// = PetsRepositoryDemo()
): ViewModel() {

    private val mPetListUISTate = MutableStateFlow(PetListUIState())
    private val mFavoritePetList: MutableStateFlow<List<Cat>> = MutableStateFlow(emptyList())
    private val mSelectedPet: MutableStateFlow<Cat?> = MutableStateFlow(null)

    val petListUISTate: StateFlow<PetListUIState>
        get() = mPetListUISTate


    val favoritePetList: StateFlow<List<Cat>>
        get() = mFavoritePetList

    fun updatePet(pet: Cat) {
        viewModelScope.launch {
            petsRepository.updatePet(pet)
        }
    }

    val selectedPet: StateFlow<Cat?>
        get() = mSelectedPet

    fun setSelectedPet(pet: Cat?) { mSelectedPet.value = pet }


    fun fetchPets() {
        mPetListUISTate.value = PetListUIState(isLoading = true)

        viewModelScope.launch {
            val resultFlow = petsRepository.getAllPets().asNetworkResult()

            resultFlow.collect { result ->
                when (result) {
                    is NetworkResult.Success -> mPetListUISTate.update {
                        it.copy(isLoading = false, pets = result.data)
                    }

                    is NetworkResult.Error -> mPetListUISTate.update {
                        it.copy(isLoading = false, error = result.msg)
                    }
                }
            }
        }
    }

    fun fetchFavoritePets() {
        viewModelScope.launch {
            val catListFlow = petsRepository.getFavoritePets()

            catListFlow.collect { catList ->
                mFavoritePetList.value = catList
            }
        }
    }

    init {
        fetchPets()
        fetchFavoritePets()
    }
}

data class PetListUIState(
    val isLoading: Boolean = false,
    val pets: List<Cat> = emptyList(),
    val error: String? = null
)