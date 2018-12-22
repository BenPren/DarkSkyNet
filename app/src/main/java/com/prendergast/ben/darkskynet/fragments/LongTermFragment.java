package com.prendergast.ben.darkskynet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import com.prendergast.ben.darkskynet.R;
import com.prendergast.ben.darkskynet.model.DetailsViewHolder;
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
            ExpandableListView listView = view.findViewById(R.id.expandableForecastList);
            listView.setAdapter(new DataPointAdapter(dataPoints));
        }
    }

    private static class DataPointAdapter extends BaseExpandableListAdapter {

        List<DataPoint> dataPointList;

        DataPointAdapter(@NonNull List<DataPoint> list) {
            dataPointList = list;
        }

        @Override
        public int getGroupCount() {
            return dataPointList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return 1;
        }

        @Override
        public Object getGroup(int i) {
            return dataPointList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return dataPointList.get(i);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i << 16 + i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_expandable_forecast, viewGroup, false);
                view.setTag(new ForecastViewHolder(view, DateFormat.getDateInstance(DateFormat.DEFAULT)));
            }

            ForecastViewHolder holder = (ForecastViewHolder)view.getTag();
            holder.updateViews(viewGroup.getContext(), dataPointList.get(i));
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_forecast_details, viewGroup, false);
                view.setTag(new DetailsViewHolder(view));
            }

            DetailsViewHolder holder = (DetailsViewHolder)view.getTag();
            holder.updateViews(viewGroup.getContext(), dataPointList.get(i));
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }

    }

}
