package com.ell.MemoRazor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.ell.MemoRazor.R;
import com.ell.MemoRazor.data.WordGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WordGroupSelectionAdapter extends ArrayAdapter<WordGroup> {
    private ArrayList<WordGroup> selectedObjects;
    private ArrayList<WordGroup> objects;

    CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                selectedObjects.add((WordGroup)compoundButton.getTag());
            } else {
                selectedObjects.remove(compoundButton.getTag());
            }
        }
    };

    public ArrayList<WordGroup> getSelectedWordGroups() {
        return selectedObjects;
    }

    public WordGroupSelectionAdapter(Context context, int resource, ArrayList<WordGroup> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.selectedObjects = new ArrayList<WordGroup>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.word_group_selection_layout, null);
        }

        WordGroup wordGroup = objects.get(position);

        if (wordGroup != null) {
            CheckBox groupNameCheckbox = (CheckBox)convertView.findViewById(R.id.selection_group_name_text);
            groupNameCheckbox.setText(wordGroup.getName());
            groupNameCheckbox.setTag(wordGroup);
            groupNameCheckbox.setOnCheckedChangeListener(checkListener);
        }

        return convertView;
    }
}
