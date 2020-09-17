package com.app.weather.webservice

import com.app.weather.model.WeatherModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*


interface ApiServiceInterface {


    @GET("data/2.5/onecall")
    fun accessWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String
    ): Observable<Response<WeatherModel>>


}