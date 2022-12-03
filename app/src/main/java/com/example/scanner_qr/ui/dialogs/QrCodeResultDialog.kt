package com.example.scanner_qr.ui.dialogs

import android.app.Dialog
import android.content.Context
import androidx.core.content.getSystemService
import android.content.ClipData
import android.content.Intent
import android.text.ClipboardManager
import android.widget.Toast
import com.example.scanner_qr.R
import com.example.scanner_qr.db.DBHelper
import com.example.scanner_qr.db.DBHelperI
import com.example.scanner_qr.db.database.QrResultDatabase
import com.example.scanner_qr.db.entities.QrResult
import com.example.scanner_qr.ui.utils.toFormattedDisplay
import kotlinx.android.synthetic.main.layout_qr_result_show.*


class QrCodeResultDialog(var context : Context) {
    private lateinit var dialog : Dialog

    private  var qrResult: QrResult? = null

    private var onDismissListener : OnDismissListener? = null

    private lateinit var dbHelperI : DBHelperI

    init {
        init()
        initDialog()
    }

    private fun init() {
        dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(context)!!)
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(false)
        onClicks()
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener){
        this.onDismissListener = onDismissListener
    }

    fun show(qrResult: QrResult){
        this.qrResult = qrResult
        dialog.scannedDate.text = qrResult.calendar.toFormattedDisplay()
        dialog.scannedText.text = qrResult.result
        dialog.favouriteIcon.isSelected = qrResult.favourite
        dialog.show()
    }

    private fun onClicks() {
        dialog.favouriteIcon.setOnClickListener{
            if(dialog.favouriteIcon.isSelected){
                removeFromFavourite()
            } else {
                addToFavourite()
            }
        }

        dialog.shareResult.setOnClickListener{
            shareResult()
        }

        dialog.copyResult.setOnClickListener{
            copyResultToClipboard()
        }

        dialog.cancelDialog.setOnClickListener{
            onDismissListener?.onDismiss()
            dialog.dismiss()
        }
    }

    private fun addToFavourite() {
        dialog.favouriteIcon.isSelected = true
        dbHelperI.addToFavourite(qrResult?.id!!)
    }

    private fun removeFromFavourite() {
        dialog.favouriteIcon.isSelected = false
        dbHelperI.removeFromFavourite(qrResult?.id!!)
    }

    private fun shareResult() {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(Intent.EXTRA_TEXT,dialog.scannedText.text.toString())
        context.startActivity(txtIntent)
    }

    private fun copyResultToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrScannerResult",dialog.scannedText.text)
        Toast.makeText(context,"Copied to Clipboard",Toast.LENGTH_SHORT).show()
    }

    interface OnDismissListener{
        fun onDismiss()
    }
}
