package com.edocent.movieapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = "DetailActivityFragment";

    Movie movieDetailObject;
    ImageView movieDetailImage;
    TextView movieDetailTitle;
    TextView movieDetailYear;
    TextView movieDetailLength;
    TextView movieDetailRating;
    TextView movieDetailOverview;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        movieDetailImage = (ImageView) view.findViewById(R.id.movieDetailImageId);
        movieDetailTitle = (TextView) view.findViewById(R.id.movieDetailTitleId);
        movieDetailYear = (TextView) view.findViewById(R.id.movieDetailYearId);
        movieDetailLength = (TextView) view.findViewById(R.id.movieDetailLengthId);
        movieDetailRating = (TextView) view.findViewById(R.id.movieDetailRatingId);
        movieDetailOverview = (TextView) view.findViewById(R.id.movieDetailOverviewId);

        if(getActivity().getIntent() != null){
            movieDetailObject = (Movie) getActivity().getIntent().getExtras().get(AppConstants.DETAIL_MOVIE_OBJECT);
        }

        if(movieDetailObject != null){
            String imageURL = AppConstants.MOVIE_URL+movieDetailObject.getPosterPath();
            Log.v(TAG, "Image URL " + imageURL);
            Picasso.with(getActivity()).load(imageURL).into(movieDetailImage);
            movieDetailTitle.setText(movieDetailObject.getTitle());
            movieDetailYear.setText(movieDetailObject.getReleaseDate());
            //movieDetailLength.setText(movieDetailObject.getMovieLength());
            movieDetailRating.setText(movieDetailObject.getVoteAverage());
            movieDetailOverview.setText(movieDetailObject.getOverview());
        }

        return view;
    }
}
