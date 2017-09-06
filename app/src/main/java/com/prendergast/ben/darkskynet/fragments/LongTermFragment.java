package com.prendergast.ben.darkskynet.fragments;

import android.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import com.prendergast.ben.darkskynet.R;
import com.prendergast.ben.darkskynet.model.ViewHolder;
import com.prendergast.ben.darkskynet.model.WeatherModel;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by doubl on 9/2/2017.
 */

public class LongTermFragment extends Fragment implements Observer<Forecast> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_long_term, container, false);

        WeatherModel model = ViewModelProviders.of((AppCompatActivity)getActivity()).get(WeatherModel.class);
        setAdapter(view, model.weather.getValue());
        model.weather.observe((LifecycleOwner) getActivity(), this);

        return view;
    }

    @Override
    public void onChanged(@Nullable Forecast weatherResponse) {
        setAdapter(getView(), weatherResponse);
    }

    private void setAdapter(@Nullable View view, @Nullable Forecast weatherResponse) {
        if(view == null || weatherResponse == null || weatherResponse.getDaily() == null ||
                weatherResponse.getDaily().getDataPoints() == null) {
            return;
        }

        List<DataPoint> dataPoints = weatherResponse.getDaily().getDataPoints();
        if(dataPoints.size() > 7) {
            dataPoints = dataPoints.subList(0, 7);
        }

        if(dataPoints.size() > 0) {
            ListView listView = view.findViewById(R.id.forecastList);
            listView.setAdapter(new DataPointAdapter(dataPoints));
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//    }
//
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
                view.setTag(new ViewHolder(view, DateFormat.getDateInstance(DateFormat.DEFAULT)));
            }

            ViewHolder holder = (ViewHolder)view.getTag();
            holder.updateViews(viewGroup.getContext(), dataPointList.get(i));
            return view;
        }
    }

}
