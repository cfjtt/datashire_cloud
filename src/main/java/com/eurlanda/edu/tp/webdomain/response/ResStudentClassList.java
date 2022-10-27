package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResStudentClassList {
    private List<ResClassDetail> studentClassList;

    public List<ResClassDetail> getStudentClassList() {
        return studentClassList;
    }

    public void setStudentClassList(List<ResClassDetail> studentClassList) {
        this.studentClassList = studentClassList;
    }
}