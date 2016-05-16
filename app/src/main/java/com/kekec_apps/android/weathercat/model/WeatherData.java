package com.kekec_apps.android.weathercat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Janko on 15.5.2016.
 */
public class WeatherData implements Parcelable {
    private long id;
    private String name;
    private Main main;

    public WeatherData(long id, String name, float temp){
        this.id = id;
        this.name = name;
        this.main = new Main(temp);

    }

    protected WeatherData(Parcel in) {
        id = in.readLong();
        name = in.readString();
        main = in.readParcelable(Main.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Main getMain() {
        return main;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeParcelable(main, 0);
    }


    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };



}
