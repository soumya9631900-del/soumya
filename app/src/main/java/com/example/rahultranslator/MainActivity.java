package com.example.rahultranslator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText inputText;
    TextView resultText;
    Spinner sourceLanguage;
    Spinner targetLanguage;
    TextToSpeech textToSpeech;

    String[] languages = {
            "English",
            "Hindi",
            "French",
            "Spanish",
            "German",
            "Japanese",
            "Chinese"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        resultText = findViewById(R.id.resultText);
        sourceLanguage = findViewById(R.id.sourceLanguage);
        targetLanguage = findViewById(R.id.targetLanguage);

        Button translateButton = findViewById(R.id.translateButton);
        Button copyButton = findViewById(R.id.copyButton);
        Button speakButton = findViewById(R.id.speakButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                languages
        );

        sourceLanguage.setAdapter(adapter);
        targetLanguage.setAdapter(adapter);

        targetLanguage.setSelection(1);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });

        translateButton.setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();

            if (text.isEmpty()) {
                Toast.makeText(
                        this,
                        "Please enter some text",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            resultText.setText(
                    "Translation result:\n\n" + text
            );
        });

        copyButton.setOnClickListener(v -> {
            String result = resultText.getText().toString();

            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(
                            Context.CLIPBOARD_SERVICE
                    );

            ClipData clip = ClipData.newPlainText(
                    "Translation",
                    result
            );

            clipboard.setPrimaryClip(clip);

            Toast.makeText(
                    this,
                    "Copied!",
                    Toast.LENGTH_SHORT
            ).show();
        });

        speakButton.setOnClickListener(v -> {
            String text = resultText.getText().toString();

            if (!text.isEmpty()) {
                textToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}
