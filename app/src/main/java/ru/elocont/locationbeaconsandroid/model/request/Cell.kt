package ru.elocont.locationbeaconsandroid.model.request

data class Cell(
    val lac: Int,
    val cid: Int,
    var psc: Int? = null
)
