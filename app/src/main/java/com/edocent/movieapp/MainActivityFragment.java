package com.edocent.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
    ArrayList<Movie> moviesListFromJSON;
    ArrayList<Movie> allMoviesList;
    MovieAdapter adapter;

    int pageNo = 1;
    int tempFirstVisibleItem;
    boolean refreshEnabled = true;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        moviesListView = (GridView)view.findViewById(R.id.moviesListViewId);
        moviesListView.setOnItemClickListener(this);
        moviesListView.setOnScrollListener(this);

        if(savedInstanceState == null || !savedInstanceState.containsKey(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY)){
            if(refreshEnabled){
                getMovieList();
            }
        }else{
            //Log.v(TAG, "Get the list from Bundle");
            moviesListFromJSON = savedInstanceState.getParcelableArrayList(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY);
            if(allMoviesList == null){
                allMoviesList = new ArrayList<>();
            }
            allMoviesList.addAll(moviesListFromJSON);
            //Log.v(TAG, "Got the list, now set the adapter");
            setAdapter();
        }

        /*Ends*/

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        //getMovieList();
    }

    public boolean getSortOrderPref(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean sortOrderPref = sharedPreferences.getBoolean(getString(R.string.pref_rating_sort), false);
        return sortOrderPref;
    }

    public void getMovieList(){
        MovieService service = new MovieService();
        //Start progress bar
        if(getSortOrderPref()) {
            service.execute(AppConstants.RATING);
        }else{
            service.execute(AppConstants.POPULARITY);
        }
        //Stop progress bar
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie detailMovieObj = null;
        if(allMoviesList != null && allMoviesList.get(position) != null){
            detailMovieObj = allMoviesList.get(position);
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
        //If the visible item is about to become equal to the count then refresh
        //To refresh the list fetch is called with new page no -> progress bar started -> adapter notifies data change
        if(refreshEnabled && (tempFirstVisibleItem != firstVisibleItem) && ((totalItemCount - firstVisibleItem) <= 12)){
            //Log.v(TAG, "First visible " + firstVisibleItem+"totalItemCount is "+totalItemCount);
            tempFirstVisibleItem = firstVisibleItem;
            getMovieList();
        }
    }


    /*Add an Async Task class*/
    public class MovieService extends AsyncTask<String, Void, String>{

        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setMessage("We are almost done !!");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            refreshEnabled = false;
            //Log.v(TAG, "In doInBackground with param "+params[0]);
            return getMovieJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result){
            /*Populate the Movie object with the data from the service call*/
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            //Log.v(TAG, "Got the following result "+result);
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
                //Log.v(TAG, "moviesListFromJSON size is " + moviesListFromJSON.size());
                /*Call Adapter*/
                setAdapter();
                /*increment page count*/
                pageNo++;
                //Log.v(TAG, "Page Count is " + pageNo);
                if(allMoviesList == null){
                    allMoviesList = new ArrayList<>();
                }
                allMoviesList.addAll(moviesListFromJSON);
            }
            dialog.dismiss();
            refreshEnabled = true;
        }
    }

    public void setAdapter(){
        if(adapter == null) {
            adapter = new MovieAdapter(getActivity(), R.layout.list_item_movie, moviesListFromJSON);
            moviesListView.setAdapter(adapter);
        }else{
            adapter.addAll(moviesListFromJSON);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //Log.v(TAG, "Device orientation changed");
        if(moviesListFromJSON != null){
            bundle.putParcelableArrayList(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY, moviesListFromJSON);
            //Log.v(TAG, "List saved in Bundle");
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
                    .appendQueryParameter(AppConstants.PAGE_NO, String.valueOf(pageNo))
                    .appendQueryParameter(AppConstants.SORT_BY, sortBy)
                    .appendQueryParameter(AppConstants.API_KEY, AppConstants.MOVIE_API_KEY)
                    .build();

            //Log.v(TAG, "URI - "+uri.toString());
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
        if(date != null && date.length() > 0 && date.indexOf("-") > 0) {
            year = date.substring(0, date.indexOf("-"));
        }else{
            year = date;
        }
        return year;
    }
}