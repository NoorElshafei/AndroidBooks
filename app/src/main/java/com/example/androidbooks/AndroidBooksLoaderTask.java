package com.example.androidbooks;

import android.content.Context;

import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class AndroidBooksLoaderTask extends AsyncTaskLoader<ArrayList<AndroidBooksModel>> {

    public static final String LOG_TAG = AndroidBooksLoaderTask.class.getName();
    private String murl;

    public AndroidBooksLoaderTask(Context context,String murl) {
        super(context);
        Log.i(LOG_TAG, "AndroidBooksLoaderTask");
        this.murl=murl;
    }

    @Override
    public ArrayList<AndroidBooksModel> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground");

        if (murl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of AndroidBooks.
        ArrayList<AndroidBooksModel> bookList=AndroidBooksGetterTask.fetchEarthquakeData(murl);
        return bookList;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading");
        forceLoad();
    }

}
