package com.app.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.weather.MyApplication
import com.app.weather.webservice.ApiServiceInterface
import com.google.gson.Gson
import javax.inject.Inject


open class BaseViewModel(var app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var webApiService: ApiServiceInterface

    init {
        val myApplication = app as MyApplication
        myApplication.appComponent.inject(this)
    }
}