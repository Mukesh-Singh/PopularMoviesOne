package com.movies.popular.fragments;
/**
 * Created by mukesh on 1/2/16.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.movies.popular.activity.MovieDetailActivity;
import com.movies.popular.adapters.MoviesListAdapter;
import com.movies.popular.api.ApiConstants;
import com.movies.popular.api.RestClient;
import com.movies.popular.base.ApplicationController;
import com.movies.popular.base.BaseActivity;
import com.movies.popular.db.MoviesListingTable;
import com.movies.popular.model.movie_api.MoviesResponseBean;
import com.movies.popular.one.R;
import com.movies.popular.utility.AppConstants;
import com.movies.popular.utility.EndlessScrollGridLayoutManager;
import com.movies.popular.utility.RecyclerViewWithEmptySupport;
import com.movies.popular.utility.SnackBarBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MoviesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ProgressBar mProgressBar;
    private MoviesListAdapter mAdapter;
    private RecyclerViewWithEmptySupport mMoviesListRecyclerView;
    private int mPagination = 1;
    private ArrayList<MoviesResponseBean.MoviesResult> moviesResultsList = new ArrayList<>();
    private String mSortByParam = ApiConstants.POPULARITY_DESC;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isSortApplied;
    protected Snackbar mSnackBar;
    final int mSpanCount=2;
    private BaseActivity activity;
    private View view;

    private EndlessScrollGridLayoutManager mGridLayoutManager;
    private MovieDetailFragment mMovieDetailFragment;

    private static final String DATA="data";
    private static final String SELECTED_POSITION="selected_position";
    private int mSelectedPosition;


    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof BaseActivity)
            this.activity=(BaseActivity)activity;

        super.onAttach(activity);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_POSITION,mSelectedPosition);
        outState.putParcelableArrayList(DATA,moviesResultsList);
        super.onSaveInstanceState(outState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        intUI(view);
        if (savedInstanceState!=null){
            mSelectedPosition=savedInstanceState.getInt(SELECTED_POSITION);
            moviesResultsList.clear();
            ArrayList<MoviesResponseBean.MoviesResult> p=savedInstanceState.getParcelableArrayList(DATA);
            moviesResultsList.addAll(p);
            setListAdapter();
            if (activity.mTwoPane){
                //showMoviesDetails(mSelectedPosition);
            }
        }
        else {
            getMoviesList(View.VISIBLE);
        }


    }


    @Override
    public void onPause() {
        super.onPause();
        if (mSnackBar != null)
            mSnackBar.dismiss();
    }

    private void intUI(View view)
    {
        mMoviesListRecyclerView = (RecyclerViewWithEmptySupport) view.findViewById(R.id.movies_list_recycler_view);
        // use a Grid layout manager

        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mProgressBar=(ProgressBar)view.findViewById(R.id.progressBar);
       mGridLayoutManager = new EndlessScrollGridLayoutManager(getActivity(),mSpanCount) {
        @Override
        public void onBottomReached() {
            if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
                mPagination = mPagination + 1;
                getMoviesList(View.VISIBLE);
            } else
                mSnackBar = SnackBarBuilder.make(getActivity().getWindow().getDecorView(), getString(R.string.no_internet_connection)).build();

        }

        @Override
        public void onTopReached() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onScrollUp() {

        }
    } ;
        mMoviesListRecyclerView.setLayoutManager(mGridLayoutManager);
        mMoviesListRecyclerView.setEmptyView(view.findViewById(R.id.list_empty));




    }

    private void setListAdapter() {
        if (mAdapter == null) {
            mAdapter = new MoviesListAdapter(getActivity(),moviesResultsList) {
                @Override
                public void onItemClick(int position) {
                    super.onItemClick(position);
                    showMoviesDetails(position);

                }
            };
            mMoviesListRecyclerView.setAdapter(mAdapter);
            mMoviesListRecyclerView.scheduleLayoutAnimation();

        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showMoviesDetails(int position){
        mSelectedPosition =position;
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.EXTRA_INTENT_PARCEL, moviesResultsList.get(position));
        if (activity.mTwoPane){
            mMovieDetailFragment = new MovieDetailFragment();
            mMovieDetailFragment.setRetainInstance(true);
            mMovieDetailFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, mMovieDetailFragment, "detailfragment")
                    .commit();
        }
        else {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtras(bundle);
            Pair<View, String> p6 = Pair.create(view, "row");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p6);
            startActivity(intent,options.toBundle());
        }

    }



    private void getMoviesList(int progressBarVisibility) {
        if (mSortByParam.equals(ApiConstants.MY_FAVORITES)) {
            MoviesListingTable moviesListingDao = new MoviesListingTable(activity);
            mMoviesListRecyclerView.setOnClickListener(null);
            moviesResultsList.addAll(moviesListingDao.getFavouriteMovieList());
            setListAdapter();
            mProgressBar.setVisibility(View.GONE);
        } else if (ApplicationController.getApplicationInstance().isNetworkConnected()) {
            mProgressBar.setVisibility(progressBarVisibility);
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put(ApiConstants.PARAM_SORT_BY, mSortByParam);
            stringHashMap.put(ApiConstants.PARAM_API_KEY, ApiConstants.API_KEY);
            stringHashMap.put(ApiConstants.PARAM_PAGE, mPagination + "");

            Call<MoviesResponseBean> beanCall = RestClient.getInstance().getApiServices().apiMoviesList(stringHashMap);
            beanCall.enqueue(new Callback<MoviesResponseBean>() {
                @Override
                public void onResponse(Response<MoviesResponseBean> response, Retrofit retrofit) {
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        MoviesResponseBean responseBean = response.body();
                        moviesResultsList.addAll(responseBean.getResults());
                        setListAdapter();


                        if (responseBean.getResults().isEmpty())
                            Log.i("Retro", response.toString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("Retro", t.toString());
                }
            });
        } else {
            mSnackBar = SnackBarBuilder.make(getActivity().getWindow().getDecorView(), getString(R.string.no_internet_connection))
                    .setActionText(getString(R.string.retry))
                    .onSnackBarClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMoviesList(View.VISIBLE);
                        }
                    })
                    .build();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                isSortApplied = true;
                mSortByParam = ApiConstants.POPULARITY_DESC;
                onRefresh();
                return true;
            case R.id.highest_rated:
                mSortByParam = ApiConstants.HIGHEST_RATED;
                isSortApplied = true;
                onRefresh();
                return true;
            case R.id.my_favorites:
                mSortByParam = ApiConstants.MY_FAVORITES;
                isSortApplied = true;
                onRefresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeExitingDetailFragment(){
        if (activity.mTwoPane){
            getFragmentManager().beginTransaction()
                    .remove(mMovieDetailFragment)
                    .commit();
        }

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
            mPagination = 1;
            moviesResultsList.clear();
            removeExitingDetailFragment();
            try {
                mAdapter.notifyDataSetChanged();
                mAdapter = null;
            } catch (Exception ignored) {
            }

            if (isSortApplied) {
                isSortApplied = false;
            } else {
                mSortByParam = ApiConstants.POPULARITY_DESC;
            }

            getMoviesList(View.VISIBLE);
        }
}
