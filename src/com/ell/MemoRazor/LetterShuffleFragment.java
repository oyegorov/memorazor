package com.ell.MemoRazor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.layouts.FlowLayout;

public class LetterShuffleFragment extends Fragment {
    private String userInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lettershuffle, container, false);
    }

    public void setWord (String word) {
        FlowLayout container = (FlowLayout)getView().findViewById(R.id.lettershuffle_container);
        container.removeAllViews();

        userInput = "";
        for (char c : word.toCharArray()) {
            char currentChar = Character.toUpperCase(c);

            Button button = new Button(App.getContext());
            button.setText(Character.toString(currentChar));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.GONE);
                }
            });

            container.addView(button);
        }
    }
}
