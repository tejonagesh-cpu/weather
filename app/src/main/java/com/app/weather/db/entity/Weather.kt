package com.app.weather.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "weather_table")
class Weather {
    @PrimaryKey
    var city : String = ""
    var temp : String = ""
    var dt : String = ""
    var weather : String = ""
}

@Entity(tableName = "weather_details_table")
data class WeatherDetails(
    var city_name : String,
    var temp : String,
    var dt : String,
    var weather : String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

class WeatherOutputModel {
    @Embedded
    var weather: Weather ?=null

    @Relation(
        parentColumn = "city",
        entityColumn = "city_name"
    )
    var days: List<WeatherDetails>?=null
}