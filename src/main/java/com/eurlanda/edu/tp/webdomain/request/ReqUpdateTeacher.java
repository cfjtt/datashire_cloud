package com.eurlanda.edu.tp.webdomain.request;

public class ReqUpdateTeacher {
    private int id;
    private String teacherNo;
    private String teacherName;
    private int teacherTitleId;
    private int gender;
    private String teacherContact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTeacherTitleId() {
        return teacherTitleId;
    }

    public void setTeacherTitleId(int teacherTitleId) {
        this.teacherTitleId = teacherTitleId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getTeacherContact() {
        return teacherContact;
    }

    public void setTeacherContact(String teacherContact) {
        this.teacherContact = teacherContact;
    }
}
