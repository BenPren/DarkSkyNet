package com.prendergast.ben.darkskynet.model;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.zetterstrom.com.forecast.models.DataPoint;

import com.prendergast.ben.darkskynet.R;

import java.text.DateFormat;

import androidx.annotation.NonNull;

/**
 * Created by doubl on 9/5/2017.
 */

public class DetailsViewHolder {

    private TextView cloudCoverText;
    private TextView dewPointText;
    private TextView sunriseTimeText;
    private TextView sunsetTimeText;
    private TextView apparentTemperatureMinText;
    private TextView apparentTemperatureMaxText;

    private DateFormat dateFormat;

    public DetailsViewHolder(View view) {
        dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT);

        cloudCoverText = view.findViewById(R.id.cloudCoverText);
        dewPointText = view.findViewById(R.id.dewPointText);
        sunriseTimeText = view.findViewById(R.id.sunriseTimeText);
        sunsetTimeText = view.findViewById(R.id.sunsetTimeText);
        apparentTemperatureMinText = view.findViewById(R.id.apparentTemperatureMinText);
        apparentTemperatureMaxText = view.findViewById(R.id.apparentTemperatureMaxText);

    }

    public void updateViews(Context context, @NonNull DataPoint dataPoint) {

        if(dataPoint.getCloudCover() == null || dataPoint.getDewPoint() == null) {
            cloudCoverText.setVisibility(View.GONE);
            dewPointText.setVisibility(View.GONE);
        } else {
            cloudCoverText.setText(context.getString(R.string.could_cover, dataPoint.getCloudCover() * 100));
            dewPointText.setText(context.getString(R.string.dew_point, dataPoint.getDewPoint() * 100));
            cloudCoverText.setVisibility(View.VISIBLE);
            dewPointText.setVisibility(View.VISIBLE);
        }
        sunriseTimeText.setText(context.getString(R.string.sunrise, dateFormat.format(dataPoint.getSunriseTime())));
        sunsetTimeText.setText(context.getString(R.string.sunset, dateFormat.format(dataPoint.getSunsetTime())));
        apparentTemperatureMinText.setText(context.getString(R.string.could_cover, dataPoint.getApparentTemperatureMin()));
        apparentTemperatureMaxText.setText(context.getString(R.string.could_cover, dataPoint.getApparentTemperatureMax()));

    }

}
