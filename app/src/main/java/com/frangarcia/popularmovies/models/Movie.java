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

package com.frangarcia.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.frangarcia.popularmovies.utilities.ArraysUtils;
import com.frangarcia.popularmovies.utilities.JSONUtils;
import com.frangarcia.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/*
 * Data Model for movies following the documentation from
 * https://developers.themoviedb.org/3/movies/get-popular-movies
 */
public class Movie implements Parcelable{
    /* *****************************************
     * Constants for json field names
     ******************************************/
    static final String        ID_FIELD                    = "id";
    static final String        POSTER_PATH_FIELD           = "poster_path";
    static final String        ADULT_FIELD                 = "adult";
    static final String        OVERVIEW_FIELD              = "overview";
    static final String        RELEASE_DATE_FIELD          = "release_date";
    static final String        GENRE_IDS_FIELD             = "genre_ids";
    static final String        ORIGINAL_TITLE_FIELD        = "original_title";
    static final String        ORIGINAL_LANGUAGE_FIELD     = "original_language";
    static final String        TITLE_FIELD                 = "title";
    static final String        POPULARITY_FIELD            = "popularity";
    static final String        VOTE_COUNT_FIELD            = "vote_count";
    static final String        VIDEO_FIELD                 = "video";
    static final String        VOTE_AVERAGE_FIELD          = "vote_average";

    //the total amount of the fields written as String Array in destination Parcel
    static final int           TOTAL_STRING_ARRAY_FIELDS   = 13;

    /* *****************************************
     * Fields
     ******************************************/
    private Integer     mId;
    private String      mPosterPath;
    private Boolean     mAdult;
    private String      mOverview;
    private String      mReleaseDate;
    private Integer[]   mGenreIds;
    private String      mOriginalTitle;
    private String      mOriginalLanguage;
    private String      mTitle;
    private Double      mPopularity;
    private Integer     mVoteCount;
    private Boolean     mVideo;
    private Double      mVoteAverage;
    private String      posterCompleteURL;

    /* *****************************************
     * Getters & Setters
     ******************************************/
    public Integer getmId() {
        return mId;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public Boolean getmAdult() {
        return mAdult;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public Integer[] getmGenreIds() {
        return mGenreIds;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmOriginalLanguage() {
        return mOriginalLanguage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public Double getmPopularity() {
        return mPopularity;
    }

    public Integer getmVoteCount() {
        return mVoteCount;
    }

    public Boolean getmVideo() {
        return mVideo;
    }

    public Double getmVoteAverage() {
        return mVoteAverage;
    }

    public String getPosterCompleteURL() {
        return posterCompleteURL;
    }

    public void setPosterCompleteURL(String posterCompleteURL) {
        this.posterCompleteURL = posterCompleteURL;
    }

    /* *****************************************
     * Constructors
     ******************************************/
    /**
     * Creates an Movie instance from a given JSON object
     * @param json the JSON object
     * @throws JSONException
     */
    public  Movie(JSONObject json) throws JSONException {
        this(new Integer(json.getInt(ID_FIELD)), json.getString(POSTER_PATH_FIELD)
                , json.getBoolean(ADULT_FIELD), json.getString(OVERVIEW_FIELD)
                , json.getString(RELEASE_DATE_FIELD), JSONUtils.getIntegerArray(json.getJSONArray(GENRE_IDS_FIELD)), json.getString(ORIGINAL_TITLE_FIELD)
                , json.getString(ORIGINAL_LANGUAGE_FIELD), json.getString(TITLE_FIELD)
                , json.getDouble(POPULARITY_FIELD), json.getInt(VOTE_COUNT_FIELD)
                , json.getBoolean(VIDEO_FIELD), json.getDouble(VOTE_AVERAGE_FIELD));
    }

    public Movie(Parcel parcel) {
        String[] rawData = new String[TOTAL_STRING_ARRAY_FIELDS];
//        int[] readGenresIds = null;
//        parcel.readIntArray(readGenresIds);
        parcel.readStringArray(rawData);

        mId = Integer.parseInt(rawData[0]);
        mPosterPath = rawData[1];
        mAdult = Boolean.parseBoolean(rawData[2]);
        mOverview = rawData[3];
        mReleaseDate = rawData[4];
//        mGenreIds = ArraysUtils.intArrayToIntegerArray(readGenresIds);
        mOriginalTitle = rawData[5];
        mOriginalLanguage = rawData[6];
        mTitle = rawData[7];
        mPopularity = Double.parseDouble(rawData[8]);
        mVoteCount = Integer.parseInt(rawData[9]);
        mVideo = Boolean.parseBoolean(rawData[10]);
        mVoteAverage = Double.parseDouble(rawData[11]);
        posterCompleteURL = rawData[12];
    }

    public Movie(Integer id, String posterPath, Boolean adult, String overview, String releaseDate
            , Integer[] genreIds, String originalTitle, String originalLanguage, String title
            , Double popularity, Integer voteCount, Boolean video, Double voteAverage){
        mId = id;
        mPosterPath = posterPath.substring(1); //remove first character (slash "/")
        mAdult = adult;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mGenreIds = genreIds;
        mOriginalTitle = originalTitle;
        mOriginalLanguage = originalLanguage;
        mTitle = title;
        mPopularity = popularity;
        mVoteCount = voteCount;
        mVideo = video;
        mVoteAverage = voteAverage;
        posterCompleteURL = NetworkUtils.buildImageURL(mPosterPath).toString();
    }

    /* *****************************************
    * Overridden Methods
    ******************************************/
    @Override
    public String toString() {
        return "Movie{" +
                "mId=" + mId +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mAdult=" + mAdult +
                ", mOverview='" + mOverview + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mGenreIds=" + Arrays.toString(mGenreIds) +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mOriginalLanguage='" + mOriginalLanguage + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mPopularity=" + mPopularity +
                ", mVoteCount=" + mVoteCount +
                ", mVideo=" + mVideo +
                ", mVoteAverage=" + mVoteAverage +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                String.valueOf(mId),
                mPosterPath,
                String.valueOf(mAdult),
                mOverview,
                mReleaseDate,
                mOriginalTitle,
                mOriginalLanguage,
                mTitle,
                String.valueOf(mPopularity),
                String.valueOf(mVoteCount),
                String.valueOf(mVideo),
                String.valueOf(mVoteAverage),
                posterCompleteURL
        });

//        dest.writeIntArray(ArraysUtils.integerArrayToIntArray(getmGenreIds()));
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
