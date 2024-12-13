package com.englishstudy.englishstudy.model;

public enum FileType {
    VIDEO(1),
    AUDIO(2),
    IMAGE(3),
    DOCUMENT(4);

    private final int value;

    FileType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FileType fromValue(int value) {
        for (FileType type : FileType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}