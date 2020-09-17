package com.app.weather.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.*


@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String {
    return Settings.Secure.getString(
        this.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    block(intent)
    startActivity(intent)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)



fun Activity.setActivityResultOk(options: Bundle? = null) {
    val intent = Intent()
    intent.putExtra("detail", options)
    this.setResult(RESULT_OK, intent)
}

fun Activity.finishActivityResultOk() {
    val intent = Intent()
    this.setResult(RESULT_OK, intent)
    finish()
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(this, text, duration)
    val view = toast.view
    view.setBackgroundColor(Color.BLACK)

    val txtView = (view as LinearLayout).findViewById<TextView>(android.R.id.message)
    txtView.setTextColor(Color.WHITE)
    txtView.gravity = Gravity.CENTER
    toast.setGravity(Gravity.CENTER,0,0)

    toast.show()
}

fun Context.showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val toast = Toast.makeText(this, getString(resId), duration)
    val view = toast.view
    view.setBackgroundColor(Color.BLACK)

    val txtView = (view as LinearLayout).findViewById<TextView>(android.R.id.message)
    txtView.setTextColor(Color.WHITE)
    txtView.gravity = Gravity.CENTER
    toast.setGravity(Gravity.CENTER,0,0)
    toast.show()
}