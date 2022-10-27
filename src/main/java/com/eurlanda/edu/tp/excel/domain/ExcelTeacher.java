package com.eurlanda.edu.tp.excel.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/10/22.
 */
public class ExcelTeacher {

    private List<ExcelTeacherElement> excelTeacherElementList;

    public class ExcelTeacherElement {
        private String teacherNum;
        private String teacherName;
        private int teacherTitle;
        private String teacherTitleStr;
        private int gender;
        private String genderStr;
        private String teacherContact;

        public String getTeacherTitleStr() {
            return teacherTitleStr;
        }

        public void setTeacherTitleStr(String teacherTitleStr) {
            this.teacherTitleStr = teacherTitleStr;
        }

        public String getGenderStr() {
            return genderStr;
        }

        public void setGenderStr(String genderStr) {
            this.genderStr = genderStr;
        }

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

        public int getTeacherTitle() {
            return teacherTitle;
        }

        public void setTeacherTitle(int teacherTitle) {
            this.teacherTitle = teacherTitle;
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

    public List<ExcelTeacherElement> getExcelTeacherElementList() {
        return excelTeacherElementList;
    }

    public void setExcelTeacherElementList(List<ExcelTeacherElement> excelTeacherElementList) {
        this.excelTeacherElementList = excelTeacherElementList;
    }
}
