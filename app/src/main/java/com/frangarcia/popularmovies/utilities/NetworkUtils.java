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

/*
 * Modifications copyright (C) 2016 Francisco Manuel Garcia Moreno
 */

package com.frangarcia.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.frangarcia.popularmovies.MainActivity;
import com.frangarcia.popularmovies.MoviesSortOrder;
import com.frangarcia.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {
    /* *****************************************
     * Constants
     ******************************************/
    private static final String TAG                 = NetworkUtils.class.getSimpleName();
    private static final String MOVIES_API_BASE_URL = "http://api.themoviedb.org/3";
    private static final String IMAGES_BASE_URL     = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_PATH          = "movie";
    private static final String MOST_POPULAR_PATH   = "popular";
    private static final String TOP_RATED_PATH      = "top_rated";
    private static final String API_QUERY           = "api_key";
    private static final String PAGE_PARAM          = "page";
    private static final String IMG_SIZE_92         = "w92";
    private static final String IMG_SIZE_154        = "w154";
    private static final String IMG_SIZE_185        = "w185";
    private static final String IMG_SIZE_342        = "w342";
    private static final String IMG_SIZE_500        = "w500";
    private static final String IMG_SIZE_780        = "w780";
    private static final String IMG_SIZE_ORIGINAL   = "original";

    /* *****************************************
    * Public Static Methods
    ******************************************/
    public static URL buildUrl(Context context) {return  buildUrl(context, 1, MoviesSortOrder.MOST_POPULAR);}
    public static URL buildUrl(Context context, int page) {return  buildUrl(context, page, MoviesSortOrder.MOST_POPULAR);}
    public static URL buildUrl(Context context, MoviesSortOrder order) {return  buildUrl(context, 1, order);}

    /**
     * Build the URL
     * @param context the context
     * @param page the current page
     * @param order the sort order for filtering
     * @return the movies search URL ordered by sort order parameter
     */
    public static URL buildUrl(Context context, int page, MoviesSortOrder order) {
        URL url = null;

        Uri builtUri = Uri.parse(MOVIES_API_BASE_URL);
        Uri.Builder uriBuilder = builtUri.buildUpon()
                .appendPath(MOVIE_PATH)
                .appendQueryParameter(API_QUERY, context.getString(R.string.api_key))
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page));

        switch (order){
            case MOST_POPULAR:
                uriBuilder.appendPath(MOST_POPULAR_PATH).build();
                break;
            case TOP_RATED:
                uriBuilder.appendPath(TOP_RATED_PATH).build();
                break;
        }

        builtUri = uriBuilder.build();

        try {
            url = new URL(builtUri.toString());
            Log.v(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Build the movie poster URL
     * @param imagePath the image path
     * @return the movie poster URL
     */
    public static URL buildImageURL(String imagePath){
        URL url = null;
        Uri builtUri = Uri.parse(IMAGES_BASE_URL).buildUpon()
                .appendPath(IMG_SIZE_185)
                .appendPath(imagePath)
                .build();

        try {
            url = new URL(builtUri.toString());
            Log.v(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}