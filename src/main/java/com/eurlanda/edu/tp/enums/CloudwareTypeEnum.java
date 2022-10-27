package com.eurlanda.edu.tp.enums;

public enum CloudwareTypeEnum {
    RSTUDIO(1, "RStudio", "编程类－RStudio"),
    PYTHON(2, "Python", "编程类－Python"),
    BASE(3, "Base", "编程类－Base"),
    HADOOP(4, "Hadoop", "编程类－Hadoop"),
    JUPYTER_PYTHON(5, "jupyter_python", "编程类－JupyterPython"),
    IDE_JAVA(6, "IdeJava", "编程类－IdeJava"),
    SHU_LIE_YUN(7,"shu_lie_yun","图形化－数猎云");
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

    CloudwareTypeEnum(int index, String en, String zh) {
        this.code = index;
        this.en = en;
        this.zh = zh;
    }

    public static String getEnFromCode(int code) {
        for (CloudwareTypeEnum cloudwareTypeEnum : CloudwareTypeEnum.values()) {
            if (cloudwareTypeEnum.getCode() == code) {
                return cloudwareTypeEnum.getEn();
            }
        }
        return "null";
    }

    public static String getZhFromCode(int code) {
        for (CloudwareTypeEnum cloudwareTypeEnum : CloudwareTypeEnum.values()) {
            if (cloudwareTypeEnum.getCode() == code) {
                return cloudwareTypeEnum.getZh();
            }
        }
        return "null";
    }

    @Override
    public String toString(){
        return super.toString().toLowerCase();
    }

    public static CloudwareTypeEnum fromInt(int i){
        if(i > CloudwareTypeEnum.values().length || i <= 0){
            return null;
        }

        return CloudwareTypeEnum.values()[i-1];
    }

    public static boolean isExistTypeEnum(String name){
        for (CloudwareTypeEnum cloudwareTypeEnum : CloudwareTypeEnum.values()) {
            if (cloudwareTypeEnum.getEn().toUpperCase().equals(name.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public static CloudwareTypeEnum parse(String name){
        CloudwareTypeEnum typeEnum = null;
        for(CloudwareTypeEnum cloudwareTypeEnum : CloudwareTypeEnum.values()){
            if(cloudwareTypeEnum.getEn().toUpperCase().equals(name.toUpperCase())){
                typeEnum = cloudwareTypeEnum;
            }
        }
        return typeEnum;
    }

}
