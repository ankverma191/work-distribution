package com.freedom.financial.work.distribution.entity;

public enum Priority {
    LOW(1), HIGH(2);

    private int value = 0;
    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
