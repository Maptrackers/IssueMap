package com.maptracker.issuemap.domain.issue.entity;


public enum IssueType {
    EPIC,
    STORY,
    TASK,
    BUG,
    CUSTOM; // 사용자 정의 타입

    private String customValue;

    // 사용자 정의 값을 설정하는 메서드
    public void setCustomValue(String customValue) {
        if (this == CUSTOM) {
            this.customValue = customValue;
        } else {
            throw new UnsupportedOperationException("Only CUSTOM type can have custom values.");
        }
    }

    // 사용자 정의 값을 가져오는 메서드
    public String getCustomValue() {
        if (this == CUSTOM) {
            return customValue;
        }
        return name(); // 기본 타입의 이름 반환
    }
}