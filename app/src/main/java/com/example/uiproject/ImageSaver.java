package com.example.uiproject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.OutputStream;
import java.util.List;

public class ImageSaver {

    public static Uri saveMaskedImage(Context context, Bitmap original, List<MaskRegion> regions) {
        Bitmap output = original.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(output);

        Paint maskPaint = new Paint();
        maskPaint.setColor(Color.BLACK);
        maskPaint.setStyle(Paint.Style.FILL);

        // 검은색 가림 박스 합성
        for (MaskRegion region : regions) {
            if (!region.isConfirmed()) continue;

            RectF rect = region.getRect();
            canvas.drawRect(rect, maskPaint);
        }

        // 갤러리 저장
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "SafeMask_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/SafeMask");

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri != null) {
            try (OutputStream out = resolver.openOutputStream(uri)) {
                output.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uri;
    }
}
