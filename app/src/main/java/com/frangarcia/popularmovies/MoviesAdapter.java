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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frangarcia.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviePosterViewHolder> {
    /* *****************************************
     * Constants
     ******************************************/
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    /* *****************************************
     * Fields
     ******************************************/
    private int mTotalMovies;

    /* *****************************************
     * Listeners
     ******************************************/
    private final MoviePostersClickListener mOnClickListener;

    public interface MoviePostersClickListener{
        void onPosterClick(int clickedPosterIndex);
    }

    /* *****************************************
     * Constructors
     ******************************************/
    public MoviesAdapter(int totalMovies, MoviePostersClickListener listener){
        mTotalMovies = totalMovies;
        mOnClickListener = listener;
    }

    /* *****************************************
     * Overridden Methods
     ******************************************/
    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        MoviePosterViewHolder viewHolder = new MoviePosterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTotalMovies;
    }

    /* *****************************************
     * Inner ViewHolder class
     ******************************************/
    class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /* *****************************************
        * Fields
        ******************************************/
        private ImageView mMoviePosterView;

        /* *****************************************
        * Constructors
        ******************************************/
        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            mMoviePosterView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            mMoviePosterView.setOnClickListener(this);
        }

        /* *****************************************
        * Overridden Methods
        ******************************************/
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onPosterClick(clickedPosition);
        }

        /* *****************************************
        * Private Methods
        ******************************************/

        /**
         * Load the movie poster into the ImageView control
         * @param posterIndex
         */
        private void bind(int posterIndex) {
            Log.v(TAG, "Movie Poster Index: " + posterIndex);

            //Load movie poster
            String moviePosterAPIPath = MainActivity.mMovies.get(posterIndex).getmPosterPath();
            String moviePosterURL = NetworkUtils.buildImageURL(moviePosterAPIPath).toString();
            Picasso.with(itemView.getContext()).load(moviePosterURL).into(mMoviePosterView);
        }
    }
}
