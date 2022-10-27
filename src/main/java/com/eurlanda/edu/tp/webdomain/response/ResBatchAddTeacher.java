package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResBatchAddTeacher {

    private int success;
    private int failure;
    private List<FailureReason> failureReasonList;

    public class FailureReason {
        private String teacherNum;
        private String teacherName;
        private String reason;

        public String getTeacherNum() {
            return teacherNum;
        }

        public void setTeacherNum(String teacherNum) {
            this.teacherNum = teacherNum;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
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

    public List<FailureReason> getFailureReasonList() {
        return failureReasonList;
    }

    public void setFailureReasonList(List<FailureReason> failureReasonList) {
        this.failureReasonList = failureReasonList;
    }
}
