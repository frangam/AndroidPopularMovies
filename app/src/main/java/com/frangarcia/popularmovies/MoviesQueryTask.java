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


import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.frangarcia.popularmovies.utilities.AsyncTaskCompleteListener;
import com.frangarcia.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * AcyncTask class for getting movies JSON file in background thread
 */
public class MoviesQueryTask extends AsyncTask<URL, Void, String>{
    /* *****************************************
     * Fields
     ******************************************/
    private Context mContext;
    private AsyncTaskCompleteListener<String> mListener;

    /* *****************************************
     * Constructors
     ******************************************/
    public MoviesQueryTask(Context main, AsyncTaskCompleteListener<String> listener){
        mContext = main;
        mListener = listener;
    }

    /* *****************************************
     * Overridden Methods
     ******************************************/
    @Override
    protected void onPreExecute() {
        mListener.OnTaskPreRun();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        String moviesSearchResults = null;

        try {
            if(NetworkUtils.isConnectedToInternet(mContext)) {
                moviesSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return moviesSearchResults;
    }

    @Override
    protected void onPostExecute(String moviesSearchResults) {
        mListener.onTaskComplete(moviesSearchResults);


    }
}
