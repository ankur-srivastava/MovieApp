package com.edocent.movieapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.model.Review;
import com.edocent.movieapp.utilities.AppConstants;
import com.edocent.movieapp.utilities.AppUtility;

public class MainActivity extends Activity implements MainActivityFragment.MovieDetailInterface, DetailActivityFragment.ReviewScreen, MovieReviewsFragment.ReviewDetail{

    private static final String TAG = MainActivity.class.getSimpleName();
    ConnectivityManager connMgr;
    View secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isOnline = AppUtility.isOnline(connMgr);
        secondFragment = findViewById(R.id.sectionTwoFragmentId);

        if(isOnline) {
            loadMainFragment();
            if(secondFragment != null){
                AppConstants.landscapeMode = true;
                loadDetailFragment();
            }else{
                AppConstants.landscapeMode = false;
            }
        }else{
            loadNoInternetFragment();
        }
        AppUtility.setupBannerIcon(getActionBar(), (ImageView) findViewById(android.R.id.home));
    }

    private void loadNoInternetFragment() {
        NoInternetFragment noInternetFragment = new NoInternetFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionOneFragmentId, noInternetFragment);
        fragmentTransaction.commit();
    }

    private void loadMainFragment() {
        MainActivityFragment mainActivityFragment = new MainActivityFragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionOneFragmentId, mainActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    private void loadDetailFragment(){
        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionTwoFragmentId, detailActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadMovieDetails(Movie movie) {
        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        detailActivityFragment.setMovieDetailObject(movie);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionTwoFragmentId, detailActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void displayReviewDetail(Review review) {
        ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
        reviewDetailFragment.setReview(review);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionTwoFragmentId, reviewDetailFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void displayReviews(long movieId) {
        Log.v(TAG, "In displayReviews .. and movieId is "+movieId);
        MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
        movieReviewsFragment.setMovieId(movieId);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionTwoFragmentId, movieReviewsFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
