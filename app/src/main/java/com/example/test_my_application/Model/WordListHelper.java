package com.example.test_my_application.Model;

import android.content.res.Resources;
import android.util.Log;

import com.example.test_my_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// чтение json файла
public class WordListHelper {
    public static List<String> getWordList(Resources resources) {
        List<String> wordList = new ArrayList<>();
        InputStream inputStream = resources.openRawResource(R.raw.words);

        try {
            Scanner scanner = new Scanner(inputStream);
            StringBuilder stringBuilder = new StringBuilder();

            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray wordsArray = jsonObject.getJSONArray("words");

            for (int i = 0; i < wordsArray.length(); i++) {
                wordList.add(wordsArray.getString(i));
            }

        } catch (JSONException e) {
            Log.e("WordListHelper", "JSON parsing error: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return wordList;
    }
}

