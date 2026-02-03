package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.api.OpenWeatherMapApi
import com.example.data.api.OpenWeatherMapInterceptor
import com.example.data.api.OpenWeatherMapServer
import com.example.data.api.OpenWeatherMapServerConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@InstallIn(SingletonComponent::class)
@Module
internal object ApiModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Singleton
    @Provides
    fun provideOpenWeatherMapServerConfig(): OpenWeatherMapServerConfig {
        return OpenWeatherMapServer.prod
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }


    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory
            .create(gson)
    }

    @Singleton
    @Provides
    fun provideOpenWeatherMapInterceptor(): OpenWeatherMapInterceptor {
        return OpenWeatherMapInterceptor(BuildConfig.OPEN_WEATHER_MAP_API_KEY)
    }

    @Singleton
    @Provides
    fun provideOpenWeatherMapApi(
        loggingInterceptor: HttpLoggingInterceptor,
        openWeatherMapServerConfig: OpenWeatherMapServerConfig,
        converterFactory: GsonConverterFactory,
        openWeatherMapInterceptor: OpenWeatherMapInterceptor,
    ): OpenWeatherMapApi {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(openWeatherMapInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(openWeatherMapServerConfig.baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create(OpenWeatherMapApi::class.java)
    }
}