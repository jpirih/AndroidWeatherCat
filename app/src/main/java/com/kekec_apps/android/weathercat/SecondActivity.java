package com.kekec_apps.android.weathercat;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = new Bundle();
        arguments.putParcelable(CityDetailFragment.EXTRA_WEATHER_DATA,
                getIntent().getExtras().getParcelable(CityDetailFragment.EXTRA_WEATHER_DATA));

        Fragment fragment = new CityDetailFragment();
        fragment.setArguments(arguments);

        getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();

    }
}
