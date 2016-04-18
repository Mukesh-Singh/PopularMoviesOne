package com.movies.popular.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author mukesh on 28/3/16
 */
public class MoviesHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popular_movies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = MoviesHelper.class.getSimpleName();

    private static MoviesHelper databaseHelperInstance;

    public static synchronized MoviesHelper getDatabaseHelperInstance(
            Context context) {
        if (databaseHelperInstance == null)
            databaseHelperInstance = new MoviesHelper(context);

        return databaseHelperInstance;
    }

    private MoviesHelper(Context paramContext) {
        super(paramContext, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper Constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL(MoviesListingTable.CREATE_MOVIES_LISTING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DatabaseHelper onUpgrade called");
    }

    public static int getDataBaseVersion() {
        return DATABASE_VERSION;
    }

}
