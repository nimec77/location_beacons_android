package ru.elocont.locationbeaconsandroid.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.elocont.locationbeaconsandroid.repository.LocationRepository
import ru.elocont.locationbeaconsandroid.service.buildUnwiredLabsService

class LocationViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(
                LocationRepository(buildUnwiredLabsService())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}