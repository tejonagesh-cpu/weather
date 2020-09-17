package com.app.weather.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.weather.db.entity.Weather
import com.app.weather.db.entity.WeatherDetails
import com.app.weather.db.entity.WeatherOutputModel

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: Weather)

    @Insert
    fun insert(weatherDetails: List<WeatherDetails>)

    @Query("SELECT * FROM weather_table ")
    fun getAllWeather(): List<Weather>

    @Query("DELETE FROM weather_table WHERE city LIKE :city")
    fun removeWeather(city : String)

    @Query("DELETE FROM weather_details_table WHERE city_name LIKE :city")
    fun removeWeatherDetails(city : String)

    @Query("SELECT * FROM weather_table,weather_details_table where weather_table.city LIKE :city AND weather_details_table.city_name LIKE :city")
    fun getWeather(city : String): WeatherOutputModel?

}