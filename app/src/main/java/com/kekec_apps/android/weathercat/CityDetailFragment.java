package com.kekec_apps.android.weathercat;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;
import com.kekec_apps.android.weathercat.service.FavouriteService;

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

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.notify);

        final SharedPreferences preferences = getContext().getSharedPreferences("weathercat", Context.MODE_PRIVATE);
        long selectedId = preferences.getLong("cities", -1);
        checkBox.setChecked(selectedId == weatherData.getId());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SharedPreferences prefrences = getContext().getSharedPreferences("weathercat", Context.MODE_PRIVATE);
                // alarm manager
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), FavouriteService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if(isChecked)
                {
                    preferences.edit()
                            .putLong("cities", weatherData.getId())
                            .apply();

                    alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 10*1000, 60*1000, pendingIntent);
                }
                else
                {
                    preferences.edit().remove("cities").apply();
                    alarmManager.cancel(pendingIntent);
                }
            }
        });
    }
}
