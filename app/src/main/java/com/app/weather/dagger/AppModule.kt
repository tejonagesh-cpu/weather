package com.app.weather.dagger

import com.app.weather.BuildConfig
import com.app.weather.MyApplication
import com.app.weather.utils.CommonUtils
import com.app.weather.utils.NoConnectionInterceptor
import com.app.weather.webservice.ApiServiceInterface
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(var myApplication: MyApplication) {

    // Web service
    // Pref
    // Database
    // will be inject here for object creation

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        gsonBuilder.setLenient()
        gsonBuilder.setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
        return gsonBuilder.create()
    }

    companion object {
        private var logLevel =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpLogLevel = logLevel
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = okHttpLogLevel
        val noConnectionInterceptor = NoConnectionInterceptor(myApplication)
        return OkHttpClient.Builder()
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
            .addInterceptor(loggingInterceptor)
            .addInterceptor(noConnectionInterceptor)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    internal fun provideAppAPI(gson: Gson, okHttpClient: OkHttpClient): ApiServiceInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiServiceInterface::class.java)
    }

}