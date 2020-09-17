package com.app.weather.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.weather.db.dao.WeatherDao
import com.app.weather.db.entity.Weather
import com.app.weather.db.entity.WeatherDetails

@Database(entities = [Weather::class, WeatherDetails::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}