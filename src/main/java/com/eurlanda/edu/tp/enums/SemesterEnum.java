package com.eurlanda.edu.tp.enums;

public enum SemesterEnum {
    //todo: improve globalization
    NO1(1, "the first term", "第一学期"),
    NO2(2, "the second term", "第二学期"),
    NO3(3, "the third term", "第三学期");

    private int code;
    private String en;
    private String zh;

    public int getCode() {
        return code;
    }

    public String getEn() {
        return en;
    }

    public String getZh() {
        return zh;
    }

    SemesterEnum(int index, String en, String zh) {
        this.code = index;
        this.en = en;
        this.zh = zh;
    }

    public static String getEnFromCode(int code) {
        for (SemesterEnum semesterEnum : SemesterEnum.values()) {
            if (semesterEnum.getCode() == code) {
                return semesterEnum.getEn();
            }
        }
        return "null";
    }

    public static String getZhFromCode(int code) {
        for (SemesterEnum semesterEnum : SemesterEnum.values()) {
            if (semesterEnum.getCode() == code) {
                return semesterEnum.getZh();
            }
        }
        return "null";
    }

    public static SemesterEnum fromInt(int i){
        if(i > SemesterEnum.values().length || i <= 0){
            return null;
        }

        return SemesterEnum.values()[i-1];
    }
}
