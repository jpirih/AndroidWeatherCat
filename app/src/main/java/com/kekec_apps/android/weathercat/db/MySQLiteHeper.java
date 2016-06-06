package com.kekec_apps.android.weathercat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by Janko on 3.6.2016.
 */
public class MySQLiteHeper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weathercat_db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHeper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cities (_id integer primary key autoincrement, city_id integer);");

        long[] cities = new long[]{
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
        };

        for(long city : cities)
        {
            ContentValues values = new ContentValues();
            values.put("city_id", city);

            db.insert("cities", null, values);
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // za upgrade baze podatkov
    }
}
