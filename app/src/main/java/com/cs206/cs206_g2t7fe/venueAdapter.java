package com.cs206.cs206_g2t7fe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

public class venueAdapter extends ArrayAdapter<venueInformation> {
    public venueAdapter(Context context, ArrayList<venueInformation> eventList) {
        super(context, 0, eventList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        venueInformation venueInformation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.venue_event, parent, false);
        }

        TextView venueNameView = convertView.findViewById(R.id.textView_venue_name);
        TextView venueAddressView = convertView.findViewById(R.id.textView_venue_address);


        venueNameView.setText(venueInformation.getLocationName());
        venueAddressView.setText(venueInformation.getVenueAddress());

        return convertView;
    }
}
