package com.cs206.cs206_g2t7fe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<EventsDisplay> {
    public EventAdapter(Context context, ArrayList<EventsDisplay> eventList) {
        super(context, 0, eventList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventsDisplay event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.textView_event_name);

        nameView.setText(event.getName());

        return convertView;
    }
}