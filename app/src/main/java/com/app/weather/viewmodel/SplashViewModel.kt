package com.app.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.weather.MyApplication
import com.app.weather.activity.HomeActivity
import com.app.weather.extension.launchActivity
import com.app.weather.webservice.ApiServiceInterface
import com.google.gson.Gson
import java.util.logging.Handler
import javax.inject.Inject


open class SplashViewModel(application: Application) : BaseViewModel(application) {
    //enter splash screen operations here
}