package com.example.scanner_qr.ui.scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.scanner_qr.R
import com.example.scanner_qr.db.DBHelper
import com.example.scanner_qr.db.DBHelperI
import com.example.scanner_qr.db.database.QrResultDatabase
import com.example.scanner_qr.ui.dialogs.QrCodeResultDialog
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import kotlinx.android.synthetic.main.fragment_qr_scanner.view.*
import kotlin.coroutines.*

class QrScannerFragment : Fragment(),ZXingScannerView.ResultHandler {
    companion object {
        fun newInstance(): QrScannerFragment {
            return QrScannerFragment()
        }
    }

    private lateinit var mView: View

    private lateinit var scannerView: ZXingScannerView

    private lateinit var resultDialog: QrCodeResultDialog

    private lateinit var dbHelperI : DBHelperI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        init()
        initViews()
        onClicks()
        return mView.rootView
    }

    private fun init() {
        dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(requireContext())!!)
    }

    private fun initViews() {
        initializeQRCamera()
        setResultDialog()
    }

    private fun setResultDialog() {
        resultDialog = QrCodeResultDialog(requireContext())
        resultDialog.setOnDismissListener(object : QrCodeResultDialog.OnDismissListener{
            override fun onDismiss(){
                scannerView.resumeCameraPreview(this@QrScannerFragment)
            }
        })
    }

    private fun onClicks() {
        mView.flashToggle.setOnClickListener{
            if(it.isSelected) {
                offFlashLight()
            }else{
                onFlashLight()
            }
        }
    }

    private fun offFlashLight() {
        mView.flashToggle.isSelected = false
        scannerView.flash = false
    }

    private fun onFlashLight() {
        mView.flashToggle.isSelected = true
        scannerView.flash = true
    }

    private fun initializeQRCamera() {
        scannerView = ZXingScannerView(requireContext())
        scannerView.setResultHandler(this)
        scannerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorTranslucent))
        scannerView.setBorderColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        scannerView.setLaserColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        scannerView.setBorderStrokeWidth(10)
        scannerView.setSquareViewFinder(true)
//        scannerView.setupScanner()
        scannerView.setAutoFocus(true)
        startQRCamera()
        mView.containerScanner.addView(scannerView)
    }

    private fun startQRCamera() {
        scannerView.startCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        onQrResult(p0!!.text)
//        scannerView.resumeCameraPreview(this)
    }

    private fun onQrResult(text: String?){
        if(text.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Empty Qr Code",Toast.LENGTH_SHORT).show()
        }else{
            saveToDatabase(text)
        }
    }

    private fun saveToDatabase(result: String) {
        val insertQrRowId = dbHelperI.insertQrResult(result)
        val qrResult = dbHelperI.getQrResult(insertQrRowId)
        resultDialog.show(qrResult)
    }

}