package ru.elocont.locationbeaconsandroid.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.elocont.locationbeaconsandroid.model.request.CellInfo
import ru.elocont.locationbeaconsandroid.repository.LocationRepository

class LocationViewModel(
    private val repository: LocationRepository
): ViewModel() {
    private var job: Job? = null
    private val _locationLiveData = MutableLiveData<State>()

    val locationLiveData: LiveData<State> = _locationLiveData

    fun fetchLocation(cellInfo: CellInfo) {
        job?.cancel()

        job = viewModelScope.launch {
            repository.getLocationByCellInfo(cellInfo)
                .onStart {
                    _locationLiveData.value = State.Loading
                }
                .catch {
                    it.printStackTrace()
                    _locationLiveData.value = State.Failed(it.message.toString())
                }
                .collect { cellLocation ->
                    cellLocation?.let {
                        if (cellLocation.isSuccess()) {
                            _locationLiveData.value = State.Success(cellLocation)
                        } else {
                            _locationLiveData.value = State.Failed(cellLocation.message.toString())
                        }
                    }
                }
        }
    }
}