package com.prendergast.ben.darkskynet.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.zetterstrom.com.forecast.models.DataPoint;

import com.prendergast.ben.darkskynet.R;

import java.text.DateFormat;

/**
 * Created by doubl on 9/5/2017.
 */

public class ForecastViewHolder {

    private TextView timeText;
    private TextView humidityText;
    private TextView temperatureText;
    private TextView precipitationText;
    private TextView summaryText;

    private DateFormat dateFormat;

    public ForecastViewHolder(View view, DateFormat dateFormat) {
        this.dateFormat = dateFormat;

        timeText = view.findViewById(R.id.timeText);
        humidityText = view.findViewById(R.id.humidityText);
        temperatureText = view.findViewById(R.id.temperatureText);
        precipitationText = view.findViewById(R.id.precipitationText);
        summaryText = view.findViewById(R.id.summaryText);

    }

    public void updateViews(Context context, @NonNull DataPoint dataPoint) {
        timeText.setText(dateFormat.format(dataPoint.getTime()));
        humidityText.setText(context.getString(R.string.humidity, dataPoint.getHumidity() * 100));
        summaryText.setText(dataPoint.getSummary());
        temperatureText.setText(context.getString(R.string.temperature, dataPoint.getTemperature()));
        precipitationText.setText(context.getString(R.string.precipitation, dataPoint.getPrecipProbability() * 100));

    }

}
