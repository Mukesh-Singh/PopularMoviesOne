package com.movies.popular.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.movies.popular.adapters.ReviewsListAdapter;
import com.movies.popular.api.ApiConstants;
import com.movies.popular.api.RestClient;
import com.movies.popular.base.ApplicationController;
import com.movies.popular.base.BaseActivity;
import com.movies.popular.model.review_api.ReviewsListingResponse;
import com.movies.popular.one.R;
import com.movies.popular.utility.EndlessScrollListener;
import com.movies.popular.utility.SnackBarBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by mukesh on 30/3/16.
 */
public class ReviewsListingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String DATA = "data";
    private static final String PAGE_NUMBER = "page_number";
    private ListView mReviewsListView;
    private int mPagination = 1;
    private ProgressBar progressBar;
    private Snackbar mSnackBar;
    EndlessScrollListener mEndlessScrollListener = new EndlessScrollListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
                mPagination = mPagination + 1;
                getMovieReviews();
            } else
                mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connection)).build();

        }
    };
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private long movieId;
    private ArrayList<ReviewsListingResponse.ReviewsEntity> moviesReviewsList = new ArrayList<>();
    private ReviewsListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_listing);
        setTitle(getString(R.string.reviews));
        initUi();
        if (savedInstanceState!=null){
            mPagination=savedInstanceState.getInt(PAGE_NUMBER);
            moviesReviewsList.clear();
            ArrayList<ReviewsListingResponse.ReviewsEntity> p=savedInstanceState.getParcelableArrayList(DATA);
            moviesReviewsList.addAll(p);
            updateData();
        }
        else {
            getMovieReviews();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(DATA,moviesReviewsList);
        outState.putInt(PAGE_NUMBER,mPagination);
        super.onSaveInstanceState(outState);
    }

    public void initUi() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        movieId = getIntent().getLongExtra(ApiConstants.EXTRA_INTENT_PARCEL, 0);
        mReviewsListView = (ListView) findViewById(R.id.reviews_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.review_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        progressBar = (ProgressBar)findViewById(R.id.review_progressBar);

    }

    private void getMovieReviews() {
        if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY);
            stringHashMap.put(ApiConstants.PARAM_PAGE, mPagination + "");

            Call<ReviewsListingResponse> beanCall = RestClient.getInstance().getApiServices().apiMovieReviews(movieId, stringHashMap);
            beanCall.enqueue(new Callback<ReviewsListingResponse>() {
                @Override
                public void onResponse(Response<ReviewsListingResponse> response1, Retrofit retrofit) {
                    progressBar.setVisibility(View.GONE);
                    ReviewsListingResponse responseBean = response1.body();
                    if (responseBean != null) {
                        moviesReviewsList.addAll(responseBean.getResults());
                       updateData();

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connection))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMovieReviews();
                        }
                    })
                    .build();
        }
    }

    private void updateData(){

        if (mPagination == 1) {
            mReviewsListView.setOnScrollListener(mEndlessScrollListener);
        }

        if (mAdapter == null) {
            mAdapter = new ReviewsListAdapter(ReviewsListingActivity.this, moviesReviewsList);
            mReviewsListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (moviesReviewsList.size() == 0) {
            mReviewsListView.setOnScrollListener(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
            mPagination = 1;
            mReviewsListView.setOnScrollListener(null);
            moviesReviewsList.clear();
            try {
                mAdapter.notifyDataSetChanged();
                mAdapter = null;
            } catch (Exception ignored) {
            }

            getMovieReviews();
        } else {
            mSnackBar = SnackBarBuilder.make(getWindow().getDecorView(), getString(R.string.no_internet_connection)).build();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSnackBar!=null)
            mSnackBar.dismiss();

    }
}
