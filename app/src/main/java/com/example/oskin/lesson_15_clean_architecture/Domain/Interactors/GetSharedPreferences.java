package com.example.oskin.lesson_15_clean_architecture.Domain.Interactors;

import com.example.oskin.lesson_15_clean_architecture.Domain.Entity.DTO.SharedPrefDTO;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.GetSharedPrefCallback;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.IRepository;

public class GetSharedPreferences implements GetSharedPrefCallback {

    private IRepository mRepository;
    private GetSharedPrefCallback mPrefCallback;
    private SharedPrefDTO mDTOOutput;

    public GetSharedPreferences(IRepository repository) {
        mRepository = repository;
    }

    public void getSharedPref(GetSharedPrefCallback prefCallback){
        mPrefCallback = prefCallback;
        mRepository.getSharedPreferences(this);
    }

    @Override
    public void onResponse(SharedPrefDTO sharedPrefDTO) {
        mDTOOutput = sharedPrefDTO;
        /**
         * Some interactor logic.
         */
        mPrefCallback.onResponse(mDTOOutput);
    }
}
