package com.example.cw11formatowanedaneteleadresowe;

import android.text.Spannable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class DataViewModel extends ViewModel {
    private final MutableLiveData<HashMap<String, Spannable>> data = new MutableLiveData<>();

    public MutableLiveData<HashMap<String, Spannable>> getSharedData(){
        return data;
    }

    public void setData(HashMap<String, Spannable> newData){
        data.setValue(newData);
    }
}
