package com.example.oskin.lesson_15_clean_architecture.Data.SharedPreferences;

import android.content.Context;

import com.example.oskin.lesson_15_clean_architecture.Data.Repositories.ISharedPrefManager;
import com.example.oskin.lesson_15_clean_architecture.Domain.Entity.DTO.ForecastDTOInput;
import com.example.oskin.lesson_15_clean_architecture.Domain.Entity.DTO.SharedPrefDTO;

public class SharedPrefManager implements ISharedPrefManager {
    private SettingsManager mSettingsManager;
    private LastTimeLoadManager mLastTimeLoadManager;
    private Context mContext;

    public SharedPrefManager(Context context) {
        mContext = context;
        mSettingsManager = new SettingsManager();
        mLastTimeLoadManager = new LastTimeLoadManager();
    }

    @Override
    public void loadSharedPref(SharedPrefDTO sharedPrefDTO) {
        mSettingsManager.loadSharedPref(mContext, sharedPrefDTO);
    }

    @Override
    public SharedPrefDTO getSharedPrefInDTO() {
        return mSettingsManager.getSharedPrefInDTO(mContext);
    }

    @Override
    public void setLastTimeLoadInEpoch(long timeInEpoch) {
        mLastTimeLoadManager.setLastTimeLoad(timeInEpoch, mContext);
    }

    @Override
    public long getLastTimeLoadInEpoch() {
        return mLastTimeLoadManager.getLastTimeLoad(mContext);
    }

    @Override
    public ForecastDTOInput getForecastDTOInput() {
        return mSettingsManager.getForecastDTOInput(mContext);
    }
}
