package com.prendergast.ben.darkskynet.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.zetterstrom.com.forecast.models.Forecast;

/**
 * Created by doubl on 9/4/2017.
 */
public class WeatherModel extends ViewModel {

    public MutableLiveData<Forecast> weather = new MutableLiveData<>();

}
