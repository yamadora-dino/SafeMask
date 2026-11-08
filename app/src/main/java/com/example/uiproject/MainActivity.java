package com.example.uiproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CustomCanvasView customCanvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCanvasView = findViewById(R.id.customCanvasView);

        Bitmap sampleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_image);

        PersonalDataDetector.detect(sampleBitmap, new PersonalDataDetector.Callback() {
            @Override
            public void onSuccess(List<MaskRegion> regions) {
                customCanvasView.setData(sampleBitmap, regions);

                Uri savedUri = ImageSaver.saveMaskedImage(MainActivity.this, sampleBitmap, regions);
                if (savedUri != null) {
                    Toast.makeText(MainActivity.this, "갤러리 저장 성공!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "탐지 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}