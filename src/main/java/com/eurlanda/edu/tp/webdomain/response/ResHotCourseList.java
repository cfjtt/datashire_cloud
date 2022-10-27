package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResHotCourseList {

    private List<ResHotCourseDetail> courseList;

    public List<ResHotCourseDetail> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<ResHotCourseDetail> courseList) {
        this.courseList = courseList;
    }

    public static class ResHotCourseDetail {
        private Integer courseId;
        private Integer actualStudentNum;
        private Integer studentNum;
        private String courseName;
        private String courseDes;
        private String imageUrl;
        private String teacherName;
        private String teacherContact;

        public ResHotCourseDetail(Integer courseId, Integer actualStudentNum, Integer studentNum, String courseName, String courseDes, String imageUrl, String teacherName, String teacherContact) {
            this.courseId = courseId;
            this.actualStudentNum = actualStudentNum;
            this.studentNum = studentNum;
            this.courseName = courseName;
            this.courseDes = courseDes;
            this.imageUrl = imageUrl;
            this.teacherName = teacherName;
            this.teacherContact = teacherContact;
        }

        protected ResHotCourseDetail(){

        }

        public Integer getCourseId() {
            return courseId;
        }

        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }

        public Integer getActualStudentNum() {
            return actualStudentNum;
        }

        public void setActualStudentNum(Integer actualStudentNum) {
            this.actualStudentNum = actualStudentNum;
        }

        public Integer getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(Integer studentNum) {
            this.studentNum = studentNum;
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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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
    }
}
