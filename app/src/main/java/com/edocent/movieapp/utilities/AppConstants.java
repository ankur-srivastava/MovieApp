package com.edocent.movieapp.utilities;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class AppConstants {
    //Please modify this key when using this App : https://www.themoviedb.org/
    public static final String MOVIE_API_KEY = "2488d2824d22372dac5e1c8f6e779c5f";
    public static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    public static final String SORT_BY="sort_by";
    public static final String PAGE_NO="page";
    public static final String API_KEY="api_key";
    public static final String POPULARITY = "popularity.desc";
    public static final String RATING = "vote_average.desc";
    public static final String POPULAR_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+MOVIE_API_KEY;
    public static final String MOVIE_URL="http://image.tmdb.org/t/p/w342";
    public static final String MOVIE_DETAIL_URL="http://image.tmdb.org/t/p/w780";
    public static final String DETAIL_MOVIE_OBJECT="movie_detail";
    public static final String MOVIE_LIST_FROM_BUNDLE_KEY="movie_list_from_bundle_key";
    public static final String MOVIE_DTL_FROM_BUNDLE_KEY="movie_list_from_bundle_key";
}
