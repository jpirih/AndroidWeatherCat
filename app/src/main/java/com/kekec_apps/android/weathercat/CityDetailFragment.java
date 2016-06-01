package com.kekec_apps.android.weathercat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;

/**
 * Created by Janko on 1.6.2016.
 */
public class CityDetailFragment extends Fragment
{
    public static final String EXTRA_WEATHER_DATA = "weather_data";
    private WeatherData weatherData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle extras = getArguments();
        weatherData = extras.getParcelable(EXTRA_WEATHER_DATA);

        getActivity().setTitle(weatherData.getName());

        TextView temView = (TextView) view.findViewById(R.id.temperatue);
        temView.setText(getString(R.string.temperature, weatherData.getMain().getTemp()));
    }
}
