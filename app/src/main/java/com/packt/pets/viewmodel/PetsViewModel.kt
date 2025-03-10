package com.packt.pets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.packt.pets.data.Cat
import com.packt.pets.data.NetworkResult
import com.packt.pets.data.PetsRepository
import com.packt.pets.workers.SynchronizePetsWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PetsViewModel(
    private val petsRepository: PetsRepository,
    private val workManager: WorkManager?,
) : ViewModel() {

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

    fun setSelectedPet(pet: Cat?) {
        mSelectedPet.value = pet
    }

    fun getPets() {
        mPetListUISTate.value = PetListUIState(isLoading = true)

        viewModelScope.launch {
            val resultFlow = petsRepository.getAllPets()

            resultFlow.collect { result ->
                when (result) {
                    is NetworkResult.Success -> if (result.data.isNotEmpty()) {
                        mPetListUISTate.value = PetListUIState(isLoading = false, pets = result.data)
                    }

                    is NetworkResult.Error -> mPetListUISTate.update {
                        it.copy(isLoading = false, error = result.msg)
                    }
                }
            }
        }
    }

    fun getFavoritePets() {
        viewModelScope.launch {
            val catListFlow = petsRepository.getFavoritePets()

            catListFlow.collect { catList ->
                mFavoritePetList.value = catList
            }
        }
    }

    fun startPetsSynchronization() {
        if (workManager != null) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val workRequest: OneTimeWorkRequest =
                OneTimeWorkRequestBuilder<SynchronizePetsWorker>()
                    .setConstraints(constraints)
                    .setInitialDelay(1, TimeUnit.SECONDS)
                    .build()

            workManager.enqueueUniqueWork(
                uniqueWorkName = "SynchronizePets",
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = workRequest,
            )
        }
    }

    init {
        getPets()
        getFavoritePets()
        startPetsSynchronization()
    }

    // Leaking
    // var context: Context? = null
}

data class PetListUIState(
    val isLoading: Boolean = false,
    val pets: List<Cat> = emptyList(),
    val error: String? = null,
)