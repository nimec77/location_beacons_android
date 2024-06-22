package ru.elocont.locationbeaconsandroid.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.elocont.locationbeaconsandroid.model.request.CellInfo
import ru.elocont.locationbeaconsandroid.model.response.CellLocation

interface UnwiredLabsService {
    @POST("v2/process.php")
    suspend fun getLocationByCellInfo(@Body cellInfo: CellInfo): Response<CellLocation>

    companion object {
        const val BASE_URL = "https://ap1.unwiredlabs.com/"
    }
}