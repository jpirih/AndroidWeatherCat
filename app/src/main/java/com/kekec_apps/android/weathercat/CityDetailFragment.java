package com.kekec_apps.android.weathercat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;
import com.kekec_apps.android.weathercat.service.FavouriteService;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.URI;


/**
 * Created by Janko on 1.6.2016.
 */
public class CityDetailFragment extends Fragment
{
    public static final String EXTRA_WEATHER_DATA = "weather_data";
    private WeatherData weatherData;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        Bundle extras = getArguments();
        weatherData = extras.getParcelable(EXTRA_WEATHER_DATA);

        getActivity().setTitle(weatherData.getName());

        imageView = (ImageView) view.findViewById(R.id.city_image);
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
        setImage();

    }

    // ikon fotoaparat menu gumb za slikanje

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_add_photo:
                if(checkPremission())
                {
                    takePicture();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if(resultCode == Activity.RESULT_OK)
                {
                    setImage();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    takePicture();
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean checkPremission()
    {
        if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            // uporabnika vprasa za dovoljenje za pisanje na external storage
            requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2 );
            return false;
        }
        else
        {
            return true;
        }
    }
    private void setImage()
    {
        File file = getFile();
        if(file.exists())
        {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
        else
        {
            imageView.setImageBitmap(null);
        }
    }

    private void takePicture()
    {
        // inplicitni intent  - zahteva akcijo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getContext().getPackageManager()) != null)
        {
            try
            {
                File photoFile = createImageFile();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, 1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private File getFile()
    {
        // pot do direktorija za slike
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, "weather_cat_"+ weatherData.getId() + ".jpg");
    }

    private File createImageFile() throws IOException
    {
        File file = getFile();
        // vgrajena funcija ki ustvari novo datoteko
        file.createNewFile();

        return file;
    }
}
