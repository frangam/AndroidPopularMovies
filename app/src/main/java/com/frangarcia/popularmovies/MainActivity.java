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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviePostersClickListener{
    private static final int NUM_LIST_ITEMS = 10;
    private MoviesAdapter mAdapter;
    private RecyclerView mRecycler;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycler = (RecyclerView) findViewById(R.id.rv_movie_posters);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(NUM_LIST_ITEMS, this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onPosterClick(int clickedPosterIndex) {
        if(mToast != null){
            mToast.cancel();
        }

        String toastMessage = "Item #"+clickedPosterIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
    }
}
