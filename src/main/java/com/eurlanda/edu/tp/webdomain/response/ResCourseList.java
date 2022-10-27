package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResCourseList {

    private List<CourseInfo> courseInfoList;

    public List<CourseInfo> getCourseInfoList() {
        return courseInfoList;
    }

    public void setCourseInfoList(List<CourseInfo> courseInfoList) {
        this.courseInfoList = courseInfoList;
    }

    public static class CourseInfo{
        private int id;
        private String courseName;
        private String courseDes;
        private String teacherName;
        private String teacherContact;
        private String author;
        private String phone;
        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
