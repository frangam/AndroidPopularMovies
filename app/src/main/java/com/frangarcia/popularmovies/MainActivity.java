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

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frangarcia.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviePostersClickListener{
    /* *****************************************
    * Constants
    ******************************************/
    private static final String TAG                 = MoviesAdapter.class.getSimpleName();
    private static final String MOVIE_RESULTS_ARRAY = "results";
    private static final int    NUM_LIST_ITEMS      = 10;

    /* *****************************************
    * Fields
    ******************************************/
    private MoviesAdapter       mAdapter;
    private RecyclerView        mRecycler;
    private TextView            mErrorMessage;
    private ProgressBar         mLoadingIndicator;
    public static List<Movie>   mMovies;

    //TODO only for testing
    private EditText mAPIKeyView;
    private Toast mToast;
    private String mAPIKey;

    /* *****************************************
    * Overridden Methods
    ******************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //TODO remove after testing
        mAPIKeyView = (EditText) findViewById(R.id.tv_api_key);

        mRecycler = (RecyclerView) findViewById(R.id.rv_movie_posters);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setHasFixedSize(true);

        //TODO uncomment after testing
//        makeMoviesSearchQuery(MoviesSortOrder.MOST_POPULAR);
    }

    @Override
    public void onPosterClick(int clickedPosterIndex) {
        if(mToast != null){
            mToast.cancel();
        }

        //TODO only for testing
        String toastMessage = "Item #"+clickedPosterIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
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



        if(selectedItemId == R.id.it_most_popular) {
            makeMoviesSearchQuery(MoviesSortOrder.MOST_POPULAR);
        }
        else if(selectedItemId == R.id.it_top_rated){
            makeMoviesSearchQuery(MoviesSortOrder.TOP_RATED);
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
         //TODO only for testing
         NetworkUtils.SECRET_API_KEY = mAPIKeyView.getText().toString();

         URL moviesSearchUrl = NetworkUtils.buildUrl(order);
         new MoviesQueryTask().execute(moviesSearchUrl);
     }

    /**
     * Load our movies list from a given json content file
     * @param jsonContent the json content file
     */
    private void loadMovies(String jsonContent){
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray results = jsonObject.getJSONArray(MOVIE_RESULTS_ARRAY);
            mMovies = new ArrayList<Movie>();

            for(int i=0; i<results.length(); i++){
                JSONObject jsonMovie = results.getJSONObject(i);
                Movie movie = new Movie(jsonMovie);
                mMovies.add(movie);
            }

            showMoviesPosters();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showMoviesPosters(){
        mRecycler.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);

        if(mMovies != null && mMovies.size() > 0){
            mAdapter = new MoviesAdapter(mMovies.size(), this);
            mRecycler.setAdapter(mAdapter);
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
            mRecycler.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String moviesSearchResults = null;

            try {
                moviesSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return moviesSearchResults;
        }

        @Override
        protected void onPostExecute(String moviesSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (moviesSearchResults != null && !moviesSearchResults.equals("")) {
                showMoviesPosters();
                loadMovies(moviesSearchResults);
            }
            else {
                showErrorMessage();
            }
        }
    }
}
