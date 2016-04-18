package com.movies.popular.api;
/**
 * Created by mukesh on 17/2/16.
 */
public class ApiConstants {


//    public static final String APP_BASE_URL = "http://api.themoviedb.org/3/discover";
//
//    public static final String MOVIES_LIST = "/movie?";

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String BASE_THUMB_IMAGE_URL_W342 = "http://image.tmdb.org/t/p/w342";
    public static final String BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";



    /**
     * Please replace this with your api key
     */
    public static final String API_KEY = "e6177712a6fbb502abad001a3210aa00";

    //API Params
    public static final String PARAM_SORT_BY = "sort_by";
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_PAGE = "page";

    //Constants
    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String HIGHEST_RATED = "vote_average.desc";
    public static final String MY_FAVORITES = "sort.favorite";
    public static final String EXTRA_INTENT_PARCEL = "intent_parcelable_extra";

}
