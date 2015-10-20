package com.edocent.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.edocent.movieapp.R;
import com.edocent.movieapp.model.Movie;

import java.util.List;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, List<Movie> movieList) {
        super(context, 0, movieList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Movie movie = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, viewGroup, false);
        }

        return view;
    }
}
