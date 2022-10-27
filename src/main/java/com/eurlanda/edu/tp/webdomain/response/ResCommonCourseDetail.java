package com.eurlanda.edu.tp.webdomain.response;

public class ResCommonCourseDetail {
    private ResTeacherInfo teacherInfo;
    private int classNum;
    private int studentNum;
    private String courseDescription;

    public ResCommonCourseDetail(ResTeacherInfo teacherInfo, int classNum, int studentNum, String courseDescription) {
        this.teacherInfo = teacherInfo;
        this.classNum = classNum;
        this.studentNum = studentNum;
        this.courseDescription = courseDescription;
    }

    public ResTeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(ResTeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
