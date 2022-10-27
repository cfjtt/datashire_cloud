package com.eurlanda.edu.tp.webdomain.response;

public class ResStudentHomeworkDetail {
    private String courseName;
    private String moduleName;
    private String homeworkName;
    private int studentId;
    private String studentName;
    private String cloudwareUrl;
    private String homeworkUrl;
    private int score;
    private String comment;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCloudwareUrl() {
        return cloudwareUrl;
    }

    public void setCloudwareUrl(String cloudwareUrl) {
        this.cloudwareUrl = cloudwareUrl;
    }

    public String getHomeworkUrl() {
        return homeworkUrl;
    }

    public void setHomeworkUrl(String homeworkUrl) {
        this.homeworkUrl = homeworkUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
