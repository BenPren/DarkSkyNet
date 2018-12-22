package com.prendergast.ben.darkskynet.model;

import android.zetterstrom.com.forecast.models.Forecast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by doubl on 9/4/2017.
 */
public class WeatherModel extends ViewModel {

    public MutableLiveData<Forecast> weather = new MutableLiveData<>();

}
