package com.mcmah113.mcmah113weatherviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WeatherDataAdapter extends ArrayAdapter<WeatherData> {
    @SuppressWarnings("All")
    public WeatherDataAdapter(Context context, List<WeatherData> weatherData) {
        super(context, 0, weatherData);
    }

    @SuppressWarnings("All")
    public View getView(int position, View row, ViewGroup parent) {
        if(row == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            row = layoutInflater.inflate(R.layout.layout_listview_item, null);
        }

        WeatherData weatherData = getItem(position);

        if(weatherData != null) {
            final ImageView imageView = row.findViewById(R.id.imageViewWeatherIcon);
            final TextView textViewTemperature = row.findViewById(R.id.textViewTemperature);
            final TextView textViewDate = row.findViewById(R.id.textViewDate);
            final TextView textViewTIme = row.findViewById(R.id.textViewTIme);
            final TextView textViewHigh = row.findViewById(R.id.textViewHigh);
            final TextView textViewLow = row.findViewById(R.id.textViewLow);
            final TextView textViewDescription = row.findViewById(R.id.textViewDescription);
            final TextView textViewHumidity = row.findViewById(R.id.textViewHumidity);

            imageView.setImageBitmap(weatherData.getWeatherIcon());
            textViewTemperature.setText(weatherData.getTemperature());
            textViewDate.setText(weatherData.getDateString());
            textViewTIme.setText(weatherData.getTime());
            textViewHigh.setText(weatherData.getTemperatureHigh());
            textViewLow.setText(weatherData.getTemperatureLow());
            textViewDescription.setText(weatherData.getWeatherDescription());
            textViewHumidity.setText(weatherData.getHumidity());
        }

        return row;
    }
}