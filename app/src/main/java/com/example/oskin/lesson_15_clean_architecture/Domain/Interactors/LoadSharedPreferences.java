package com.example.oskin.lesson_15_clean_architecture.Domain.Interactors;

import com.example.oskin.lesson_15_clean_architecture.Domain.Entity.DTO.SharedPrefDTO;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.IRepository;
import com.example.oskin.lesson_15_clean_architecture.Domain.Interactors.Interfaces.LoadSharedPrefCallback;

public class LoadSharedPreferences {

    private IRepository mRepository;
    private SharedPrefDTO mDTOOutput;

    public LoadSharedPreferences(IRepository repository) {
        mRepository = repository;
    }

    public void loadSharedPreferences(SharedPrefDTO prefDTO, LoadSharedPrefCallback callback){
        mRepository.loadSharedPreferences(prefDTO, callback);
    }
}
