package ru.elocont.locationbeaconsandroid.model.request

import ru.elocont.locationbeaconsandroid.BuildConfig

data class CellInfo(
    val token: String = BuildConfig.OPENCELLID_API_KEY,
    var radio: String? = null,
    var mcc: Int? = null,
    var mnc: Int? = null,
    var cells: List<Cell> = emptyList(),
    var address: Int = 1
)