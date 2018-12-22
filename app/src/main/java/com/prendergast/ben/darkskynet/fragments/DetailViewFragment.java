package com.prendergast.ben.darkskynet.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import com.prendergast.ben.darkskynet.R;
import com.prendergast.ben.darkskynet.model.ForecastViewHolder;
import com.prendergast.ben.darkskynet.model.WeatherModel;

import java.text.DateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 *
 * Created by doubl on 9/2/2017.
 */
public class DetailViewFragment extends Fragment implements Observer<Forecast>, AdapterView.OnItemClickListener {

    @
            Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_view, container, false);

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.contentLayout, new ManageLocationFragment());
                fragmentTransaction.addToBackStack(ManageLocationFragment.class.getSimpleName());
                fragmentTransaction.commit();
            }
        });

        WeatherModel model = ViewModelProviders.of((AppCompatActivity)getActivity()).get(WeatherModel.class);
        setAdapter(model.weather.getValue());
        model.weather.observe((LifecycleOwner) getActivity(), this);
        ListView listView = view.findViewById(R.id.forecastList);
        listView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onChanged(@Nullable Forecast weatherResponse) {
        setAdapter(weatherResponse);
    }

    private void setAdapter(@Nullable Forecast weatherResponse) {
        if(weatherResponse == null || weatherResponse.getHourly() == null || weatherResponse.getHourly().getDataPoints() == null) {
            return;
        }

        List<DataPoint> dataPoints = weatherResponse.getHourly().getDataPoints();
        if(dataPoints.size() > 24) {
            dataPoints = dataPoints.subList(0, 24);
        }
        View view = getView();
        if(view != null && dataPoints.size() > 0) {
            ListView listView = view.findViewById(R.id.forecastList);
            listView.setAdapter(new DataPointAdapter(dataPoints));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contentLayout, new LongTermFragment());
        fragmentTransaction.addToBackStack(LongTermFragment.class.getSimpleName());
        fragmentTransaction.commit();
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
                view.setTag(new ForecastViewHolder(view, DateFormat.getTimeInstance(DateFormat.DEFAULT)));
            }

            ForecastViewHolder holder = (ForecastViewHolder)view.getTag();
            holder.updateViews(viewGroup.getContext(), dataPointList.get(i));
            return view;
        }
    }


}
