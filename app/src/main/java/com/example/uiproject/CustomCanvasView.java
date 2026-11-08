package com.example.uiproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomCanvasView extends View {

    private Bitmap sourceBitmap;
    private List<MaskRegion> maskRegions = new ArrayList<>();
    private Paint maskPaint;

    public CustomCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    private void initPaints() {
        maskPaint = new Paint();
        maskPaint.setColor(Color.BLACK);
        maskPaint.setStyle(Paint.Style.FILL);
    }

    public void setData(Bitmap bitmap, List<MaskRegion> regions) {
        this.sourceBitmap = bitmap;
        this.maskRegions = regions;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sourceBitmap == null || getWidth() == 0 || getHeight() == 0) return;

        // 화면 크기와 이미지 원본 크기 간의 스케일 비율 연산
        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float bitmapWidth = sourceBitmap.getWidth();
        float bitmapHeight = sourceBitmap.getHeight();

        float scale = Math.min(viewWidth / bitmapWidth, viewHeight / bitmapHeight);
        float dx = (viewWidth - bitmapWidth * scale) / 2f;
        float dy = (viewHeight - bitmapHeight * scale) / 2f;

        // 비율에 맞게 축소된 원본 이미지 그리기
        Rect srcRect = new Rect(0, 0, (int) bitmapWidth, (int) bitmapHeight);
        RectF dstRect = new RectF(dx, dy, dx + bitmapWidth * scale, dy + bitmapHeight * scale);
        canvas.drawBitmap(sourceBitmap, srcRect, dstRect, null);

        // 탐지 영역 위치에 순수 검은색 박스 렌더링
        for (MaskRegion region : maskRegions) {
            if (!region.isConfirmed()) continue;

            RectF origRect = region.getRect();
            RectF scaledRect = new RectF(
                    dx + origRect.left * scale,
                    dy + origRect.top * scale,
                    dx + origRect.right * scale,
                    dy + origRect.bottom * scale
            );

            canvas.drawRect(scaledRect, maskPaint);
        }
    }
}