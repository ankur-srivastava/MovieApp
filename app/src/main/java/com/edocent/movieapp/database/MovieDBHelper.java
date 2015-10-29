package com.edocent.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;

/**
 * Created by Ankur on 10/28/2015.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MovieDBHelper";
    ContentValues movieContentValues;

    public MovieDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, AppConstants.DB_NAME, factory, AppConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Add Movie table
        db.execSQL("CREATE TABLE MOVIE (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "MOVIEID INTEGER"
                + "TITLE TEXT"
                + "OVERVIEW TEXT"
                + "RELEASEDATE TEXT"
                + "POSTERPATH TEXT"
                + "COUNT TEXT"
                + "LENGTH TEXT"
                + "AVERAGE TEXT"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void updateMovie(SQLiteOpenHelper helper, Movie movie){
        boolean movieExists = false;

        //Check if movie exists
        SQLiteDatabase db = helper.getReadableDatabase();



        if(movieExists){
            //Update
        }else{
            //Add
        }
    }

    public static Movie getMovie(SQLiteOpenHelper helper, int movieId){
        Movie movie = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();

            if (db != null) {
                Cursor c = db.query("MOVIE", new String[]{"TITLE", "OVERVIEW", "RELEASEDATE", "POSTERPATH", "COUNT", "LENGTH", "AVERAGE"},
                        "MOVIEID=?",
                        new String[]{Integer.toString(movieId)},
                        null, null, null);
                if (c.moveToFirst()) {
                    movie = new Movie();
                    movie.setTitle(c.getString(0));
                    movie.setOverview(c.getString(1));
                    movie.setReleaseDate(c.getString(2));
                    movie.setPosterPath(c.getString(3));
                    movie.setVoteCount(c.getString(4));
                    movie.setMovieLength(c.getString(5));
                    movie.setVoteAverage(c.getString(6));
                }

                c.close();
                db.close();
            }
        }catch(SQLiteException ex){
            Log.e(TAG, ex.getMessage());
        }
        return movie;
    }
}
