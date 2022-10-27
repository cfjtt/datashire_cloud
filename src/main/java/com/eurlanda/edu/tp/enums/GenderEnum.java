package com.eurlanda.edu.tp.enums;

public enum  GenderEnum {
    MALE(1, "male", "男"),
    FEMALE(2, "female", "女");

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

    GenderEnum(int index, String en, String zh) {
        this.code = index;
        this.en = en;
        this.zh = zh;
    }

    public static String getEnFromCode(int code) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getCode() == code) {
                return genderEnum.getEn();
            }
        }
        return "null";
    }

    public static String getZhFromCode(int code) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getCode() == code) {
                return genderEnum.getZh();
            }
        }
        return "null";
    }

    public static GenderEnum parse(String name){
        GenderEnum gender = null;
        for(GenderEnum genderEnum : GenderEnum.values()){
            if(genderEnum.getZh().equals(name)){
                gender = genderEnum;
            }
        }
        return gender;
    }

}
