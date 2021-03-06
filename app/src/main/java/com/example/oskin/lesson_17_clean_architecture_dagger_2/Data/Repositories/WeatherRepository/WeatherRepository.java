package com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.Repositories.WeatherRepository;

import android.content.Context;

import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.DB.DatabaseManager;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.Entity.DTO.LastRequestInfo;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.Entity.WeatherModel.WeatherModel;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.SharedPreferences.WeatherPreferences.SharedPrefManager;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Data.Web.ApiMapper;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Domain.Entity.DTO.ForecastDTOOutput;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Domain.Entity.DTO.UserPreferences;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Domain.Interactors.Interfaces.Callbacks.GetSelectedDayCallback;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Domain.Interactors.Interfaces.Callbacks.GetForecastCallback;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Domain.Interactors.Interfaces.DIP.IWeatherRepository;
import com.example.oskin.lesson_17_clean_architecture_dagger_2.Domain.Interactors.Interfaces.Callbacks.SetSelectedDayCallback;

public class WeatherRepository implements IWeatherRepository {

    private Context mContext;

    private ApiMapper mMapper;
    private DatabaseManager mDBManager;
    private ISharedPrefManager mPrefManager;

    //TODO Interface interaction
    private WeatherModel mWeatherModelResponse;
    private ForecastDTOOutput mForecastDTOOutput;
    private UserPreferences mUserPreferences;
    private WeatherMapper mWeatherMapper;
    private GetForecastCallback mLoadCallback;


    public WeatherRepository(Context context,
                             ApiMapper apiMapper,
                             DatabaseManager databaseManager,
                             SharedPrefManager sharedPrefManager,
                             WeatherMapper weatherMapper) {
        mWeatherMapper = weatherMapper;
        mContext = context;
        mMapper = apiMapper;
        mDBManager = databaseManager;
        mPrefManager = sharedPrefManager;
    }

    @Override
    public void loadWeatherForecast(GetForecastCallback callback) {
        mLoadCallback = callback;
        mUserPreferences = mPrefManager.getSharedPrefInDTO();

        /**
         * Проверка на необходмость загрузки из интернета при устаревании данных
         */
        if (isDataRelevant()) {
            mWeatherModelResponse = loadFromDB(mUserPreferences);
            mForecastDTOOutput = mWeatherMapper.getDTOFromPOJO(mWeatherModelResponse, mUserPreferences);
            mLoadCallback.onResponse(mForecastDTOOutput);
            return;
        }

        mWeatherModelResponse = loadFromWeb(mUserPreferences);

        if (mWeatherModelResponse == null) {
            mLoadCallback.onFailure();
            mWeatherModelResponse = loadFromDB(mUserPreferences);
            mForecastDTOOutput = mWeatherMapper.getDTOFromPOJO(mWeatherModelResponse, mUserPreferences);
            mLoadCallback.onResponse(mForecastDTOOutput);
        } else {

            /**
             * Маппинг модели погоды и сохранение в базу данных.
             */
            mWeatherModelResponse = mWeatherMapper.getDBModelFromResponse(mWeatherModelResponse, mUserPreferences);
            mDBManager.addWeatherModel(mWeatherModelResponse);

            /**
             * Создание и сохранения объекта последнего запроса.
             */
            LastRequestInfo info = new LastRequestInfo();
            info.setLastTimeInEpoch(mWeatherModelResponse.getLocation().getLocaltimeEpoch());
            info.setLastCityName(mWeatherModelResponse.getCityName());
            info.setLastCountDays(mUserPreferences.getCountDays());
            mPrefManager.setLastRequest(info);

            /**
             * Выгрузка модели из базы данных и маппинг в DTO.
             */
            mWeatherModelResponse = loadFromDB(mUserPreferences);
            mForecastDTOOutput = mWeatherMapper.getDTOFromPOJO(mWeatherModelResponse, mUserPreferences);
            mLoadCallback.onResponse(mForecastDTOOutput);
        }

    }

    @Override
    public void setSelectedDay(ForecastDTOOutput.Day day, SetSelectedDayCallback callback) {
        mPrefManager.setSelectedDay(day);
        callback.onResponse();
    }

    @Override
    public void getSelectedDay(GetSelectedDayCallback callback) {
        callback.onResponse(mPrefManager.getSelectedDay());
    }

    private WeatherModel loadFromDB(UserPreferences request) {
        return mDBManager.getWeatherModel(request.getCityName());
    }

    private WeatherModel loadFromWeb(UserPreferences request) {
        return mMapper.loadForecast(request.getCityCoordinatesToString(), request.getCountDays());
    }

    private boolean isDataRelevant() {

        LastRequestInfo info = mPrefManager.getLastRequest();

        String cityNameCurrent = mUserPreferences.getCityName();
        long currentTime = System.currentTimeMillis();
        int currentCountDays = mUserPreferences.getCountDays();

        String cityNameLast = info.getLastCityName();
        long lastUpdateTime = info.getLastTimeInEpoch();
        int lastCountDays = info.getLastCountDays();

        long maxDifference = 15 * 60 * 1000;
        long timeDifference = currentTime - lastUpdateTime * 1000;

        return ((cityNameCurrent.equals(cityNameLast))
                && (timeDifference <= maxDifference)
                && (currentCountDays <= lastCountDays));
    }
}
