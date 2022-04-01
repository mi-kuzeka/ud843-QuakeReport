package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private String distanceFromPlace = getContext().getString(R.string.near_the);
    private String nameOfPlace;
    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        // Set earthquake magnitude on TextView.
        TextView magnitudeTextView = listItemView.findViewById(R.id.magnitude_text_view);
        magnitudeTextView.setText(formatMagnitude(currentEarthquake.getMagnitude()));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude.
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle.
        magnitudeCircle.setColor(magnitudeColor);

        // Split string of place to 2 parts
        splitPlaceText(currentEarthquake);

        // Set distance from place of earthquake on TextView.
        TextView distanceTextView = listItemView.findViewById(R.id.distance_text_view);
        distanceTextView.setText(distanceFromPlace);

        // Set name of place of earthquake on TextView.
        TextView placeTextView = listItemView.findViewById(R.id.place_text_view);
        placeTextView.setText(nameOfPlace);

        // Set earthquake date on TextView.
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        Date earthquakeDate = currentEarthquake.getDateTime();
        dateTextView.setText(formatDate(earthquakeDate));

        // Set earthquake time on TextView.
        TextView timeTextView = listItemView.findViewById(R.id.time_text_view);
        timeTextView.setText(formatTime(earthquakeDate));

        return listItemView;
    }

    /**
     * Return the color for the magnitude circle based on the intensity of the earthquake.
     *
     * @param magnitude of the earthquake
     */
    private int getMagnitudeColor(float magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    /**
     * Split string of the place to two part: location asset and name of place.
     */
    private void splitPlaceText(Earthquake currentEarthquake) {
        String place = currentEarthquake.getPlace();
        if (place.contains(LOCATION_SEPARATOR)) {
            String[] parts = place.split(LOCATION_SEPARATOR);
            distanceFromPlace = (parts[0] + LOCATION_SEPARATOR).trim();
            nameOfPlace = parts[1].trim();
        } else {
            nameOfPlace = place.trim();
        }
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(float magnitude) {
        if (magnitude < 10) {
            DecimalFormat format = new DecimalFormat("0.0");
            return format.format(magnitude);
        }
        DecimalFormat format = new DecimalFormat("0");
        return format.format(magnitude);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        return new SimpleDateFormat("LLL d, yyyy", Locale.getDefault()).format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(dateObject);
    }
}
