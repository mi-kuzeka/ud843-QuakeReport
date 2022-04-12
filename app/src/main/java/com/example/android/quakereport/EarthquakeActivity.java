/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;

import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * Adapter for the list of earthquakes
     */
    private EarthquakeAdapter mAdapter;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=20";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyView;
    private TextView mNoInternetView;

    private boolean noInternet = false;
    private boolean hasEarthquakes = false;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mContext = this;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        mEmptyView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyView);

        mNoInternetView = findViewById(R.id.no_internet);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(mContext, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                try {
                    // Send the intent to launch a new activity
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            R.string.browser_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        });

        noInternet = !isOnline(mContext);
        if (noInternet) {
            // Hide loading indicator because the data can't been loaded
            hideLoadingSpinner();

            setNoInternetText();
            return;
        }

        LoaderManager loaderManager = getSupportLoaderManager();

        Log.v(LOG_TAG, "initLoader");
        // Prepare the loader. Either re-connect with an existing one, or start a new one.
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    private static boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem checking internet connection", e);
        }
        return false;
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(LOG_TAG, "TEST: onCreateLoader");
        return new EarthquakeLoader(mContext, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.v(LOG_TAG, "TEST: onLoadFinished");

        noInternet = !isOnline(mContext);
        if (noInternet) {
            return;
        }

        // Hide loading indicator because the data has been loaded
        hideLoadingSpinner();

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        hasEarthquakes = earthquakes != null && !earthquakes.isEmpty();
        if (hasEarthquakes) {
            mAdapter.addAll(earthquakes);
        } else {
            setEmptyViewText();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        Log.v(LOG_TAG, "TEST: onLoaderReset");
        mAdapter.clear();
    }

    private void hideLoadingSpinner() {
        ProgressBar loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);
    }

    // Set empty state text to display "No earthquakes found."
    private void setEmptyViewText() {
        mEmptyView.setText(R.string.no_earthquakes);
        hasEarthquakes = false;
    }

    // Set empty state text to display "No earthquakes found."
    private void setNoInternetText() {
        mNoInternetView.setText(R.string.no_internet);
    }
}
