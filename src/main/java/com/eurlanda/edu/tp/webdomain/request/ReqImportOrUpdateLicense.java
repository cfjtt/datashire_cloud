package com.eurlanda.edu.tp.webdomain.request;

/**
 * Created by test on 2018/6/6.
 */
public class ReqImportOrUpdateLicense {

    private Integer type;//操作类型 1.导入 2.更新

    private String license;//license

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
