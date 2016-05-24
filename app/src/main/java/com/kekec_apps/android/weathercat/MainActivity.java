package com.kekec_apps.android.weathercat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kekec_apps.android.weathercat.model.WeatherData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CitiesAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();

    String cityIds = TextUtils.join(",", new Integer[]{
            3239318
    });

    HttpUrl url = new HttpUrl.Builder()
            .scheme("http")
            .host("api.openweathermap.org")
            .addPathSegment("/data/2.5/group")
            .addQueryParameter("id", cityIds)
            .addQueryParameter("units", "metric")
            .addQueryParameter("appid", "425d08d39ad4a87c17bcb351795ba4c3")
            .build();

    Request request = new Request.Builder().url(url).build();
  // work in progress ...

    public static final WeatherData[] DATA = new WeatherData[] {
            new WeatherData(3239318, "Mestna Občina Ljubljana", 13.91f),
            new WeatherData(3186843, "Občina Žalec", 13.91f),
            new WeatherData(3192062, "Občina Radovljica", 13.91f),
            new WeatherData(3197378, "Kranj", 13.91f),
            new WeatherData(3194351, "Novo Mesto", 15.91f),
            new WeatherData(3198647, "Jesenice", 13.91f),
            new WeatherData(3192241, "Ptuj", 17.21f),
            new WeatherData(3195506, "Maribor", 13.25f),
            new WeatherData(5128638, "New York", 17.91f),
            new WeatherData(1689973, "San Francisco", 13.91f),
            new WeatherData(3186886, "Zagreb", 13.91f),
            new WeatherData(2759794, "Amsterdam", 13.91f),
            new WeatherData(5056033, "London", 13.91f),
            new WeatherData(2950159, "Berlin", 13.91f),
            new WeatherData(2988507, "Paris", 13.91f),
            new WeatherData(292223, "Dubai", 13.91f),
            new WeatherData(1609350, "Bangkok", 13.91f),
            new WeatherData(1138958, "Kabul", 13.91f)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "OnCreate function ");
        Button refresh = (Button) findViewById(R.id.button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Click on Refresh button");
                TextView text = (TextView) findViewById(R.id.opis);
                text.setText("Deluje");
            }
        });


        // list wiew
        // svoj thread za branje api-ja

        adapter = new CitiesAdapter(this);
        adapter.setItems(DATA);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherData item = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra(SecondActivity.EXTRA_WEATHER_DATA, item);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "3, 2, 1 GO - Started successfully :)");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.v(TAG, "Application is onPause now ... Take a deep breath ..");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Application is now powered off");
    }
}
