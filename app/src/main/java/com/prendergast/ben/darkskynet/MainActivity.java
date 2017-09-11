package com.prendergast.ben.darkskynet;

import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.zetterstrom.com.forecast.models.Forecast;

import com.prendergast.ben.darkskynet.fragments.DetailViewFragment;
import com.prendergast.ben.darkskynet.model.WeatherModel;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    public static final String SHARED_PREFERENCES = "com.prendergast.ben.darkskynet";
    public static final String ZIP_PREFERENCE = "String.ZIP_PREFERENCE";
    public static final String ACTION_DATA_UPDATED = "ACTION_DATA_UPDATED";
    public static final String ACTION_DATA_ERROR = "ACTION_DATA_ERROR";

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        broadcastReceiver = new DataUpdatedReceiver();
        WeatherUpdateService.startAlarmForService(this, PendingIntent.FLAG_NO_CREATE, false);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, new DetailViewFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(ACTION_DATA_UPDATED);
        intentFilter.addAction(ACTION_DATA_ERROR);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        tryLoadData();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onPause();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    private void tryLoadData() {
        try {
            Forecast forecast = FileReader.readForecast(this);
            WeatherModel model = ViewModelProviders.of(this).get(WeatherModel.class);
            model.weather.setValue(forecast);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class DataUpdatedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()) {
                case ACTION_DATA_ERROR:
                    Snackbar.make(findViewById(R.id.contentLayout), R.string.update_error, Snackbar.LENGTH_LONG).show();
                    break;
                case ACTION_DATA_UPDATED:
                    tryLoadData();
                    break;
            }
        }
    }

}
