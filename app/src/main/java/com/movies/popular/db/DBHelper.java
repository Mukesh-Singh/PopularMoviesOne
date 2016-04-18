package com.movies.popular.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class DBHelper {

	public abstract SQLiteDatabase openDB();
}
