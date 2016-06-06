package com.kekec_apps.android.weathercat;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kekec_apps.android.weathercat.db.MySQLiteHeper;
import com.kekec_apps.android.weathercat.model.Cities;
import com.kekec_apps.android.weathercat.model.WeatherData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Janko on 31.5.2016.
 */
public class CityListFragment extends Fragment
{
    private final String TAG ="CityListFragment";
    private CitiesAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();
    private ShowDetail detailInterface;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof ShowDetail)
        {
            detailInterface = (ShowDetail) context;
        }
        else
        {
            throw new  RuntimeException("Parent activity should implement ShowDetail");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // list wiew
        // svoj thread za branje api-ja

        adapter = new CitiesAdapter(getActivity());

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WeatherData item = adapter.getItem(position);
                detailInterface.showDetail(item);
            }
        });
        new LoadTask(getContext()).execute();


    }



    private class LoadTask extends AsyncTask<Void, Void, Cities> {
        private final Context context;

        public LoadTask(Context context) {
            this.context = context;
        }

        @Override
        protected Cities doInBackground(Void... params) {
            MySQLiteHeper mySQLiteHeper = new MySQLiteHeper(context);

            Cursor cursor = mySQLiteHeper
                    .getReadableDatabase()
                    .query("cities", new String[]{"city_id"}, null, null, null, null, "_id ASC");

            List<Long> cityIds = new ArrayList<>();

            int city_id_index = cursor.getColumnIndex("city_id");
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                cityIds.add(cursor.getLong(city_id_index));
                cursor.moveToNext();
            }
            String cityNR = TextUtils.join(",", cityIds);

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host("api.openweathermap.org")
                    .addPathSegment("/data/2.5/group")
                    .addQueryParameter("id", cityNR)
                    .addQueryParameter("units", "metric")
                    .addQueryParameter("appid", "425d08d39ad4a87c17bcb351795ba4c3")
                    .build();

            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Gson gson = new Gson();
                return gson.fromJson(response.body().string(), Cities.class);


            } catch (IOException e) {
                return null;

            }
        }

        @Override
        protected void onPostExecute(Cities cities) {
            if (cities != null) {
                adapter.setData(cities.getList());
            } else {
                adapter.setData(null);
            }
        }
    }
}
