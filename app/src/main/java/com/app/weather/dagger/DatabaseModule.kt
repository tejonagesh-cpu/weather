package com.app.weather.dagger

import android.content.Context
import androidx.room.Room
import com.app.weather.MyApplication
import com.app.weather.db.WeatherDatabase
import com.app.weather.db.dao.WeatherDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(var myApplication: MyApplication)  {

    @DatabaseInfo
    private val mDBName = "weather.db"

    @Singleton
    @Provides
    fun provideDatabase(): WeatherDatabase {
        return Room.databaseBuilder(
            myApplication,
            WeatherDatabase::class.java,
            mDBName
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return mDBName
    }

    @Singleton
    @Provides
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao {
        return db.weatherDao()
    }
}