package com.prendergast.ben.darkskynet;

import android.content.Context;
import android.zetterstrom.com.forecast.models.Forecast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by doubl on 9/3/2017.
 */

class FileReader {

    static void writeForecast(Context context, Forecast forecast) throws IOException {
        FileOutputStream fos = context.openFileOutput("forecast.obj", Context.MODE_PRIVATE);
        ObjectOutputStream writer = new ObjectOutputStream(fos);
        writer.writeObject(forecast);
        writer.flush();
        writer.close();

    }

    static Forecast readForecast(Context context) throws IOException, ClassNotFoundException, ClassCastException {
        FileInputStream fis = context.openFileInput("forecast.obj");
        ObjectInputStream reader = new ObjectInputStream(fis);
        Forecast forecast = (Forecast) reader.readObject();
        reader.close();
        return forecast;
    }

}
