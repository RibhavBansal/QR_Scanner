package com.example.scanner_qr.db

import com.example.scanner_qr.db.database.QrResultDatabase
import com.example.scanner_qr.db.entities.QrResult
import java.util.Calendar

class DBHelper(var qrResultDatabase: QrResultDatabase) : DBHelperI {
    override fun insertQrResult(result: String): Int {
        val time = Calendar.getInstance()
        val resultType = "TEXT"
        val qrResult = QrResult(result = result, resultType = resultType, calendar = time, favourite = false)

        return qrResultDatabase.getQrDao().insertQrResult(qrResult).toInt()

    }

    override fun deleteAllQrScannerResults() {
        qrResultDatabase.getQrDao().deleteAllScannedResults()
    }

    override fun deleteAllFavouriteQrScannerResults() {
        qrResultDatabase.getQrDao().deleteAllFavouriteResult()
    }

    override fun deleteQrResult(id: Int): Int {
        return qrResultDatabase.getQrDao().deleteQrResult(id)
    }

    override fun getAllQrScannedResult(): List<QrResult> {
        return qrResultDatabase.getQrDao().getAllScannedResults()
    }

    override fun getAllFavouriteQrScannedResult(): List<QrResult> {
        return qrResultDatabase.getQrDao().getAllFavouriteResults()
    }

    override fun getQrResult(id: Int): QrResult {
        return qrResultDatabase.getQrDao().getQrResult(id)
    }

    override fun addToFavourite(id: Int): Int {
        return qrResultDatabase.getQrDao().addToFavourite(id)
    }

    override fun removeFromFavourite(id: Int): Int {
        return qrResultDatabase.getQrDao().removeFromFavourite(id)
    }
}