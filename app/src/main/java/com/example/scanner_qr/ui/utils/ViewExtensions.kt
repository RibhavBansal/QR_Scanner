package com.example.scanner_qr.ui.utils

import android.annotation.SuppressLint
import android.view.View
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

@SuppressLint("SimpleDateFormat")
fun Calendar.toFormattedDisplay() : String{
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:a",Locale.US)
    return simpleDateFormat.format(this.time)
}