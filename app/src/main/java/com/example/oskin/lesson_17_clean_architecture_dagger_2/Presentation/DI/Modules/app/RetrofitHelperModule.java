package com.example.oskin.lesson_17_clean_architecture_dagger_2.Presentation.DI.Modules.app;

import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.Web.ApiMapper;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.Web.WeatherWebService;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Presentation.DI.Qualifier.WeatherApiKey;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitHelperModule {

    private static final String VERSION_API = "v1/";
    private static final String BASE_URL = "https://api.apixu.com/" + VERSION_API;

    @Provides
    @Singleton
    ApiMapper provideApiMapper(WeatherWebService weatherWebService, @WeatherApiKey String weatherApiKey){
        return new ApiMapper(weatherWebService, weatherApiKey);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient client){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    WeatherWebService provideWeatherWebService(Retrofit retrofit){
        return retrofit.create(WeatherWebService.class);
    }

    @Singleton
    @Provides
    @WeatherApiKey
    String provideApiKey() {
        return "66d755c1072e47dcab4161954190202";
    }

    @Singleton
    @Provides
    String kek(){
        return "kek";
    }
}
