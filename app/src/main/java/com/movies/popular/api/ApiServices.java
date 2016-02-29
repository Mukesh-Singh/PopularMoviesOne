package com.movies.popular.api;


import com.movies.popular.model.movie_api.MoviesResponseBean;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by mukesh on 17/2/16.
 */
public interface ApiServices {


    @GET("discover/movie")
    Call<MoviesResponseBean> apiMoviesList(@QueryMap Map<String, String> stringMap);


    @GET("movie/{movie_id}?")
    Call<MoviesResponseBean.MoviesResult> apiMoviesDetail(@Path("movie_id") long taskId, @QueryMap Map<String, String> stringMap);



}
