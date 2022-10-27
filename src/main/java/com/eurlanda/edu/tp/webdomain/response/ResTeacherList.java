package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResTeacherList {
    int count;
    private List<ResTeacherInfo> teacherInfoList;

    public List<ResTeacherInfo> getTeacherInfoList() {
        return teacherInfoList;
    }

    public void setTeacherInfoList(List<ResTeacherInfo> teacherInfoList) {
        this.teacherInfoList = teacherInfoList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
