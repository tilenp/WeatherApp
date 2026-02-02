package com.example.weatherapp.screens.forecast.di

import com.example.weatherapp.screens.EventAggregator
import com.example.weatherapp.utils.DispatcherProvider
import com.example.weatherapp.utils.RuntimeDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object CoreModule {

    @Singleton
    @Provides
    fun provideEventAggregator(): EventAggregator {
        return EventAggregator()
    }

    @Singleton
    @Provides
    fun provideLocale(): Locale {
        return Locale.getDefault()
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return RuntimeDispatcherProvider()
    }
}