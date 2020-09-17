package com.app.weather.model

data class WeatherModel(
    var lat : Double,
    var lon : Double,
    var current : CurrentWeatherModel,
    var daily : ArrayList<DailyWeatherModel>
)

data class CurrentWeatherModel(
    var dt : String,
    var temp : String,
    var weather : ArrayList<WeatherDetailModel>
)

data class WeatherDetailModel(
    var main : String
)

data class DailyWeatherModel(
    var dt : String,
    var temp : TempModel,
    var weather : ArrayList<WeatherDetailModel>
)

data class TempModel(
    var day : String
)