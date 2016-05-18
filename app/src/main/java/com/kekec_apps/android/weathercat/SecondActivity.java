package com.kekec_apps.android.weathercat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    public static final String EXTRA_WEATHER_DATA  = "vremenski podatek";
    private  WeatherData weatherData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "App is onCreate NOW");
        setContentView(R.layout.activity_second);
        Bundle extras = getIntent().getExtras();
        weatherData = extras.getParcelable(EXTRA_WEATHER_DATA);
        setTitle(weatherData.getName());

        Button backMain = (Button) findViewById(R.id.backMain);
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"Click on Back to Main button");
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        TextView tempViev = (TextView) findViewById(R.id.temperatue);
        tempViev.setText(getString(R.string.temperature, weatherData.getMain().getTemp()));


    }


}
