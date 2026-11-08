package com.example.uiproject;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonalDataDetector {

    public interface Callback {
        void onSuccess(List<MaskRegion> regions);
        void onFailure(Exception e);
    }

    // 전화번호 및 주민번호 패턴 정규식
    private static final Pattern PHONE_PATTERN = Pattern.compile("01[016789]-?\\d{3,4}-?\\d{4}");
    private static final Pattern RRN_PATTERN = Pattern.compile("\\d{6}-[1-4]\\d{6}");

    public static void detect(Bitmap bitmap, Callback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    List<MaskRegion> regions = new ArrayList<>();
                    int idCounter = 0;

                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                        for (Text.Line line : block.getLines()) {
                            String text = line.getText();
                            Rect box = line.getBoundingBox();
                            if (box == null) continue;

                            RectF rectF = new RectF(box);
                            MaskType type = classifyText(text);

                            regions.add(new MaskRegion("region_" + (idCounter++), rectF, type, true));
                        }
                    }
                    callback.onSuccess(regions);
                })
                .addOnFailureListener(callback::onFailure);
    }

    private static MaskType classifyText(String text) {
        if (RRN_PATTERN.matcher(text).find()) {
            return MaskType.RRN;
        } else if (PHONE_PATTERN.matcher(text).find()) {
            return MaskType.PHONE;
        } else if (text.contains("성명") || text.contains("이름")) {
            return MaskType.NAME;
        }
        return MaskType.OTHER;
    }
}