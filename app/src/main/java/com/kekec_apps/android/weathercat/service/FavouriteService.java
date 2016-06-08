package com.kekec_apps.android.weathercat.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;

import com.google.gson.Gson;
import com.kekec_apps.android.weathercat.CityDetailFragment;
import com.kekec_apps.android.weathercat.R;
import com.kekec_apps.android.weathercat.SecondActivity;
import com.kekec_apps.android.weathercat.model.WeatherData;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Janko on 8. 06. 2016.
 */
public class FavouriteService extends IntentService
{
    private SharedPreferences preferences;
    private OkHttpClient client = new OkHttpClient();

    // konstruktor
    public FavouriteService()
    {
        super("FavoriteCity");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        preferences = getSharedPreferences("weathercat", Context.MODE_PRIVATE);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        long cityId = preferences.getLong("cities", -1);

        if (cityId != -1)
        {
            // vremenski podatki iz api-ja
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host("api.openweathermap.org")
                    .addPathSegment("/data/2.5/weather")
                    .addQueryParameter("id", String.valueOf(cityId))
                    .addQueryParameter("units", "metric")
                    .addQueryParameter("appid", "425d08d39ad4a87c17bcb351795ba4c3")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try
            {
                Response response = client.newCall(request).execute();
                if(!response.isSuccessful()) throw new IOException("Unexpectred code " + response);
                Gson gson = new Gson();
                WeatherData data = gson.fromJson(response.body().string(), WeatherData.class);

                // notification - prikaz podatkov
                NotificationManager notificationManager  = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                String title = String.format("Vreme v kraju %s", data.getName());
                Notification.Builder builder = new Notification.Builder(this)
                        .setContentTitle(title)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                        .setAutoCancel(true);

                Notification.BigTextStyle style = new Notification.BigTextStyle(builder)
                        .bigText(String.format("V kraju %s je trenutno %.2f °C", data.getName(), data.getMain().getTemp()))
                        .setSummaryText(String.format("%.2f °C", data.getMain().getTemp()))
                        .setBigContentTitle(title);
                builder.setStyle(style);

                // poganja intent da ko klinem na notification odpre city details
                Intent intent1 = new Intent(this, SecondActivity.class);
                intent1.putExtra(CityDetailFragment.EXTRA_WEATHER_DATA, data);
                PendingIntent activity = PendingIntent.getActivity(this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(activity);

                notificationManager.notify(1, builder.build());



            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }

    }
}
