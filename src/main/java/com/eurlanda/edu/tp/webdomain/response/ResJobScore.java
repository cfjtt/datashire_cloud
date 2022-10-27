package com.eurlanda.edu.tp.webdomain.response;

import java.util.Date;

public class ResJobScore /*implements Comparable<ResJobScore>*/{
    private Integer id;
    private Integer classId;
    private Integer courseId;
    private Integer studentId;
    private Integer experimentId;
    private Integer score;
    private Date createTime;
    private String studentName;
    private Integer gender;
    private String sno;
    private String submitDate;

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public ResJobScore(Integer studentId, String studentName, Integer gender, String sno, Integer score, Date createTime) {
        this.studentId=studentId;
        this.studentName = studentName;
        this.gender = gender;
        this.sno = sno;
        this.score = score;
        this.createTime = createTime;
    }

    public ResJobScore(Integer studentId, String studentName, Integer gender, String sno, Integer score, Date createTime,String submitDate) {
        this.studentId=studentId;
        this.studentName = studentName;
        this.gender = gender;
        this.sno = sno;
        this.score = score;
        this.createTime = createTime;
        this.submitDate=submitDate;
    }

    public ResJobScore() {
    }

    public ResJobScore(Integer id, Integer classId, Integer courseId, Integer studentId, Integer experimentId, Integer score, Date createTime, String studentName, Integer gender, String sno) {
        this.id = id;
        this.classId = classId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.experimentId = experimentId;
        this.score = score;
        this.createTime = createTime;
        this.studentName = studentName;
        this.gender = gender;
        this.sno = sno;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


   /* @Override
    public int compareTo(ResJobScore o) {
        int i = Integer.parseInt(this.getSno()) - Integer.parseInt(o.getSno());//先按照学号排序
        if(i == 0){
            return this.score - o.getScore();//如果年龄相等了再用分数进行排序
        }
        return i;
    }*/
}
