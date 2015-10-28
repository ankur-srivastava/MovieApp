package com.edocent.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edocent.movieapp.utilities.AppConstants;

/**
 * Created by Ankur on 10/28/2015.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    ContentValues movieContentValues;

    public MovieDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, AppConstants.DB_NAME, factory, AppConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Add Movie table
        db.execSQL("CREATE TABLE MOVIE (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"movieId INTEGER"
                +"title TEXT"
                +"overview TEXT"
                +"releaseDate TEXT"
                +"posterPath TEXT"
                +"voteCount TEXT"
                +"movieLength TEXT"
                +"voteAverage TEXT"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
