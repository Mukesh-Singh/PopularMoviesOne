package com.movies.popular.fragments;

/**
 * Created by mukesh on 1/2/16.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.movies.popular.api.ApiConstants;
import com.movies.popular.api.RestClient;
import com.movies.popular.base.ApplicationController;
import com.movies.popular.model.movie_api.MoviesResponseBean;
import com.movies.popular.one.R;
import com.movies.popular.utility.AppConstants;
import com.movies.popular.utility.SnackBarBuilder;
import com.movies.popular.utility.Utility;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle= getArguments();
        moviesResult=bundle.getParcelable(AppConstants.EXTRA_INTENT_PARCEL);
        //getActivity().setTitle(moviesResult.getTitle()+"");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intUI(view);
        getMoviesDetail();
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
                    .with(getActivity())
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
            mSnackBar = SnackBarBuilder.make(getActivity().getWindow().getDecorView(), getString(R.string.no_internet_connection))
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_movie_detail_favourite_tv:
                mSnackBar = SnackBarBuilder.make(getActivity().getWindow().getDecorView(), getString(R.string.feature_in_stage_two)).build();
                break;
        }
    }
}
