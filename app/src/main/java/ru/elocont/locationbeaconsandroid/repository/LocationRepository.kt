package ru.elocont.locationbeaconsandroid.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.elocont.locationbeaconsandroid.model.request.CellInfo
import ru.elocont.locationbeaconsandroid.service.UnwiredLabsService

class LocationRepository(
    private val service: UnwiredLabsService
) {
    suspend fun getLocationByCellInfo(cellInfo: CellInfo) = flow {
        val response = service.getLocationByCellInfo(cellInfo)
        emit(response.body())
    }.flowOn(Dispatchers.IO)
}