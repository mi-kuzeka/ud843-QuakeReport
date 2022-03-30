package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
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
        magnitudeTextView.setText(String.valueOf(currentEarthquake.getMagnitude()));

        // Set place of earthquake on TextView.
        TextView placeTextView = listItemView.findViewById(R.id.place_text_view);
        placeTextView.setText(currentEarthquake.getPlace());

        // Set earthquake magnitude on TextView.
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        Date earthquakeDate = currentEarthquake.getDate();
        dateTextView.setText( new SimpleDateFormat("MMM d, yyyy",
                Locale.getDefault()).format(earthquakeDate));

        return listItemView;
    }
}
