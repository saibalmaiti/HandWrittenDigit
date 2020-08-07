package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.fingerpaintview.FingerPaintView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private FingerPaintView mFpvPaint;
    private TextView prediction;
    private Button detect, clear;

    private Classifier mClassifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mFpvPaint = findViewById(R.id.fpv_paint);
        prediction = findViewById(R.id.prediction);
        detect = findViewById(R.id.detect);
        clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFpvPaint.clear();
                prediction.setText(R.string.empty);
            }
        });
         detect.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mClassifier == null) {
                     Log.e("LOG_TAG1", "onDetectClick(): Classifier is not initialized");
                     return;
                 } else if (mFpvPaint.isEmpty()) {
                     Toast.makeText(MainActivity.this, "Please write a digit in the box above.", Toast.LENGTH_SHORT).show();
                     return;
                 }

                 Bitmap image = mFpvPaint.exportToBitmap(
                         Classifier.IMG_WIDTH, Classifier.IMG_HEIGHT);
                 Result result = mClassifier.classify(image);
                 renderResult(result);
             }
         });


    }
    private void init() {
        try {
            mClassifier = new Classifier(this);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Failed to initialize a classifier", Toast.LENGTH_LONG).show();
            Log.e("LOG_TAG2", "init(): Failed to create Classifier", e);
        }
    }

    private void renderResult(@NotNull Result result) {
        prediction.setText(String.valueOf(result.getNumber()));
        //mTvProbability.setText(String.valueOf(result.getProbability()));

    }
}
