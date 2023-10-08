package com.example.test_my_application.View;
import android.content.Context;
import android.content.res.Resources;

public interface SpeechRecognitionView {
    void showWordToRecognize(String word);
    void showSuccessMessage();
    void showRetryMessage();
    void showAllWordsCompletedMessage();
    Resources getResources();
    Context getContext();

}

