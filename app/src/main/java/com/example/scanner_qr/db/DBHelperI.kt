package com.example.scanner_qr.db

import com.example.scanner_qr.db.entities.QrResult

interface DBHelperI {
    fun insertQrResult(result: String) : Int

    fun getQrResult(id : Int) : QrResult

    fun addToFavourite(id:Int) : Int

    fun removeFromFavourite(id:Int) : Int

    fun getAllQrScannedResult() : List<QrResult>

    fun getAllFavouriteQrScannedResult() : List<QrResult>

    fun deleteQrResult(id:Int) : Int

    fun deleteAllQrScannerResults()

    fun deleteAllFavouriteQrScannerResults()
}