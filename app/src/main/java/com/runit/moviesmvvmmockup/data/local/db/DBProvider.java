package com.runit.moviesmvvmmockup.data.local.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;


/**
 * Class used for retrieving reference to database.
 */
public class DBProvider {
    private static DBProvider mInstance;
    // Db instance
    private MovieDatabase mDb;

    public static DBProvider getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBProvider.class) {
                if (mInstance == null) {
                    mInstance = new DBProvider(context);
                }
            }
        }
        return mInstance;
    }

    private DBProvider(Context context) {
        mDb = Room.databaseBuilder(context, MovieDatabase.class, "Movie4UDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
    }

}
