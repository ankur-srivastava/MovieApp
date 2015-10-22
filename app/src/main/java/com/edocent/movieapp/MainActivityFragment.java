package com.edocent.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.edocent.movieapp.adapters.MovieAdapter;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    GridView moviesListView;
    static String TAG = "MainActivityFragment";
    List<Movie> moviesListFromJSON;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        moviesListView = (GridView)view.findViewById(R.id.moviesListViewId);
        moviesListView.setOnItemClickListener(this);
        moviesListView.setOnScrollListener(this);

        /*Get Movies List*/
        MovieService service = new MovieService();
        service.execute(AppConstants.POPULARITY);
        /*Ends*/

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie detailMovieObj = null;
        if(moviesListFromJSON != null && moviesListFromJSON.get(position) != null){
            detailMovieObj = moviesListFromJSON.get(position);
        }

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(AppConstants.DETAIL_MOVIE_OBJECT, detailMovieObj);

        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    /*Add an Async Task class*/
    public class MovieService extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            Log.v(TAG, "In doInBackground with param "+params[0]);
            return getMovieJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result){
            /*Populate the Movie object with the data from the service call*/
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            Log.v(TAG, "Got the following result "+result);
            try {
                jsonObject = new JSONObject(result);
                if(jsonObject != null){
                    jsonArray = jsonObject.getJSONArray("results");
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error "+e.getMessage());
            }
            if(jsonArray != null){
                moviesListFromJSON = new ArrayList<Movie>();
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        if(tempObject != null){
                            moviesListFromJSON.add(mapMovieData(tempObject));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error " + e.getMessage());
                    }

                }
            }
            if(moviesListFromJSON != null) {
                Log.v(TAG, "moviesListFromJSON size is " + moviesListFromJSON.size());
                /*Call Adapter*/
                MovieAdapter adapter = new MovieAdapter(getActivity(), R.layout.list_item_movie, moviesListFromJSON);
                moviesListView.setAdapter(adapter);
            }
        }
    }

    /*
    Get data from the service
    This code has been borrowed from https://gist.github.com/udacityandroid/d6a7bb21904046a91695
    * */
    public String getMovieJSONString(String sortBy){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            //URL url = new URL(AppConstants.POPULAR_MOVIES_URL);
            //sort_by=popularity.desc&api_key="+MOVIE_API_KEY
            Uri uri= Uri.parse(AppConstants.BASE_URL).buildUpon()
                    .appendQueryParameter(AppConstants.SORT_BY, sortBy)
                    .appendQueryParameter(AppConstants.API_KEY, AppConstants.MOVIE_API_KEY)
                    .build();

            Log.v(TAG, "URI - "+uri.toString());
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return movieJsonStr;
    }

    /*Map JSON to Movie object*/
    /*
    * Sample response
    *               "adult": false,
	                "backdrop_path": "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg",
	                "genre_ids": [
	                    28,
	                    12,
	                    878,
	                    53
	                ],
	                "id": 135397,
	                "original_language": "en",
	                "original_title": "Jurassic World",
	                "overview": "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
	                "release_date": "2015-06-12",
	                "poster_path": "/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
	                "popularity": 52.196327,
	                "title": "Jurassic World",
	                "video": false,
	                "vote_average": 6.9,
	                "vote_count": 2657
    * */
    public Movie mapMovieData(JSONObject tempObject){
        if(tempObject == null){
            return null;
        }
        Movie tempMovie = new Movie();
        try {
            tempMovie.setTitle(tempObject.getString("title"));
            if(tempObject.getString("release_date") != null && !tempObject.getString("release_date").equals("")){
                tempMovie.setReleaseDate(getYear(tempObject.getString("release_date")));
            }
            tempMovie.setPosterPath(tempObject.getString("poster_path"));
            tempMovie.setOverview(tempObject.getString("overview"));
            tempMovie.setMovieLength(tempObject.getString("overview"));
            tempMovie.setVoteCount(tempObject.getString("vote_count"));
            tempMovie.setVoteAverage(tempObject.getString("vote_average")+"/10");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return tempMovie;
    }

    public String getYear(String date){
        String year = "";
        year = date.substring(0, date.indexOf("-"));
        return year;
    }
}