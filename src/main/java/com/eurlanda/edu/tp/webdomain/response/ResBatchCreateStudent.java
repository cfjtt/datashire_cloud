package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResBatchCreateStudent {
    private int success;
    private int failure;
    private List<ResBatchCreateStudent.FailureReason> failureReasonList;
    public class FailureReason {
        private String studentNum;
        private String studentName;
        private String reason;
        private int gender;
        private String grade;
        private String phone;
        private int errorCode;

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
    }
    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public List<ResBatchCreateStudent.FailureReason> getFailureReasonList() {
        return failureReasonList;
    }

    public void setFailureReasonList(List<ResBatchCreateStudent.FailureReason> failureReasonList) {
        this.failureReasonList = failureReasonList;
    }
}
