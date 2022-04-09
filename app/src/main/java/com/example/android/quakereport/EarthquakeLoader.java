package com.example.android.quakereport;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if (TextUtils.isEmpty(mUrl)) {
            return null;
        }
        /** Create the list of earthquake from {@link QueryUtils} */
        return QueryUtils.fetchEarthquakesData(mUrl);
    }
}
