package com.prendergast.ben.darkskynet;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Forecast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prendergast.ben.darkskynet.MainActivity.SHARED_PREFERENCES;
import static com.prendergast.ben.darkskynet.MainActivity.ZIP_PREFERENCE;

/**
 * Created by doubl on 9/3/2017.
 */

public class WeatherUpdateService extends IntentService {

    public WeatherUpdateService() {
        super(WeatherUpdateService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("WeatherUpdateService", "onHandleIntent");
        String currentZip = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getString(ZIP_PREFERENCE, "");

        if(TextUtils.isEmpty(currentZip)) {
            return;
        }

        requestWeatherForLocation(41.044331, -73.526235);
//        try {
//            Geocoder geocoder = new Geocoder(this);
//            List<Address> addressList = geocoder.getFromLocationName(currentZip, 1);
//            if(addressList.size() != 1) {
//                return;
//            }
//
//            Address address = addressList.get(0);
//            requestWeatherForLocation(address.getLatitude(), address.getLongitude());
//        } catch (IOException e) {
//            e.printStackTrace();
//            requestWeatherForLocation(41.044331, -73.526235);
//        }

    }

    private void requestWeatherForLocation(double latitude, double longitude) {
        ForecastConfiguration configuration = new ForecastConfiguration.Builder("8c95278136479ef8e8171b7b0caef6e4")
                .setCacheDirectory(getCacheDir())
                .build();
        ForecastClient.create(configuration);

        ForecastClient.getInstance()
                .getForecast(latitude, longitude, new Callback<Forecast>() {

                    @Override
                    public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {

                        try {
                            FileReader.writeForecast(WeatherUpdateService.this, response.body());
                            LocalBroadcastManager.getInstance(WeatherUpdateService.this).sendBroadcast(new Intent(MainActivity.ACTION_DATA_UPDATED));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startAlarmForService(WeatherUpdateService.this, true);
                    }

                    @Override
                    public void onFailure(Call<Forecast> forecastCall, Throwable t) {
                        Log.e(WeatherUpdateService.class.getSimpleName(), "requestWeatherForLocation: onFailure", t);
                    }
                });
    }

    public static void startAlarmForService(Context context, boolean forceRefresh) {
        PendingIntent pendingIntent;
        if(forceRefresh) {
            pendingIntent = PendingIntent.getService(context, 0, new Intent(context, WeatherUpdateService.class), PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            pendingIntent = PendingIntent.getService(context, 0, new Intent(context, WeatherUpdateService.class), PendingIntent.FLAG_NO_CREATE);
        }
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);


    }
}
