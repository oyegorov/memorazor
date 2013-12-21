package com.ell.MemoRazor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ell.MemoRazor.data.QuizAnswer;
import com.ell.MemoRazor.data.Word;
import com.ell.MemoRazor.helpers.LanguageHelper;
import com.ell.MemoRazor.helpers.WordPlaybackManager;

import java.util.ArrayList;
import java.util.Random;

public class SimpleQuizActivity extends MemoRazorActivity {
    public static final String EXTRA_QUIZ_ANSWERS = "com.ell.QUIZ_ANSWERS";

    private Random random = new Random(System.currentTimeMillis());
    private int currentStep = 1;
    private int totalSteps;
    private long currentQuestionStartTime;

    private Word currentWord;
    private ArrayList<QuizAnswer> answers;
    private ArrayList<Integer> availableIndices;
    private ArrayList<Word> allWords;
    private TextView quizWordNumber;
    private TextView quizTranslation;
    private Button quizNext;
    private Button quizSkip;
    private TextView quizHint;
    private ImageView quizLang;
    private LetterShuffleFragment quizShuffledWord;
    private WordPlaybackManager playbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplequiz);
    }

    @Override
    protected void configureActionBar(ActionBar actionBar) {
        super.configureActionBar(actionBar);
        actionBar.setIcon(R.drawable.quiz);
    }

    @Override
    protected void bindControls() {
        super.bindControls();

        quizWordNumber = (TextView) findViewById(R.id.quizWordNumber);
        quizTranslation = (TextView) findViewById(R.id.quizTranslation);
        quizNext = (Button) findViewById(R.id.quizNext);
        quizSkip = (Button) findViewById(R.id.quizSkip);
        quizHint = (TextView) findViewById(R.id.quizHint);
        quizLang = (ImageView) findViewById(R.id.quizLang);
        quizShuffledWord = (LetterShuffleFragment)getSupportFragmentManager().findFragmentById(R.id.quizShuffledWord);
    }

    @Override
    protected void initialize() {
        super.initialize();

        answers = new ArrayList<QuizAnswer>();
        allWords = (ArrayList<Word>) getIntent().getSerializableExtra(WordGroupsSelectionActivity.EXTRA_SELECTED_WORDS);
        availableIndices = new ArrayList<Integer>();
        for (int i = 0; i < allWords.size(); i++) {
            availableIndices.add(i);
        }

        playbackManager = new WordPlaybackManager(getHelper(), null);

        loadNewWord();

        addShuffledWordButtonsHandlers();
        addQuizNextHandler();
        addQuizSkipHandler();
        addQuizNextWordHandler();

        currentQuestionStartTime = System.currentTimeMillis();
    }

    private void addQuizNextWordHandler() {
        quizNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextWord(!quizShuffledWord.getUserInput().equalsIgnoreCase(currentWord.getName()));
            }
        });
    }

    private void inputLetter(String word) {
        quizHint.setText(word.toUpperCase());

        if (word.length() == currentWord.getName().length()) {
            if (currentWord.getName().equalsIgnoreCase(word)) {
                finishWordInput(quizShuffledWord.noErrors());
            } else {
                finishWordInput(false);
            }
        }
    }

    private void addShuffledWordButtonsHandlers() {
        quizShuffledWord.setOnInputCorrectLetter(new LetterShuffleFragment.OnInputLetterListener() {
            @Override
            public void onInputLetter(String letter, String word) {
                inputLetter(word);
            }
        });
    }

    private void addQuizSkipHandler() {
        quizSkip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finishWordInput(false);
                    }
                });
    }

    private void finishWordInput(boolean inputCorrect) {
        playbackManager.playWord(currentWord, true);

        quizHint.setTextColor(inputCorrect ? Color.GREEN : Color.RED);
        quizHint.setText(currentWord.getName().toUpperCase());
        quizNext.setVisibility(View.VISIBLE);
        quizSkip.setVisibility(View.GONE);

        getSupportFragmentManager().beginTransaction().hide(quizShuffledWord).commit();
    }

    private void addQuizNextHandler() {
        quizNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextWord(currentWord.getName().equalsIgnoreCase(quizShuffledWord.getUserInput()) && quizShuffledWord.noErrors());
            }
        });
    }

    private void nextWord(Boolean skip) {
        if (currentStep <= totalSteps) {
            QuizAnswer answer = new QuizAnswer();
            answer.setElapsedMilliseconds(System.currentTimeMillis() - currentQuestionStartTime);
            answer.setWord(currentWord);
            answer.setQuestionNumber(currentStep);
            if (skip) {
                answer.setCorrect(false);
                answer.setSkipped(true);
            } else {
                answer.setSkipped(false);
                answer.setProposedAnswer(quizShuffledWord.getUserInput().toString().trim());
                answer.setCorrect(quizShuffledWord.noErrors());
            }
            answers.add(answer);

            if (currentStep == totalSteps) {
                Intent quizResultsIntent = new Intent(this, QuizResultsActivity.class);
                quizResultsIntent.putExtra(EXTRA_QUIZ_ANSWERS, answers);
                startActivity(quizResultsIntent);
            } else {
                currentStep++;
                loadNewWord();

                currentQuestionStartTime = System.currentTimeMillis();
            }
        }
    }

    private void loadNewWord() {
        int i = random.nextInt(availableIndices.size());
        int currentWordIndex = availableIndices.get(i);
        availableIndices.remove(i);

        currentWord = allWords.get(currentWordIndex);
        quizLang.setImageResource(LanguageHelper.langCodeToImage(currentWord.getLanguage()));
        quizTranslation.setSingleLine();
        quizTranslation.setSingleLine(false);
        quizTranslation.setText(currentWord.getMeaning());

        totalSteps = Math.min(App.getNumQuizQuestions(), availableIndices.size() + currentStep);

        String labelText = String.format(getResources().getString(R.string.quiz_word_number),
                currentStep,
                totalSteps);
        quizWordNumber.setText(labelText);

        quizNext.setVisibility(View.GONE);
        quizSkip.setVisibility(View.VISIBLE);
        quizHint.setText("");
        quizHint.setTextColor(Color.BLACK);

        quizShuffledWord.setWord(currentWord.getName());
        getSupportFragmentManager().beginTransaction().show(quizShuffledWord).commit();
    }
}
