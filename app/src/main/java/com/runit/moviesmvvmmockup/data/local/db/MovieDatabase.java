package com.runit.moviesmvvmmockup.data.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.runit.moviesmvvmmockup.data.local.dao.MovieDao;
import com.runit.moviesmvvmmockup.data.model.Account;
import com.runit.moviesmvvmmockup.data.model.MovieModel;


@Database(entities = {MovieModel.class, Account.class},
        version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    abstract MovieDao movieDao();
}
