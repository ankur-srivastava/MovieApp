package com.edocent.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.edocent.movieapp.DetailActivity;
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
        onUpgrade(db, 0, AppConstants.DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < AppConstants.DB_VERSION){
            //Add Movie table
            db.execSQL("CREATE TABLE MOVIE (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"MOVIEID INTEGER"
                    +"TITLE TEXT"
                    +"OVERVIEW TEXT"
                    +"RELEASEDATE TEXT"
                    +"POSTERPATH TEXT"
                    +"COUNT TEXT"
                    +"LENGTH TEXT"
                    +"AVERAGE TEXT"
                    +"FAVORITE TEXT"
                    +")");
        }
    }

    public class UpdateMovieAsync extends AsyncTask{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] params) {
            SQLiteOpenHelper tempDBHelper = (SQLiteOpenHelper) params[0];
            Movie movie = (Movie) params[1];
            if(tempDBHelper != null && movie != null){
                Log.v(TAG, "Going to update movie");
                updateMovie(tempDBHelper, movie);
            }
            return null;
        }
    }

    public void updateMovie(SQLiteOpenHelper helper, Movie movie){
        SQLiteDatabase db = helper.getWritableDatabase();
        Movie movieFromDB = getMovie(helper, (int)movie.getMovieId());
        movieContentValues = new ContentValues();
        if(movieFromDB != null){
            if(movieFromDB.getFavorite() == null || movieFromDB.getFavorite().equals("") || movieFromDB.getFavorite().equals(AppConstants.NOT_FAVORITE_MOVIE)){
                movieContentValues.put("FAVORITE", AppConstants.FAVORITE_MOVIE);
            }else{
                movieContentValues.put("FAVORITE", AppConstants.NOT_FAVORITE_MOVIE);
            }
            updateMovieRecord(db, movieContentValues, movieFromDB.getId());
        }else{
            //Add
            movieContentValues.put("MOVIEID", movie.getMovieId());
            movieContentValues.put("TITLE", movie.getTitle());
            movieContentValues.put("OVERVIEW", movie.getOverview());
            movieContentValues.put("RELEASEDATE", movie.getReleaseDate());
            movieContentValues.put("POSTERPATH", movie.getPosterPath());
            movieContentValues.put("COUNT", movie.getVoteCount());
            movieContentValues.put("LENGTH", movie.getMovieLength());
            movieContentValues.put("AVERAGE", movie.getVoteAverage());
            movieContentValues.put("FAVORITE", AppConstants.NOT_FAVORITE_MOVIE);
            addMovie(db, movieContentValues);
        }
    }

    public boolean updateMovieRecord(SQLiteDatabase db, ContentValues cv, int id){
        Log.v(TAG, "Ready to update");
        try{
            db.update("MOVIE", cv, "_id = ?", new String[]{Integer.toString(id)});
            Log.v(TAG, "Movie Updated");
            return true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public boolean addMovie(SQLiteDatabase db, ContentValues cv){
        Log.v(TAG, "Ready to Add");
        try{
            db.insert("MOVIE",null,cv);
            Log.v(TAG, "Movie Added");
            return true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static Movie getMovie(SQLiteOpenHelper helper, int movieId){
        Movie movie = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {


            if (db != null) {
                Cursor c = db.query("MOVIE", new String[]{"_id", "TITLE", "OVERVIEW", "RELEASEDATE", "POSTERPATH", "COUNT", "LENGTH", "AVERAGE", "FAVORITE"},
                        "MOVIEID=?",
                        new String[]{Integer.toString(movieId)},
                        null, null, null);
                if (c.moveToFirst()) {
                    movie = new Movie();
                    movie.setId(c.getInt(0));
                    movie.setTitle(c.getString(1));
                    movie.setOverview(c.getString(2));
                    movie.setReleaseDate(c.getString(3));
                    movie.setPosterPath(c.getString(4));
                    movie.setVoteCount(c.getString(5));
                    movie.setMovieLength(c.getString(6));
                    movie.setVoteAverage(c.getString(7));
                    movie.setFavorite(c.getString(8));
                }

                c.close();
            }
        }catch(SQLiteException ex){
            Log.e(TAG, ex.getMessage());
        }finally{
            db.close();
        }
        return movie;
    }
}