package com.ell.MemoRazor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ell.MemoRazor.R;
import com.ell.MemoRazor.data.Word;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActionsAdapter extends ArrayAdapter<String> {
    private ArrayList<String> objects;

    public MainActionsAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_action_layout, null);
        }

        String action = objects.get(position);

        if (action != null) {
            TextView actionNameTextView = (TextView)convertView.findViewById(R.id.action_name_text);

            actionNameTextView.setText(action);
        }

        return convertView;
    }
}
