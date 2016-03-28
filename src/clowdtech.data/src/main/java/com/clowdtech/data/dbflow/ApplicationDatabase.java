package com.clowdtech.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = ApplicationDatabase.NAME, version = ApplicationDatabase.VERSION)
public class ApplicationDatabase {
    public static final String NAME = "Application";

    public static final int VERSION = 38;
}
