/*
 * Copyright (C) 2016 Francisco Manuel Garcia Moreno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frangarcia.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frangarcia.popularmovies.models.Movie;
import com.frangarcia.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviePosterEventsListener {
    /* *****************************************
    * Constants
    ******************************************/
    private static final String TAG                 = MoviesAdapter.class.getSimpleName();
    public  static final String MOVIE_INTENT_EXTRA  = "movie";
    private static final String MOVIE_RESULTS_ARRAY = "results";

    /* *****************************************
    * Fields
    ******************************************/
    private MoviesAdapter       mAdapter;
    private RecyclerView        mRecycler;
    private TextView            mErrorMessage;
    private ProgressBar         mLoadingIndicator;
    private int                 mCurrentMoviesPage = 1;
    private MoviesSortOrder     mCurrentSortOrder = MoviesSortOrder.MOST_POPULAR;
    private boolean             cleanMovies = true;

    //TODO only for testing
    private Toast mToast;


    /* *****************************************
    * Overridden Methods
    ******************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_main_loader_indicator);

        mRecycler = (RecyclerView) findViewById(R.id.rv_movie_posters);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this, new ArrayList<Movie>(), mRecycler, this);
        mRecycler.setAdapter(mAdapter);

        makeMoviesSearchQuery(mCurrentSortOrder);
    }

    @Override
    public void onPosterClick(int clickedPosterIndex) {
        Class destinationActivity = MovieDetailsActivity.class;
        Intent intent = new Intent(MainActivity.this, destinationActivity);
        Movie selectedMovie = mAdapter.getmMovies().get(clickedPosterIndex);
        intent.putExtra(MOVIE_INTENT_EXTRA, selectedMovie);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreMovies() {
        //add null , so the adapter will check view_type and show progress bar at bottom
        mAdapter.getmMovies().add(null);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemInserted(mAdapter.getmMovies().size());
            }
        });

        cleanMovies = false;
        mCurrentMoviesPage++;
        makeMoviesSearchQuery(mCurrentSortOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = true;
        int selectedItemId = item.getItemId();
        cleanMovies = true;
        mCurrentMoviesPage = 1;

        if(selectedItemId == R.id.it_most_popular) {
            makeMoviesSearchQuery(MoviesSortOrder.MOST_POPULAR);
        }
        else if(selectedItemId == R.id.it_top_rated){
            makeMoviesSearchQuery(MoviesSortOrder.TOP_RATED);
        }
        else if(selectedItemId == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        else{
            res = super.onOptionsItemSelected(item);
        }

        return res;
    }

     /* *****************************************
     * Private Methods
     ******************************************/
     private void makeMoviesSearchQuery(MoviesSortOrder order) {
         mCurrentSortOrder = order;
         URL moviesSearchUrl = NetworkUtils.buildUrl(this, mCurrentMoviesPage, mCurrentSortOrder);
         new MoviesQueryTask().execute(moviesSearchUrl);
     }

    /**
     * Load our movies list from a given json content file
     * @param jsonContent the json content file
     */
    private void loadMovies(final String jsonContent){
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray results = jsonObject.getJSONArray(MOVIE_RESULTS_ARRAY);

            //remove the null movie object added previously for showing loader indicator
            if (mCurrentMoviesPage > 1) {
                mAdapter.getmMovies().remove(mAdapter.getmMovies().size() - 1);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemRemoved( mAdapter.getmMovies().size());
                    }
                });
            }

            if(cleanMovies){
                mAdapter.getmMovies().clear();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            for(int i=0; i<results.length(); i++){
                JSONObject jsonMovie = results.getJSONObject(i);
                Movie movie = new Movie(jsonMovie);
                mAdapter.getmMovies().add(movie);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemInserted(mAdapter.getmMovies().size());
                    }
                });
            }

            mAdapter.setLoading(false);
            showMoviesPosters(mAdapter.getmMovies());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showMoviesPosters(List<Movie> movies){
        if(movies != null && movies.size() > 0){
            int totalMovies = movies.size();

            Log.v(TAG, "Total Movies: " + movies.size());
//            mAdapter = new MoviesAdapter(movies, mRecycler, this);
//            mRecycler.setAdapter(mAdapter);


            mRecycler.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMessage(){
        mRecycler.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    /* *****************************************
     * Inner AcyncTask class for getting
     * movies JSON file
     ******************************************/
    public class MoviesQueryTask extends AsyncTask<URL, Void, String> {
        /* *****************************************
        * Overridden Methods
        ******************************************/
        @Override
        protected void onPreExecute() {
//            mRecycler.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String moviesSearchResults = null;

            try {
                if(NetworkUtils.isConnectedToInternet(MainActivity.this)) {
                    moviesSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return moviesSearchResults;
        }

        @Override
        protected void onPostExecute(String moviesSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (moviesSearchResults != null && !moviesSearchResults.equals("")) {
                loadMovies(moviesSearchResults);
            }
            else {
                showErrorMessage();
            }
        }
    }
}
