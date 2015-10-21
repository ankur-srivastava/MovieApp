package com.edocent.movieapp.utilities;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class AppConstants {
    //Please modify this key when using this App : https://www.themoviedb.org/
    public static final String MOVIE_API_KEY = "2488d2824d22372dac5e1c8f6e779c5f";
    public static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    public static final String SORT_BY="sort_by";
    public static final String API_KEY="api_key";
    public static final String POPULARITY = "popularity.desc";
    public static final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+MOVIE_API_KEY;
}
