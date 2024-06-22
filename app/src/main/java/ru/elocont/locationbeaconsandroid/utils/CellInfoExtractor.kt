package ru.elocont.locationbeaconsandroid.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import ru.elocont.locationbeaconsandroid.model.request.Cell
import ru.elocont.locationbeaconsandroid.model.request.CellInfo
import ru.elocont.locationbeaconsandroid.model.request.RadioType

@SuppressLint("MissingPermission")
fun getCurrentCellInfo(context: Context): List<CellInfo> {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val accCellInfo = telephonyManager.allCellInfo

    return accCellInfo.mapNotNull {
        when (it) {
            is CellInfoGsm -> getCellInfo(it)
            is CellInfoWcdma -> getCellInfo(it)
            is CellInfoLte -> getCellInfo(it)
            else -> null
        }
    }
}

fun getCellInfo(info: CellInfoGsm): CellInfo {
    val cellInfo = CellInfo()
    cellInfo.radio = RadioType.GSM

    info.cellIdentity.let {
        val (mcc, mnc) =
            Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        cellInfo.mcc = mcc
        cellInfo.mnc = mnc
        cellInfo.cells = listOf(Cell(it.lac, it.cid, null))
    }

    return cellInfo
}

fun getCellInfo(info: CellInfoWcdma): CellInfo {
    val cellInfo = CellInfo()

    cellInfo.radio = RadioType.CDMA

    info.cellIdentity.let {
        val (mcc, mnc) = Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        cellInfo.mcc = mcc
        cellInfo.mnc = mnc
        cellInfo.cells = listOf(Cell(it.lac, it.cid, it.psc))
    }

    return cellInfo
}

fun getCellInfo(info: CellInfoLte): CellInfo {
    val cellInfo = CellInfo()

    cellInfo.radio = RadioType.LTE

    info.cellIdentity.let {
        val (mcc, mnc) = Pair(it.mccString?.toInt() ?: 0, it.mncString?.toInt() ?: 0)
        cellInfo.mcc = mcc
        cellInfo.mnc = mnc
        cellInfo.cells = listOf(Cell(it.tac, it.ci, it.pci))
    }

    return cellInfo
}