package com.eurlanda.edu.tp.webdomain.response;

public class ResStatistics {
    private int studentNum;
    private int teacherNum;
    private int courseNum;
    private int experimentNum;

    public ResStatistics(int studentNum, int teacherNum, int courseNum, int experimentNum) {
        this.studentNum = studentNum;
        this.teacherNum = teacherNum;
        this.courseNum = courseNum;
        this.experimentNum = experimentNum;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(int teacherNum) {
        this.teacherNum = teacherNum;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public int getExperimentNum() {
        return experimentNum;
    }

    public void setExperimentNum(int experimentNum) {
        this.experimentNum = experimentNum;
    }
}
