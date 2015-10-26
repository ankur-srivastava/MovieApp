package com.edocent.movieapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * This Fragment will be used to display Movie Reviews
 * Sample URL http://api.themoviedb.org/3/movie/177677/reviews?api_key=2488d2824d22372dac5e1c8f6e779c5f
 */
public class MovieReviewsFragment extends Fragment {


    public MovieReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_reviews, container, false);
    }


}
