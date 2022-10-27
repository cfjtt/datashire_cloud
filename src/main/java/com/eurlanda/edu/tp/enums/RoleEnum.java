package com.eurlanda.edu.tp.enums;

public enum RoleEnum {
    STUDENT(1, "student", "学生"),
    TEACHER(2, "teacher", "教师"),
    MANAGER(3, "manager", "教务员");

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

    RoleEnum(int index, String en, String zh) {
        this.code = index;
        this.en = en;
        this.zh = zh;
    }

    public static String getEnFromCode(int code) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getCode() == code) {
                return roleEnum.getEn();
            }
        }
        return "null";
    }

    public static String getZhFromCode(int code) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getCode() == code) {
                return roleEnum.getZh();
            }
        }
        return "null";
    }
}
