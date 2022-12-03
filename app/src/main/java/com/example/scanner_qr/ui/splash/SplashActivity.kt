package com.example.scanner_qr.ui.splash
//package com.androchef.qrcodescanner.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.scanner_qr.ui.main.MainActivity
import com.example.scanner_qr.R

//import com.androchef.qrcodescanner.ui.mainactivity.MainActivity
//import com.androchef.qrcodescanner.R

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            checkForPermission()
        }, 2000)
    }

    private fun checkForPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            gotToMainActivity()
        } else {
            requestPermission()
        }
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun gotToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotToMainActivity()
            } else if (isPermanentlyDenied()) {
                showGoToAppSettingsDialog()
            } else
                requestPermission()
        }
    }

    private fun isPermanentlyDenied(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).not()
        } else {
            return false
        }
    }


    private fun showGoToAppSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("grant_permissions")
            .setMessage("we_need_permission")
            .setPositiveButton("grant") { _, _ ->
                goToAppSettings()
            }
            .setNegativeButton("cancel") { _, _ ->
                Toast.makeText(this,"we need permission for start this application",Toast.LENGTH_SHORT).show()
                finish()
            }.show()
    }

    private fun goToAppSettings() {
        val intent = Intent(
            ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        checkForPermission()
    }
}