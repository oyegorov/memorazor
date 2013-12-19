package com.ell.MemoRazor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ell.MemoRazor.R;

import java.util.ArrayList;

public class MainActionsAdapter extends ArrayAdapter<String> {
    private ArrayList<String> actions;
    private ArrayList<Integer> images;

    public MainActionsAdapter(Context context, int resource, ArrayList<String> actions) {
        super(context, resource, actions);
        this.actions = actions;

        images = new ArrayList<Integer>();
        images.add(R.drawable.group);
        images.add(R.drawable.quiz);
        images.add(R.drawable.cards);
        images.add(R.drawable.exportimport);
        images.add(R.drawable.preferences);
        images.add(R.drawable.about);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_action_layout, null);
        }

        String action = actions.get(position);

        if (action != null) {
            TextView actionNameTextView = (TextView)convertView.findViewById(R.id.action_name_text);
            ImageView actionImage = (ImageView)convertView.findViewById(R.id.action_image);

            actionImage.setImageResource(images.get(position));
            actionNameTextView.setText(action);
        }

        return convertView;
    }
}
