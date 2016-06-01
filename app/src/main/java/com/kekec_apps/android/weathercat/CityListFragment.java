package com.kekec_apps.android.weathercat;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import com.kekec_apps.android.weathercat.model.Cities;
import com.kekec_apps.android.weathercat.model.WeatherData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

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
        makeNetworkRquest();
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



    }

    // funkcija za povezavo do povezavo do openWeather appi-ja  OKHTTP client
    private void makeNetworkRquest()
    {
        String cityIds = TextUtils.join(",", new Integer[]{
                3239318,3199523,3195281,3203611,3195506,3197378,
                3196359,3199171,3202781,3201730,3192682,3190536,3194351,3189075,3190717,3194452,
                3197753,3194648,3197147,3198647,3186906,3193341,3189038,3196681,3203925,3188915,
                3192241,3192673,3203412,3197943,3199131,3193299,3186450,3186607,3186844,3187125,3187214,
                3187448,3187690,3188684,3188688,3188886,3190219,3190311,3190530,3190534,3190712,3190945,
                3190950,3191029,3191044,3191059,3191062,3191063,3191401,3191580,3191685,3191845,3192021,
                3192063,3192121,3192139,3192144,3192165,3192484,3192762,3193011,3193965,3194622,3194792,
                3195162,3195202,3195214,3195250,3196165,3196307,3196425,3196560,3196652,3196682,3196760,
                3198365,3199162,3199297,3200197,3200385,3201253,3202333,3202459,3202709,3203338,3203677,
                3204303,3204854,3216508,3217862,3218907,3220262,3339120,3343512,3343518
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
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Gson gson = new Gson();
                final Cities cities = gson.fromJson(response.body().string(), Cities.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(cities.getList());
                    }
                });
            }
        });

    }


}
