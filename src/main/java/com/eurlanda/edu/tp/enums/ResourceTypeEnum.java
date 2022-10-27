package com.eurlanda.edu.tp.enums;

public enum  ResourceTypeEnum {
    IMAGE(1),

    HOMEWORK(2),

    MARKDOWN(3);

    private int code;

    public int getCode() {
        return code;
    }

    ResourceTypeEnum(int index) {
        this.code = index;
    }
}
