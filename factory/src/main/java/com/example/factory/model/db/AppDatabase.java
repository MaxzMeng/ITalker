package com.example.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by mxz on 18-3-17.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
