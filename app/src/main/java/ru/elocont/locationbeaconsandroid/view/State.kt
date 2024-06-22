package ru.elocont.locationbeaconsandroid.view

import ru.elocont.locationbeaconsandroid.model.response.CellLocation

sealed class State {
    object Loading: State()
    data class Success(val response: CellLocation): State()
    data class Failed(val message: String): State()
}