package com.example.android.quakereport;

import java.util.Date;

/**
 * {@link Earthquake} represent an earthquake item
 */
public class Earthquake {
    /**
     * The magnitude for the event
     */
    private final float mMagnitude;

    /**
     * Textual description of named geographic region near to the event
     */
    private final String mPlace;

    /**
     * Time when the event occurred in the epoch format
     */
    private final long mTimeInMilliseconds;

    /**
     * Website URL for the earthquake.
     */
    private final String mUrl;

    /**
     * Constructs a new {@link Earthquake} object
     *
     * @param magnitude          is the magnitude (size) of the earthquake
     * @param place              is the city location of the earthquake
     * @param timeInMilliseconds is the date the earthquake happened
     */
    public Earthquake(float magnitude, String place, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    /**
     * Return magnitude for event
     */
    public float getMagnitude() {
        return mMagnitude;
    }

    /**
     * Return description of named geographic region near to the event
     */
    public String getPlace() {
        return mPlace;
    }

    /**
     * Return date and time when the event occurred in the epoch format
     */
    public Date getDateTime() {
        return new Date(mTimeInMilliseconds);
    }

    /**
     * Return website URL for the earthquake.
     */
    public String getUrl() {
        return mUrl;
    }
}
