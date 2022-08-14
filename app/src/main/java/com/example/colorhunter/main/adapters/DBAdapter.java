package com.example.colorhunter.main.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBAdapter extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myDatabase";
    public static final int DATABASE_VERSION = 1;

    // colors table
    public static final String DATABASE_TABLE_COLORS = "colors";

    public static final String KEY_ID = "_id";

    public static final String KEY_COLOR_NAME = "name";
    public static final String KEY_COLOR_DESCRIPTION = "description";
    public static final String KEY_COLOR_HEX = "hex";
    public static final String KEY_COLOR_RGB = "rgb";

    // create colors table
    private static final String DATABASE_CREATE_TABLE_COLORS =
            "create table " + DATABASE_TABLE_COLORS + "("
                    + KEY_ID + " integer primary key AUTOINCREMENT, "
                    + KEY_COLOR_NAME + " text, "
                    + KEY_COLOR_DESCRIPTION + " text, "
                    + KEY_COLOR_HEX + " text, "
                    + KEY_COLOR_RGB + " text)";

    public DBAdapter(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_COLORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE_COLORS);

        onCreate(db);
    }

    public void putData(String name, String description, String hex, String rgb){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBAdapter.KEY_COLOR_NAME, name);
        contentValues.put(DBAdapter.KEY_COLOR_DESCRIPTION, description);
        contentValues.put(DBAdapter.KEY_COLOR_HEX, hex);
        contentValues.put(DBAdapter.KEY_COLOR_RGB, rgb);

        database.insert(DBAdapter.DATABASE_TABLE_COLORS, null, contentValues);
        database.close();
    }
}
