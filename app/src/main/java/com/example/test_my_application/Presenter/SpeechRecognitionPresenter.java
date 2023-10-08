package com.example.test_my_application.Presenter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.RecognitionListener;

import android.speech.SpeechRecognizer;

import com.example.test_my_application.Model.WordListHelper;

import com.example.test_my_application.View.SpeechRecognitionView;

import java.util.ArrayList;
import java.util.List;


public class SpeechRecognitionPresenter {
    private SpeechRecognitionView view;
    private List<String> wordList;
    private int currentWordIndex = 0;
    private SpeechRecognizer speechRecognizer;
    private Context context;
    private MediaPlayer mediaPlayer;

    public SpeechRecognitionPresenter(SpeechRecognitionView view, Context context) {
        this.view = view;
        this.context = context;
        wordList = WordListHelper.getWordList(view.getResources());
        initSpeechRecognizer();
    }

    public void onCheckButtonClicked() {
        startSpeechRecognition();
    }

    private void initSpeechRecognizer() {
        // Используйте языковой код "ru-RU" для русской локали
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(android.os.Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                view.showRetryMessage();
            }

            @Override
            public void onResults(android.os.Bundle results) {
                ArrayList<String> recognizedWords = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
                if (recognizedWords != null && recognizedWords.size() > 0) {
                    String spokenWord = recognizedWords.get(0);

                    String wordToRecognize = wordList.get(currentWordIndex);

                    if (wordToRecognize.equalsIgnoreCase(spokenWord)) {
                        view.showSuccessMessage();
                        currentWordIndex++;
                        if (currentWordIndex < wordList.size()) {
                            startSpeechRecognition();
                        } else {
                            view.showAllWordsCompletedMessage();
                        }
                    } else {
                        view.showRetryMessage();
                    }
                }
            }

            @Override
            public void onPartialResults(android.os.Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, android.os.Bundle params) {}
        });
    }

    private void startSpeechRecognition() {
        if (currentWordIndex < wordList.size()) {
            String wordToRecognize = wordList.get(currentWordIndex);
            view.showWordToRecognize(wordToRecognize);

            int audioResourceId = view.getContext().getResources().getIdentifier("word" + (currentWordIndex + 1), "raw", view.getContext().getPackageName());
            if (audioResourceId != 0) {
                mediaPlayer = MediaPlayer.create(view.getContext(), audioResourceId);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        speechRecognizer.startListening(createRecognizerIntent());
                    }
                });
                mediaPlayer.start();
            } else {
                view.showRetryMessage();
            }
        }
    }

    private Intent createRecognizerIntent() {
        Intent intent = new Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, "ru-RU"); // Установка локали на русский
        return intent;
    }

    public void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
