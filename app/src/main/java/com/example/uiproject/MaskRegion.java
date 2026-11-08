package com.example.uiproject;

import android.graphics.RectF;

public class MaskRegion {
    private String id;
    private RectF rect;
    private MaskType type;
    private boolean isConfirmed;

    public MaskRegion(String id, RectF rect, MaskType type, boolean isConfirmed) {
        this.id = id;
        this.rect = rect;
        this.type = type;
        this.isConfirmed = isConfirmed;
    }

    public String getId() { return id; }
    public RectF getRect() { return rect; }
    public MaskType getType() { return type; }
    public boolean isConfirmed() { return isConfirmed; }
    public void setConfirmed(boolean confirmed) { isConfirmed = confirmed; }
}
