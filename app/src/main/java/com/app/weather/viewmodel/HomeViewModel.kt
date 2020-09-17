package com.app.weather.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.weather.MyApplication
import com.app.weather.model.WeatherModel
import com.app.weather.utils.NoConnectionInterceptor
import com.app.weather.webservice.ApiServiceCall
import com.app.weather.webservice.ApiServiceInterface
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


open class HomeViewModel(application: Application) : BaseViewModel(application) {
    //enter home screen operations here
    fun accessWeather(
        context: Context,
        lat: String,
        lon: String,
        exclude: String,
        appid: String
    ): MutableLiveData<WeatherModel> {
        val observer = MutableLiveData<WeatherModel>()
        ApiServiceCall.accessWeather(lat,lon,exclude,appid, webApiService)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(     //subscribing results
                { response ->
                    if(response.isSuccessful)
                        observer.value = response.body()
                    else
                        observer.value = null
                },
                { error ->
                    if (error is NoConnectionInterceptor.NoConnectivityException) {
                        observer.value = null
                    } else
                        observer.value = null
                }
            )
        return observer
    }
}