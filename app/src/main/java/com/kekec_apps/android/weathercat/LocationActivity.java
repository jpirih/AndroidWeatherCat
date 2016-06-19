package com.kekec_apps.android.weathercat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.kekec_apps.android.weathercat.model.WeatherData;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private OkHttpClient client = new OkHttpClient();
    private WeatherData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        googleApiClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(LocationServices.API)
                .build();

    }
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(requestCode == RESULT_OK){
                    googleApiClient.connect();
                }else {
                    finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // nothing to do -> have a nice day
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()){
            try {
                connectionResult.startResolutionForResult(this, 1);
            }catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "Could not retrieve location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        makeRequest(location);
    }

    @SuppressWarnings("MissingPermission") private void getLastLocation() {
        if (!googleApiClient.isConnected()) {
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else if (checkPermission()) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation != null && lastLocation.getTime() > System.currentTimeMillis() - 15 * 60 * 1000) {
                makeRequest(lastLocation);
            } else {
                LocationRequest locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10 * 1000)
                        .setFastestInterval(1 * 1000)
                        .setNumUpdates(1);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        }
    }

    private boolean checkPermission(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return false;
        }else {
            return true;
        }
    }

    private void makeRequest(Location location){
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.openweathermap.org")
                .addPathSegment("/data/2.5/weather")
                .addQueryParameter("lat", String.valueOf(location.getLatitude()))
                .addQueryParameter("lon", String.valueOf(location.getLongitude()))
                .addQueryParameter("units", "metric")
                .addQueryParameter("appid", "425d08d39ad4a87c17bcb351795ba4c3")
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Gson gson = new Gson();
                final WeatherData data = gson.fromJson(response.body().string(), WeatherData.class);
                if(data.getId() != 0){
                    LocationActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showData(data);
                        }
                    });
                }
            }
        });
    }
    // prikaz podatkov o temperaturi na trenutni lokaciji! :)
    private void showData(WeatherData data){
        TextView temp = (TextView) findViewById(R.id.temperatue);
        temp.setText(getString(R.string.temperature, data.getMain().getTemp()));

        setTitle(data.getName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                    getLastLocation();
                }else {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
