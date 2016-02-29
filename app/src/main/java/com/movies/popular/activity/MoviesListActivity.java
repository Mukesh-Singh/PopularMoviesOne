package com.movies.popular.activity;
/**
 * Created by mukesh on 1/2/16.
 */

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.movies.popular.base.BaseActivity;
import com.movies.popular.one.R;
import com.movies.popular.fragments.MoviesListFragment;

public class MoviesListActivity extends BaseActivity {

    private FragmentManager mManager;
    private MoviesListFragment mMoviesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        intUI();
    }

    private void intUI() {
        setTitle(getString(R.string.title_activity_movies_list));
        mManager = getFragmentManager();
        if (mMoviesListFragment==null)
            mMoviesListFragment = new MoviesListFragment();
        mManager.beginTransaction()
                .replace(R.id.list_container, mMoviesListFragment, "listfragment")
                .addToBackStack(null)
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.most_popular) {
//            return true;
//        }
//        else if (id == R.id.highest_rated) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
