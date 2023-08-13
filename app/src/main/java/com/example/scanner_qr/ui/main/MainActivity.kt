package com.example.scanner_qr.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.scanner_qr.R
import com.example.scanner_qr.db.database.QrResultDatabase
import com.example.scanner_qr.db.entities.QrResult
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPagerAdapter()
        setBottomNavigation()
        setViewPagerListener()

//        val qrResult =
//            QrResult(result = "Dummy", resultType = "TEXT", favourite = false, calendar = Calendar.getInstance())
//        QrResultDatabase.getAppDatabase(this)?.getQrDao()?.insertQrResult(qrResult)
    }

    private fun setBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            viewPager.currentItem = when (it.itemId) {
                R.id.qrScanMenuId -> 0
                R.id.scannedResultMenuId -> 1
                R.id.favouriteScannedMenuId -> 2
                else -> 0
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setViewPagerAdapter() {
        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
    }

    private fun setViewPagerListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.qrScanMenuId
                    1 -> R.id.scannedResultMenuId
                    2 -> R.id.favouriteScannedMenuId
                    else -> R.id.qrScanMenuId
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}