package com.example.uiproject;

public enum MaskType {
    NAME("🔒 이름 가려짐"),
    RRN("🔒 주민번호 가려짐"),
    PHONE("🔒 전화번호 가려짐"),
    OTHER("🔒 기타 영역 가려짐");

    private final String label;

    MaskType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}