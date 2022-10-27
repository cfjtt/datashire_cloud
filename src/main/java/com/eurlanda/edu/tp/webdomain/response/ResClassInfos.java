package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResClassInfos {

    private List<ResClassInfo> classInfoList;

    public static class ResClassInfo {
        private int classId;
        private String className;
        private int courseId;
        private String courseName;
        private String courseDes;
        private int teacherId;
        private String teacherName;
        private String teacherContact;
        private String term;
        private String courseImage;
        private String duration;
        private Integer studentNum;
        private String courseDate;
        private Integer termId;
        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseDes() {
            return courseDes;
        }

        public void setCourseDes(String courseDes) {
            this.courseDes = courseDes;
        }

        public int getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(int teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getTeacherContact() {
            return teacherContact;
        }

        public void setTeacherContact(String teacherContact) {
            this.teacherContact = teacherContact;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getCourseImage() {
            return courseImage;
        }

        public void setCourseImage(String courseImage) {
            this.courseImage = courseImage;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public Integer getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(Integer studentNum) {
            this.studentNum = studentNum;
        }

        public String getCourseDate() {
            return courseDate;
        }

        public void setCourseDate(String courseDate) {
            this.courseDate = courseDate;
        }

        public Integer getTermId() {
            return termId;
        }

        public void setTermId(Integer termId) {
            this.termId = termId;
        }
    }

    public List<ResClassInfo> getClassInfoList() {
        return classInfoList;
    }

    public void setClassInfoList(List<ResClassInfo> classInfoList) {
        this.classInfoList = classInfoList;
    }
}
