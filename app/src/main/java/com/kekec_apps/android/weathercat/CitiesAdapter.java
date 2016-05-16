package com.kekec_apps.android.weathercat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kekec_apps.android.weathercat.model.WeatherData;

/**
 * Created by Janko on 15.5.2016.
 */

public class CitiesAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private WeatherData[] data;

    // Konstruktor
    public CitiesAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
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
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        WeatherData item = getItem(position);
        TextView title = (TextView) view.findViewById(android.R.id.text1);
        title.setText(item.getName());

        return view;
    }
}
