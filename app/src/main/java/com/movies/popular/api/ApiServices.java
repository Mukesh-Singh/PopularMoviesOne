package com.movies.popular.api;


import com.movies.popular.model.movie_api.MoviesResponseBean;
import com.movies.popular.model.review_api.ReviewsListingResponse;
import com.movies.popular.model.trailer_api.TrailersResponseBean;

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

    @GET("movie/{movie_id}/videos?")
    Call<TrailersResponseBean> apiMovieTrailers(@Path("movie_id") long movieId, @QueryMap Map<String, String> stringMap);

    @GET("movie/{movie_id}/reviews?")
    Call<ReviewsListingResponse> apiMovieReviews(@Path("movie_id") long movieId, @QueryMap Map<String, String> stringMap);



}
