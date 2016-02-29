package com.movies.popular.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.movies.popular.utility.Utility;

/**
 * Created by mukesh on 29/2/16.
 */
public class ApplicationController extends Application implements Application.ActivityLifecycleCallbacks {
    private boolean mIsNetworkConnected;
    private static ApplicationController mApplicationInstance;

    public static ApplicationController getApplicationInstance() {
        if (mApplicationInstance == null)
            mApplicationInstance = new ApplicationController();
        return mApplicationInstance;
    }

    /**
     * Method to tell the state of internet connectivity
     *
     * @return State of internet
     */
    public boolean isNetworkConnected() {
        return mIsNetworkConnected;
    }

    public void setIsNetworkConnected(boolean mIsNetworkConnected) {
        this.mIsNetworkConnected = mIsNetworkConnected;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        mApplicationInstance = this;
        mIsNetworkConnected = Utility.getNetworkState(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
