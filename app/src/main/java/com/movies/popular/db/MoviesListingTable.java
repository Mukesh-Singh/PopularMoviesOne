package com.movies.popular.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.movies.popular.model.movie_api.MoviesResponseBean;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

/**
 * Created by mukesh on 28/3/16
 */
public class MoviesListingTable extends DBHelper {

    private static final String TAG = MoviesListingTable.class.getSimpleName();

    // Table Name
    public static final String TABLE_NAME = "tbl_movies_listing";
    // Column Names
    public static final String COL_POSTER_PATH = "col_poster_path";
    public static final String COL_OVERVIEW = "col_overview";
    public static final String COL_RELEASE_DATE = "col_release_date";
    public static final String COL_ID = "col_id";
    public static final String COL_IS_FAVORITE = "col_is_favorite";
    public static final String COL_TITLE = "col_title";
    public static final String COL_VOTE_AVERAGE = "col_vote_average";
    public static final String COL_RUNTIME = "col_runtime";
    public static final String COL_TAGLINE = "col_tagline";

    public static final String CREATE_MOVIES_LISTING_TABLE = " CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_POSTER_PATH + " TEXT NOT NULL, " +
            COL_OVERVIEW + " TEXT NOT NULL, " +
            COL_RELEASE_DATE + " TEXT NOT NULL, " +
            COL_ID + " TEXT NOT NULL, " +
            COL_IS_FAVORITE + " TEXT NOT NULL, " +
            COL_TITLE + " TEXT NOT NULL, " +
            COL_VOTE_AVERAGE + " TEXT NOT NULL, " +
            COL_RUNTIME + " TEXT NOT NULL, " +
            COL_TAGLINE + " TEXT NOT NULL)";

    private Context mContext = null;
    SQLiteDatabase localSQLiteDatabase = null;

    public MoviesListingTable(Context paramContext) {
        this.mContext = paramContext;
        localSQLiteDatabase = openDB();
    }

    @Override
    public SQLiteDatabase openDB() {
        return MoviesHelper.getDatabaseHelperInstance(mContext)
                .getReadableDatabase();
    }

    public boolean toggleFavouriteMovie(MoviesResponseBean.MoviesResult moviesResult) {
        boolean isMovieFavourite = false;
        ContentValues values = new ContentValues();
        values.put(COL_POSTER_PATH, moviesResult.getPosterPath());
        values.put(COL_OVERVIEW, moviesResult.getOverview());
        values.put(COL_RELEASE_DATE, moviesResult.getReleaseDate());
        values.put(COL_ID, moviesResult.getId());
        values.put(COL_IS_FAVORITE, "1");
        values.put(COL_TITLE, moviesResult.getTitle());
        values.put(COL_VOTE_AVERAGE, moviesResult.getVoteAverage());
        values.put(COL_RUNTIME, moviesResult.getRuntime());
        values.put(COL_TAGLINE, moviesResult.getTagline());
        if (isMovieFavourite(moviesResult)) {
            long key = localSQLiteDatabase.delete(TABLE_NAME, COL_ID + " =?", new String[]{moviesResult.getId() + ""});
            Log.i(TAG, "Row Deleted :- " + key);
        } else {
            long key = localSQLiteDatabase.insert(
                    TABLE_NAME, null, values
            );
            isMovieFavourite = true;
            Log.i(TAG, "Row Inserted :- " + key);

        }
        return isMovieFavourite;
    }

    public boolean isMovieFavourite(MoviesResponseBean.MoviesResult moviesResult) {
        boolean isMovieFavourite = false;
        Cursor cursor = null;
        try {
            cursor = localSQLiteDatabase.query(TABLE_NAME, null, COL_ID + " =?",
                    new String[]{moviesResult.getId() + ""}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String favourite = cursor.getString(cursor.getColumnIndex(COL_IS_FAVORITE));
                    if (TextUtils.isEmpty(favourite))
                        isMovieFavourite = false;
                    else
                        isMovieFavourite = true;
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return isMovieFavourite;
    }

    public ArrayList<MoviesResponseBean.MoviesResult> getFavouriteMovieList() {
        Cursor cursor = null;
        ArrayList<MoviesResponseBean.MoviesResult> moviesResultArrayList = new ArrayList<>();
        try {
            cursor = localSQLiteDatabase.query(TABLE_NAME, null, null,
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    MoviesResponseBean.MoviesResult moviesResult = new MoviesResponseBean.MoviesResult();
                    moviesResult.setIsFavorite(cursor.getString(cursor
                            .getColumnIndex(COL_IS_FAVORITE)));
                    moviesResult.setId(Long.parseLong(cursor.getString(cursor
                            .getColumnIndex(COL_ID))));
                    moviesResult.setOverview(cursor.getString(cursor
                            .getColumnIndex(COL_OVERVIEW)));
                    moviesResult.setPosterPath(cursor.getString(cursor
                            .getColumnIndex(COL_POSTER_PATH)));
                    moviesResult.setReleaseDate(cursor.getString(cursor
                            .getColumnIndex(COL_RELEASE_DATE)));
                    moviesResult.setTitle(cursor.getString(cursor
                            .getColumnIndex(COL_TITLE)));
                    moviesResult.setVoteAverage(Double.parseDouble(cursor.getString(cursor
                            .getColumnIndex(COL_VOTE_AVERAGE))));
                    moviesResultArrayList.add(moviesResult);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return moviesResultArrayList;
    }
}
