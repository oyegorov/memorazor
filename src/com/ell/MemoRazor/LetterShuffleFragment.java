package com.ell.MemoRazor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ell.MemoRazor.layouts.FlowLayout;

import java.util.Random;

public class LetterShuffleFragment extends Fragment {
    private String userInput;
    private OnInputLetterListener onInputCorrectLetter;
    private OnInputLetterListener onInputWrongLetter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lettershuffle, container, false);
    }

    public void setWord (final String word) {
        FlowLayout container = (FlowLayout)getView().findViewById(R.id.lettershuffle_container);
        container.removeAllViews();

        userInput = "";
        char[] letters = word.toCharArray();
        shuffleArray(letters);

        for (char c : letters) {
            char currentChar = Character.toUpperCase(c);

            Button button = new Button(App.getContext());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.GONE);

                    String letter = ((Button)view).getText().toString();
                    userInput +=letter;

                    if (word.toUpperCase().startsWith(userInput)) {
                        if (onInputCorrectLetter != null) {
                            onInputCorrectLetter.onInputLetter(letter, userInput);
                        }
                    } else {
                        if (onInputWrongLetter != null) {
                            onInputWrongLetter.onInputLetter(letter, userInput);
                        }
                    }
                }
            });
            button.setText(Character.toString(currentChar));
            button.setLayoutParams(new FlowLayout.LayoutParams(90, 90));

            container.addView(button);
        }
    }

    static void shuffleArray(char[] letters)
    {
        Random rnd = new Random();
        for (int i = letters.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);

            char a = letters[index];
            letters[index] = letters[i];
            letters[i] = a;
        }
    }

    public void setOnInputCorrectLetter(OnInputLetterListener onInputCorrectLetter) {
        this.onInputCorrectLetter = onInputCorrectLetter;
    }

    public void setOnInputWrongLetter(OnInputLetterListener onInputWrongLetter) {
        this.onInputWrongLetter = onInputWrongLetter;
    }

    public static interface OnInputLetterListener {
        void onInputLetter(String letter, String word);
    }
}
