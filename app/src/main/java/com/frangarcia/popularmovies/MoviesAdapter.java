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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.frangarcia.popularmovies.models.Movie;
import com.frangarcia.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /* *****************************************
     * Constants
     ******************************************/
    private final String TAG = MoviesAdapter.class.getSimpleName();

    // The view types
    private final int VIEW_MOVIE_POSTERS        = 0;
    private final int VIEW_LOADER_INDICATOR     = 1;

    /* *****************************************
     * Fields
     ******************************************/
    private List<Movie>     mMovies;
    private RecyclerView    mRecycler;
    private Context         appContext;

    //
    // The minimum amount of items to have below your current scroll position before loading more.
    // idea from http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom
    private int visibleThreshold = 4;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean loading = false;
    private int mScrollY;

    /* *****************************************
     * Listeners
     ******************************************/
    private final MoviePosterEventsListener mEventsListener;

    public interface MoviePosterEventsListener {
        void onPosterClick(int clickedPosterIndex);
        void onLoadMoreMovies();
    }


    /* *****************************************
     * Getters & Setters
     ******************************************/

    public List<Movie> getmMovies() {
        return mMovies;
    }

    public void setmMovies(List<Movie> mMovies) {
        mMovies = mMovies;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    /* *****************************************
     * Constructors
     ******************************************/
    public MoviesAdapter(Context context, List<Movie> movies, RecyclerView recyclerView, MoviePosterEventsListener listener){
        appContext = context;
        mMovies = movies;
        mEventsListener = listener;
        mRecycler = recyclerView;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            // implement an endless scroll
            //idea from http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom
            mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mScrollY += dy;

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // Scroll End has been reached and we continue loading more movies
                        if (mEventsListener != null) {
                            mEventsListener.onLoadMoreMovies();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    /* *****************************************
     * Overridden Methods
     ******************************************/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType == VIEW_MOVIE_POSTERS) {
            view = inflater.inflate(R.layout.movie_poster, parent, shouldAttachToParentImmediately);
            viewHolder = new MoviePosterViewHolder(view);
        }
        else if(viewType == VIEW_LOADER_INDICATOR){
            view = inflater.inflate(R.layout.progress_loader, parent, shouldAttachToParentImmediately);
            viewHolder = new ProgressViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MoviePosterViewHolder) {
            ((MoviePosterViewHolder)holder).bind(position);
        }
        else if(holder instanceof ProgressViewHolder){
            ((ProgressViewHolder) holder).getLoaderIndicator().setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMovies.get(position) != null ? VIEW_MOVIE_POSTERS : VIEW_LOADER_INDICATOR;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_MOVIE_POSTERS) ? position : -1;
    }



    /* *****************************************
     * Inner MoviePosterViewHolder class
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
            mEventsListener.onPosterClick(clickedPosition);
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
            if(mMovies != null && mMovies.get(posterIndex) != null && NetworkUtils.isConnectedToInternet(appContext)) {
                Picasso.with(itemView.getContext()).load(mMovies.get(posterIndex).getPosterCompleteURL()).into(mMoviePosterView);
            }
        }
    }

    /* *****************************************
     * Inner ProgressViewHolder class
     ******************************************/
    class ProgressViewHolder extends RecyclerView.ViewHolder {
        /* *****************************************
         * Fields
         ******************************************/
        private ProgressBar mLoaderIndicator;

        /* *****************************************
         * Getters & Setters
         ******************************************/

        public ProgressBar getLoaderIndicator() {
            return mLoaderIndicator;
        }

        /* *****************************************
         * Constructors
         ******************************************/
        public ProgressViewHolder(View v) {
            super(v);
            mLoaderIndicator = (ProgressBar) v.findViewById(R.id.pb_loader_indicator);
        }
    }
}
