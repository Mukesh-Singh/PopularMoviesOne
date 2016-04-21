package com.movies.popular.fragments;

/**
 * Created by mukesh on 1/2/16.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.movies.popular.activity.ReviewsListingActivity;
import com.movies.popular.adapters.TrailersAdapter;
import com.movies.popular.api.ApiConstants;
import com.movies.popular.api.RestClient;
import com.movies.popular.base.ApplicationController;
import com.movies.popular.db.MoviesListingTable;
import com.movies.popular.model.movie_api.MoviesResponseBean;
import com.movies.popular.model.review_api.ReviewsListingResponse;
import com.movies.popular.model.trailer_api.TrailersResponseBean;
import com.movies.popular.one.R;
import com.movies.popular.utility.AppConstants;
import com.movies.popular.utility.RecyclerViewWithEmptySupport;
import com.movies.popular.utility.SnackBarBuilder;
import com.movies.popular.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MovieDetailFragment extends Fragment implements View.OnClickListener
{
    private MoviesResponseBean.MoviesResult moviesResult;
    private TextView mMovieTitle,mReleaseYear,mDuration,mRating,mMarkAsFav,mDescription,mTagLine;
    private ImageView mPosterImage;
    private Snackbar mSnackBar;
    private RecyclerViewWithEmptySupport mTrailersRecyclerView;
    private LinearLayout reviewsContainer;
    private TextView seeMoreReviews;
    private FrameLayout mTrailerParentLayout;
    private ProgressBar mTrailersProgressBar;
    private Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle= getArguments();
        moviesResult=bundle.getParcelable(AppConstants.EXTRA_INTENT_PARCEL);
        //activity.setTitle(moviesResult.getTitle()+"");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity=activity;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intUI(view);
        getMoviesDetail();

        getMovieTrailers();
        getMovieReviews();

        setFavoriteText();
    }

    private void intUI(View view) {
        mMovieTitle=(TextView)view.findViewById(R.id.fragment_movie_detail_movie_name);
        mTagLine=(TextView)view.findViewById(R.id.fragment_movie_detail_movie_tagline);
        mReleaseYear=(TextView)view.findViewById(R.id.fragment_movie_detail_release_year_tv);
        mDuration=(TextView)view.findViewById(R.id.fragment_movie_detail_duration_tv);
        mRating=(TextView)view.findViewById(R.id.fragment_movie_detail_rating_tv);
        mMarkAsFav=(TextView)view.findViewById(R.id.fragment_movie_detail_favourite_tv);
        mDescription=(TextView)view.findViewById(R.id.fragment_movie_detail_description);
        mPosterImage=(ImageView)view.findViewById(R.id.fragment_movie_detail_image);
        mTrailersRecyclerView=(RecyclerViewWithEmptySupport) view.findViewById(R.id.movie_detail_trailers_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        mTrailersRecyclerView.setEmptyView(view.findViewById(R.id.trailer_list_empty));
        reviewsContainer = (LinearLayout) view.findViewById(R.id.reviews_listing_parent);
        seeMoreReviews = (TextView) view.findViewById(R.id.see_more_reviews);
        seeMoreReviews.setOnClickListener(this);
        seeMoreReviews.setVisibility(View.GONE);
        mTrailerParentLayout=(FrameLayout)view.findViewById(R.id.trailer_list_parent);
        mTrailersProgressBar=(ProgressBar)view.findViewById(R.id.trailer_progress_bar);

        mMarkAsFav.setOnClickListener(this);
        updateDetails();



    }

    private void updateDetails() {
        mMovieTitle.setText(moviesResult.getTitle());
        String formattedDate = Utility.parseDateTime(moviesResult.getReleaseDate(), AppConstants.DATE_FORMAT1, AppConstants.DATE_FORMAT2);
        mReleaseYear.setText(formattedDate);
        String rating=moviesResult.getVoteAverage() + " /10";
        mRating.setText(rating);

        mDescription.setText(moviesResult.getOverview());
        if (moviesResult!=null && moviesResult.getPosterPath()!=null && !moviesResult.getPosterPath().isEmpty()) {
            Glide
                    .with(activity)
                    .load(ApiConstants.BASE_THUMB_IMAGE_URL_W342 + moviesResult.getPosterPath())
                    //.crossFade()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(mPosterImage);


        }

    }

    private void getMoviesDetail() {
        if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
            //showProgressDialog(false);
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY);

            Call<MoviesResponseBean.MoviesResult> beanCall = RestClient.getInstance().getApiServices().apiMoviesDetail(moviesResult.getId(), stringHashMap);
            beanCall.enqueue(new Callback<MoviesResponseBean.MoviesResult>() {
                @Override
                public void onResponse(Response<MoviesResponseBean.MoviesResult> response, Retrofit retrofit) {
                    //showProgressDialog(false);
                    MoviesResponseBean.MoviesResult moviesResult = response.body();
                    if (moviesResult != null) {
                        mDuration.setText(moviesResult.getRuntime() + " min");
                        mTagLine.setText(moviesResult.getTagline());
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    //showProgressDialog(false);
                    Log.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(activity.getWindow().getDecorView(), getString(R.string.no_internet_connection))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesDetail();
                        }
                    })
                    .build();
        }
    }


    private void getMovieTrailers() {
        mTrailerParentLayout.setVisibility(View.GONE);
        if (ApplicationController.getApplicationInstance().isNetworkConnected() && isAdded()) {
            mTrailersProgressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY);
            Call<TrailersResponseBean> beanCall = RestClient.getInstance().getApiServices().apiMovieTrailers(moviesResult.getId(), stringHashMap);
            beanCall.enqueue(new Callback<TrailersResponseBean>() {
                @Override
                public void onResponse(Response<TrailersResponseBean> response1, Retrofit retrofit) {
                    mTrailersProgressBar.setVisibility(View.GONE);
                    TrailersResponseBean responseBean = response1.body();
                    if (responseBean != null && responseBean.getResults() != null && !responseBean.getResults().isEmpty()) {
                        TrailersAdapter trailersAdapter = new TrailersAdapter(activity, responseBean.getResults());
                        mTrailersRecyclerView.setAdapter(trailersAdapter);
                        mTrailerParentLayout.setVisibility(View.VISIBLE);
                    } else {
                        mTrailerParentLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mTrailersProgressBar.setVisibility(View.GONE);
                    Log.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(activity.getWindow().getDecorView(),getString(R.string.no_internet_connection))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesDetail();
                        }
                    })
                    .build();
        }
    }

    private void getMovieReviews() {
        if (ApplicationController.getApplicationInstance().isNetworkConnected() && isAdded()) {
            mTrailersProgressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY);
            Call<ReviewsListingResponse> beanCall = RestClient.getInstance().getApiServices().apiMovieReviews(moviesResult.getId(), stringHashMap);
            beanCall.enqueue(new Callback<ReviewsListingResponse>() {
                @Override
                public void onResponse(Response<ReviewsListingResponse> response1, Retrofit retrofit) {
                    ReviewsListingResponse responseBean = response1.body();
                    if (responseBean != null) {
                        ArrayList<ReviewsListingResponse.ReviewsEntity> reviewsEntities = responseBean.getResults();
                        if (reviewsEntities != null && !reviewsEntities.isEmpty()) {
                            addReviews(responseBean.getResults());
                        } else {
                            reviewsContainer.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    mTrailersProgressBar.setVisibility(View.GONE);
                    Log.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(activity.getWindow().getDecorView(),getString(R.string.no_internet_connection))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesDetail();
                        }
                    })
                    .build();
        }
    }

    private void addReviews(ArrayList<ReviewsListingResponse.ReviewsEntity> resultsEntityArrayList) {
        ArrayList<ReviewsListingResponse.ReviewsEntity> results;
        if (resultsEntityArrayList.size() > 3) {
            seeMoreReviews.setVisibility(View.VISIBLE);
            results = new ArrayList<>(resultsEntityArrayList.subList(0, 3));
        } else {
            results = resultsEntityArrayList;
        }
        for (int i = 0; i < results.size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_reviews_row, null);
            TextView reviewContentTv = (TextView) view.findViewById(R.id.review_content_tv);
            TextView reviewAuthorTv = (TextView) view.findViewById(R.id.review_author_tv);
            reviewContentTv.setText(results.get(i).getContent());
            reviewAuthorTv.setText(results.get(i).getAuthor());
            reviewsContainer.addView(view);
        }
    }

    private void setFavoriteText() {
        MoviesListingTable moviesListingDao = new MoviesListingTable(activity);
        if (moviesListingDao.isMovieFavourite(moviesResult)) {
            mMarkAsFav.setText(R.string.not_favorite);
        } else {
            mMarkAsFav.setText(R.string.mark_favorite);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mSnackBar != null)
            mSnackBar.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }
    private void toggleFavorite(){
        MoviesListingTable moviesListingDao = new MoviesListingTable(activity);
        boolean isMovieFavorited = moviesListingDao.toggleFavouriteMovie(moviesResult);
        if (isMovieFavorited)
            mSnackBar = SnackBarBuilder.make(activity.getWindow().getDecorView(), moviesResult.getTitle() +
                    getString(R.string.add_to_fav)).build();
        else {
            mSnackBar = SnackBarBuilder.make(activity.getWindow().getDecorView(), moviesResult.getTitle() +
                   getString(R.string.removed_from_favourites)).build();
        }
        setFavoriteText();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_movie_detail_favourite_tv:
                toggleFavorite();
                break;
            case R.id.see_more_reviews:
                Intent intent = new Intent(activity, ReviewsListingActivity.class);
                intent.putExtra(AppConstants.EXTRA_INTENT_PARCEL, moviesResult.getId());
                startActivity(intent);
                break;
        }
    }
}
