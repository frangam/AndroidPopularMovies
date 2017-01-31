/*
 * Copyright (C) 2016 The Android Open Source Project
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.frangarcia.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    /* *****************************************
    * Constants
    ******************************************/
    private static final String TAG                 = MoviesAdapter.class.getSimpleName();

    /* *****************************************
    * Fields
    ******************************************/
    private TextView    mOriginalTitle;
    private ImageView   mMoviePoster;
    private TextView    mReleaseDate;
    private TextView    mVoteAverage;
    private TextView    mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();

        // Allow Up navigation with the app icon in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //load selected movie info
        if(intent.hasExtra(MainActivity.MOVIE_INTENT_EXTRA)) {
            Movie selectedMovie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_INTENT_EXTRA);

            mOriginalTitle = (TextView) findViewById(R.id.tv_movie_original_title);
            mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster_detail);
            mReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
            mVoteAverage = (TextView) findViewById(R.id.tv_movie_vote_average);
            mOverview = (TextView) findViewById(R.id.tv_movie_overview);

            Picasso.with(getApplicationContext()).load(selectedMovie.getPosterCompleteURL()).into(mMoviePoster);
            mOriginalTitle.setText(selectedMovie.getmOriginalTitle());
            mReleaseDate.setText(selectedMovie.getmReleaseDate().substring(0,4));
            mVoteAverage.setText(selectedMovie.getmVoteAverage().toString()+"/10");
            mOverview.setText(selectedMovie.getmOverview());
        }
    }
}
