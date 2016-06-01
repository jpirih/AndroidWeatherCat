package com.kekec_apps.android.weathercat;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kekec_apps.android.weathercat.model.WeatherData;

import java.io.IOException;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements ShowDetail
{

    private static final String TAG = "MainActivity";
    private boolean isLendscape;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLendscape = findViewById(R.id.content) != null;
    }

    @Override
    public void showDetail(WeatherData item)
    {
        if(isLendscape)
        {
            Bundle arguments = new Bundle();
            arguments.putParcelable(CityDetailFragment.EXTRA_WEATHER_DATA, item);

            Fragment fragment = new CityDetailFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
        else
        {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra(CityDetailFragment.EXTRA_WEATHER_DATA, item);
            startActivity(intent);
        }


    }
}
