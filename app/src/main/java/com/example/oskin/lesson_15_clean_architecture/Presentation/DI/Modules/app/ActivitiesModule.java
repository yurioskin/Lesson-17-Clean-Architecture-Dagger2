package com.example.oskin.lesson_15_clean_architecture.Presentation.DI.Modules.app;


import android.os.Handler;
import android.os.Looper;

import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.GetUserPreferencesInteractor;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.DIP.ISettingsRepository;
import com.example.oskin.lesson_15_clean_architecture.Presentation.DI.Qualifier.SingleThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivitiesModule {

    @Provides
    @Singleton
    public Handler provideHandler(){
        return new Handler(Looper.getMainLooper());
    }

    @Provides
    @SingleThread
    @Singleton
    public ExecutorService provideExecutorService(){
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    public GetUserPreferencesInteractor provideGetUserPreferencesInteractor(ISettingsRepository iSettingsRepository){
        return new GetUserPreferencesInteractor(iSettingsRepository);
    }

}
