package com.kekec_apps.android.weathercat.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.kekec_apps.android.weathercat.service.FavouriteService;

/**
 * Created by Janko on 8. 06. 2016.
 */
public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences preferences = context.getSharedPreferences("weathercat", Context.MODE_PRIVATE);
        long cityId = preferences.getLong("cities", -1);

        if( cityId != -1)
        {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent favouriteIntent = new Intent(context, FavouriteService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 2, favouriteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 10*1000, 60*1000, pendingIntent);
        }

    }
}
