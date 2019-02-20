package com.example.oskin.lesson_15_clean_architecture.Presentation.DI.Modules.app;

import android.content.Context;

import com.example.oskin.lesson_15_clean_architecture.Data.DB.DatabaseManager;
import com.example.oskin.lesson_15_clean_architecture.Data.Repositories.SettingsRepository.SettingRepository;
import com.example.oskin.lesson_15_clean_architecture.Data.Repositories.WeatherRepository.WeatherMapper;
import com.example.oskin.lesson_15_clean_architecture.Data.Repositories.WeatherRepository.WeatherRepository;
import com.example.oskin.lesson_15_clean_architecture.Data.SharedPreferences.SettingsPreferences.UserSettingsManager;
import com.example.oskin.lesson_15_clean_architecture.Data.SharedPreferences.WeatherPreferences.SharedPrefManager;
import com.example.oskin.lesson_15_clean_architecture.Data.Web.ApiMapper;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.DIP.ISettingsRepository;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.DIP.IWeatherRepository;
import com.example.oskin.lesson_15_clean_architecture.Presentation.DI.Qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoriesModule {

    @Provides
    @Singleton
    public ISettingsRepository provideSettingRepository(
            @ApplicationContext Context context,
            UserSettingsManager userSettingsManager) {
        return new SettingRepository(context, userSettingsManager);
    }

    @Provides
    @Singleton
    public IWeatherRepository provideWeatherRepository(
            @ApplicationContext Context context,
            ApiMapper apiMapper,
            DatabaseManager databaseManager,
            SharedPrefManager sharedPrefManager,
            WeatherMapper weatherMapper) {

        return new WeatherRepository(
                context,
                apiMapper,
                databaseManager,
                sharedPrefManager,
                weatherMapper);
    }

    @Provides
    @Singleton
    public WeatherMapper provideWeatherMapper(){
        return new WeatherMapper();
    }

}
