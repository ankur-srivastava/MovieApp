package com.edocent.movieapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edocent.movieapp.R;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;

import java.util.List;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    static final String TAG = MovieAdapter.class.getSimpleName();

    Context mContext;
    int resource;
    List<Movie> mMovieList;

    public MovieAdapter(Context context, int resource, List<Movie> movieList) {
        super(context, resource, movieList);
        this.mContext = context;
        this.resource = resource;
        this.mMovieList = movieList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        Movie movie = mMovieList.get(position);

        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        convertView = inflater.inflate(resource, viewGroup, false);

        TextView titleText = (TextView) convertView.findViewById(R.id.movieTitleId);
        titleText.setText(movie.getTitle());

        //Sizes - 75, 150, 300, 500
        //Image URL - http://image.tmdb.org/t/p/w75/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg
        // "w92", "w154", "w185", "w342", "w500", "w780", or "original" - w185 is recommended
        ImageView movieIcon = (ImageView) convertView.findViewById(R.id.movieIconId);
        String imageURL = AppConstants.MOVIE_URL+movie.getPosterPath();
        Log.v(TAG, "Image URL "+imageURL);
        //add this compile 'com.squareup.picasso:picasso:2.5.2'
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        return convertView;
    }
}
