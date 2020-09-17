package com.app.weather.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.weather.BuildConfig
import com.app.weather.R
import com.app.weather.adapter.WeatherAdapter
import com.app.weather.base.BaseActivity
import com.app.weather.db.Executor.ioThread
import com.app.weather.db.dao.WeatherDao
import com.app.weather.db.entity.Weather
import com.app.weather.db.entity.WeatherDetails
import com.app.weather.extension.hide
import com.app.weather.extension.show
import com.app.weather.model.LatLng
import com.app.weather.model.WeatherModel
import com.app.weather.utils.CommonUtils
import com.app.weather.utils.Vars
import com.app.weather.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : BaseActivity() {

    lateinit var homeViewModel: HomeViewModel
    var latLng = LatLng(0.0, 0.0)
    var city = ""
    val permsRequestCode = 200;
    private lateinit var dao : WeatherDao
    lateinit var weatherAdapter: WeatherAdapter

    lateinit var  fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // to restrict crash on 8.1.0 custom os devices added below lines
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.decorView.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }

        attachViewModel()
        initView()
     }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refer -> {
                Toast.makeText(applicationContext, "Refer wheather", Toast.LENGTH_LONG).show()
                accessCity(latLng)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        toolbar.title = getString(R.string.home)
        setSupportActionBar(toolbar)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        requestPermissions(permissions,permsRequestCode)

        fetchCurrentLOcation();


        rvWeather.layoutManager = LinearLayoutManager(this@HomeActivity)
        weatherAdapter = WeatherAdapter(this@HomeActivity)
        rvWeather.adapter = weatherAdapter
        tvHint.show()
        clResult.hide()
        dao = weatherDatabase.weatherDao()
    }





    private fun accessCity(latLng: LatLng) {
        hideKeyboard(this@HomeActivity)
        //showing progress
        llProgressBar.show()
        homeViewModel.accessWeather(
            this@HomeActivity, latLng.lat.toString(), latLng.lng.toString(),
            Vars.FILTER_HOURLY, BuildConfig.APP_KEY
        ).observe(this, Observer {
            //dismissing progress
            llProgressBar.hide()
            if (it != null) {
                //if result is success adding data into db
                addDataToDb(it)
            } else {
                //else checking from local db if its already having any values
                prepareView()
            }
        })
    }

    // formatting data as per db structure
    private fun addDataToDb(it: WeatherModel) {
        val weather = Weather()
        weather.temp = it.current.temp
        weather.dt = it.current.dt
        weather.city = city
        if (it.current.weather.isNotEmpty())
            weather.weather = it.current.weather[0].main

        val weatherDetails = ArrayList<WeatherDetails>()
        for (i in it.daily.indices) {
            var weatherType = ""
            if (it.daily[i].weather.isNotEmpty()) {
                weatherType = it.daily[i].weather[0].main
            }

            val dayDetail = WeatherDetails(
                city,
                it.daily[i].temp.day,
                it.daily[i].dt,
                weatherType
            )
            weatherDetails.add(dayDetail)
        }
        if (weatherDetails.isNotEmpty()) {
            ioThread(Runnable {
                dao.insert(weather)
                dao.removeWeatherDetails(city)
                dao.insert(weatherDetails)
                prepareView()
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun prepareView() {
        try {
            ioThread(Runnable {
                val details = dao.getWeather(city)
                runOnUiThread{
                    //updating view here
                    if (details != null) {
                        clResult.show()
                        tvHint.hide()
                        tvWeather.text = details.weather!!.weather
                        tvTemp.text = details.weather!!.temp + getString(R.string.degree_celsius)
                        weatherAdapter.setWeatherDetails(details.days!!)
                    } else {
                        clResult.hide()
                        tvHint.text = getString(R.string.no_data_available)
                        tvHint.show()
                    }
                }
            })
        } catch (e:Exception) {
            e.printStackTrace()
            clResult.hide()
            tvHint.text = getString(R.string.no_data_available)
            tvHint.show()
        }
    }

    //attach view model here
    private fun attachViewModel() {
        homeViewModel = ViewModelProvider(this@HomeActivity).get(HomeViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            200 -> if (grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

              fetchCurrentLOcation()
            }
        }


        return;
        
    }

    private fun fetchCurrentLOcation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient?.lastLocation
            ?.addOnSuccessListener { location : Location? ->
                Log.e("TAG", "initView: "+location?.latitude )

                var latLng = LatLng(location!!.latitude,location!!.longitude)

                if (CommonUtils.isInternetAvailable(this@HomeActivity)) {
                    //Threading to fetch city name  from lat lng
                    ioThread(Runnable {
                         city = getLatLngFromCity(latLng)

                        runOnUiThread{
                            if (latLng.lat == 0.0 && latLng.lng == 0.0) {
                             } else {
                                this.latLng = latLng
                                //api call
                                accessCity(latLng)
                                timerMethod()
                            }
                        }
                    })

                    toolbar.title = city
                } else {
                    prepareView()
                }


            }
    }

    private fun timerMethod() {

    val timer = Timer()
        val hourlyTask: TimerTask = object : TimerTask() {
            override fun run() {
            accessCity(latLng)
            }
        }
        timer.schedule (hourlyTask, 1000*15*60);
    }



}