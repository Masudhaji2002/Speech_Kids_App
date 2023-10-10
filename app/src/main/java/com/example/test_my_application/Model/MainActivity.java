package com.example.test_my_application.Model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test_my_application.Presenter.SpeechRecognitionPresenter;
import com.example.test_my_application.R;
import com.example.test_my_application.View.SpeechRecognitionView;


public class MainActivity extends AppCompatActivity implements SpeechRecognitionView {
    private TextView wordTextView;
    private Button checkButton;
    private SpeechRecognitionPresenter presenter;
    private static final int YOUR_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, YOUR_PERMISSION_REQUEST_CODE);
        }

        wordTextView = findViewById(R.id.wordTextView);
        checkButton = findViewById(R.id.checkButton);

        presenter = new SpeechRecognitionPresenter(this, this);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCheckButtonClicked();
            }
        });
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showWordToRecognize(String word) {
        wordTextView.setText(word);
    }

    @Override
    public void showSuccessMessage() {
        Toast.makeText(this, "Верно, молодец!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRetryMessage() {
        Toast.makeText(this, "Неверно, повторите еще раз.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAllWordsCompletedMessage() {
        Toast.makeText(this, "Все слова пройдены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}

