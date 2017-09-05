package com.prendergast.ben.darkskynet.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import com.prendergast.ben.darkskynet.R;
import com.prendergast.ben.darkskynet.model.WeatherModel;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by doubl on 9/2/2017.
 */

public class DetailViewFragment extends Fragment implements Observer<Forecast> {

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_view, container, false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.contentLayout, new ManageLocationFragment());
                fragmentTransaction.addToBackStack(ManageLocationFragment.class.getSimpleName());
                fragmentTransaction.commit();
                fab.hide();
            }
        });

        WeatherModel model = ViewModelProviders.of((AppCompatActivity)getActivity()).get(WeatherModel.class);
        model.weather.observe((LifecycleOwner) getActivity(), this);
        return view;
    }


    @Override
    public void onChanged(@Nullable Forecast weatherResponse) {
        if(weatherResponse == null) {
            return;
        }

        List<DataPoint> dataPoints = weatherResponse.getHourly().getDataPoints();
        View view = getView();
        if(view != null && dataPoints != null && dataPoints.size() > 0) {
            ListView listView = view.findViewById(R.id.forecastList);
            listView.setAdapter(new DataPointAdapter(dataPoints));
        }

    }

    private static class DataPointAdapter extends BaseAdapter {

        List<DataPoint> dataPointList;

        DataPointAdapter(@NonNull List<DataPoint> list) {
            dataPointList = list;
        }

        @Override
        public int getCount() {
            return dataPointList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataPointList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_forecast, viewGroup, false);
                view.setTag(new Holder(view));
            }

            Holder holder = (Holder)view.getTag();
            holder.updateViews(viewGroup.getContext(), dataPointList.get(i));
            return view;
        }
    }

    private static class Holder {
        private TextView timeText;
        private TextView summaryText;
        private TextView temperatureText;
        private TextView precipitationText;

        private Holder(View view) {
            timeText = view.findViewById(R.id.timeText);
            summaryText = view.findViewById(R.id.summaryText);
            temperatureText = view.findViewById(R.id.temperatureText);
            precipitationText = view.findViewById(R.id.precipitationText);

        }

        private void updateViews(Context context, DataPoint dataPoint) {
            timeText.setText(DateFormat.getTimeInstance(DateFormat.DEFAULT).format(dataPoint.getTime()));
            summaryText.setText(dataPoint.getSummary());
            temperatureText.setText(context.getString(R.string.temperature, dataPoint.getTemperature()));
            precipitationText.setText(context.getString(R.string.precipitation, dataPoint.getPrecipProbability()));

        }
    }
}
