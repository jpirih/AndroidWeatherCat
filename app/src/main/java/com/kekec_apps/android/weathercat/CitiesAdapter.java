package com.kekec_apps.android.weathercat;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;

/**
 * Created by Janko on 15.5.2016.
 */

public class CitiesAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private WeatherData[] data;
    private final String TAG = "Cities Adapter";
    private final Resources resources;

    // Konstruktor
    public CitiesAdapter(Context context){

        this.inflater = LayoutInflater.from(context);
        this.resources = context.getResources();
    }

    public void setItems(WeatherData[] data){
        this.data = data;
        notifyDataSetChanged();
    }


    // how many items is in the list
    @Override
    public int getCount() {
        if(data != null){
            return data.length;
        }else{
            return 0;
        }
    }
    // element na seznamu
    @Override
    public WeatherData getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_item_city, parent, false);
        }
        WeatherData item = getItem(position);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(item.getName());

        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        new ImageTask(resources, imageView).execute(R.drawable.sonce);
        return view;
    }

    private static class ImageTask extends AsyncTask<Integer, Void, Drawable>{
        private  final Resources resources;
        private final ImageView imageView;

        private ImageTask(Resources resources, ImageView imageView) {
            this.resources = resources;
            this.imageView = imageView;
        }

        @Override
        protected Drawable doInBackground(Integer... params) {
            return resources.getDrawable(params[0], null);
        }
        @Override
        protected void onPostExecute(Drawable drawable){
            imageView.setImageDrawable(drawable);

        }
    }
}
