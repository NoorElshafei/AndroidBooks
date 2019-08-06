package com.example.androidbooks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<AndroidBooksModel>> {

    private static final String BOOK_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int BOOKS_LOADER_ID = 1;

    public static final String LOG_TAG = MainActivity.class.getName();
    private AndroidBooksAdapter androidBooksAdapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private ListView listView;
    private FloatingSearchView searchBarView;
    private ImageView iconState;
    private Bundle bundle;
    private static final String SEARCH_RESULTS = "booksSearchResults";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate MainActivity");

        //link XML Views with Variables
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.loading_spinner);
        emptyTextView = findViewById(R.id.empty_view);
        searchBarView = findViewById(R.id.floating_search_view);
        iconState = findViewById(R.id.search_icon);

        Bundle bundle1=new Bundle();
        bundle1.putString("d","j");
        savedInstanceState=bundle1;

        if (isNetworkAvailable()) {

            if (savedInstanceState.getString("d")=="j") {
                Log.i(LOG_TAG, "savedInstanceState==null");


                progressBar.setVisibility(View.GONE);
                emptyTextView.setText("Find your next book!");

            } else{
                Log.i(LOG_TAG, "savedInstanceState==not null");

                getSupportLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);


            }
        } else {
            Log.i(LOG_TAG, "No internet connection");

            progressBar.setVisibility(View.GONE);
            iconState.setImageResource(R.drawable.ic_cloud_off);
            emptyTextView.setText(R.string.no_internet_connection);
        }


        //Declare object from androidBooksAdapter class and active set adapter method
        androidBooksAdapter = new AndroidBooksAdapter(this, 0, new ArrayList<AndroidBooksModel>());
        listView.setAdapter(androidBooksAdapter);


        //when any List is pressed this listener call back
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AndroidBooksModel currentBook = androidBooksAdapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getBookLink()));
                startActivity(intent);
            }
        });

        setupSearchBar();

    }


    @Override
    public Loader<ArrayList<AndroidBooksModel>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "onCreateLoader");

        // Check if the Bundel is null ,
        // means if the user search before, the bundle will be not null and carry search word
        //and if not , the user did not search before
        if (bundle == null) {
            // Create a new loader for the given URL
            return new AndroidBooksLoaderTask(this, null);
        } else {
            String s = bundle.getString("searchString");
            return new AndroidBooksLoaderTask(this, BOOK_API_URL + s);

        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AndroidBooksModel>> loader, ArrayList<AndroidBooksModel> androidBooksModels) {
        Log.i(LOG_TAG, "onLoadFinished");

        progressBar.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        androidBooksAdapter.clear();

        // If there is a valid list of {@link androidBooksModels}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.


        if (androidBooksModels != null) {
            iconState.setVisibility(View.GONE);
            androidBooksAdapter.addAll(androidBooksModels);
        } else {
            // Set empty state text to display "No Books found."
            emptyTextView.setText(R.string.Book_not_found);
            iconState.setVisibility(View.VISIBLE);
            iconState.setImageResource(R.drawable.ic_block);
            listView.setEmptyView(emptyTextView);


        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AndroidBooksModel>> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
        // Loader reset, so we can clear out our existing data.
        androidBooksAdapter.clear();

    }


    //Internet Connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i(LOG_TAG, "activeNetworkInfo = " + activeNetworkInfo);
        return activeNetworkInfo != null;
    }

    /**
     * Sets up listeners for search bar
     */
    public void setupSearchBar() {

        searchBarView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction(String s) {


                //Check Network Connection
                if (isNetworkAvailable()) {
                    Log.i(LOG_TAG, "NetworkAvailable");

                    Log.i(LOG_TAG, "onQueryTextSubmit");

                     bundle = new Bundle();
                    bundle.putString("searchString", s);



                    androidBooksAdapter.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    iconState.setVisibility(View.GONE);
                    listView.setAdapter(androidBooksAdapter);

                    getSupportLoaderManager().restartLoader(BOOKS_LOADER_ID, bundle, MainActivity.this);

                } else {
                    Log.i(LOG_TAG, "No internet connection");

                    progressBar.setVisibility(View.GONE);
                    iconState.setImageResource(R.drawable.ic_cloud_off);
                    emptyTextView.setText(R.string.no_internet_connection);

                }

            }
        });
    }


}


