package com.app.weather.base

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.app.weather.MyApplication
import com.app.weather.db.WeatherDatabase
import com.app.weather.model.LatLng
import java.util.*
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {

    lateinit var myApplication: MyApplication

    @Inject
    lateinit var weatherDatabase: WeatherDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = applicationContext as MyApplication
        myApplication.appComponent.inject(this)
    }

    fun getAddressFromLatLng(latitude: Double, longitude: Double): String {
        return try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
            addresses.get(0).getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getLatLngFromCity(latlon : LatLng): String {
        var city = ""
        return try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(latlon.lat, latlon.lng,1)
            city = addresses[0].locality
            city
        } catch (e: Exception) {
            e.printStackTrace()
            city
        }
    }

    fun showAlert(title: String) {
        AlertDialog.Builder(this@BaseActivity).setMessage(title).setPositiveButton("Ok",object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
            }
        }).show()
    }

    open fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}