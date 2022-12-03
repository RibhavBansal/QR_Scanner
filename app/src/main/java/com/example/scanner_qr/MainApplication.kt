package com.example.scanner_qr

import android.app.Application
import android.os.Bundle
import com.facebook.stetho.Stetho

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}