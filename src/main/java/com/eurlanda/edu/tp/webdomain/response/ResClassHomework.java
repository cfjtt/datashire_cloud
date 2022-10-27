package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */
public class ResClassHomework {
    private int classId;
    private String className;
    private String courseName;
    private List<ResClassHomeworkModule> modules;


    public class ResClassHomeworkModule {
        private int moduleId;
        private String moduleName;
        private List<ResClassHomeworkModuleHomework> homeworks;

        public int getModuleId() {
            return moduleId;
        }

        public void setModuleId(int moduleId) {
            this.moduleId = moduleId;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public List<ResClassHomeworkModuleHomework> getHomeworks() {
            return homeworks;
        }

        public void setHomeworks(List<ResClassHomeworkModuleHomework> homeworks) {
            this.homeworks = homeworks;
        }
    }

    public class ResClassHomeworkModuleHomework {
        private int homeworkId;
        private String homeworkName;
        private String homeworkDes;
        private String homeworkCreateDate;
        private String homeworkDueDate;
        private String teacherName;
        private String cloudwareType;

        public int getHomeworkId() {
            return homeworkId;
        }

        public void setHomeworkId(int homeworkId) {
            this.homeworkId = homeworkId;
        }

        public String getHomeworkName() {
            return homeworkName;
        }

        public void setHomeworkName(String homeworkName) {
            this.homeworkName = homeworkName;
        }

        public String getHomeworkDes() {
            return homeworkDes;
        }

        public void setHomeworkDes(String homeworkDes) {
            this.homeworkDes = homeworkDes;
        }

        public String getHomeworkCreateDate() {
            return homeworkCreateDate;
        }

        public void setHomeworkCreateDate(String homeworkCreateDate) {
            this.homeworkCreateDate = homeworkCreateDate;
        }

        public String getHomeworkDueDate() {
            return homeworkDueDate;
        }

        public void setHomeworkDueDate(String homeworkDueDate) {
            this.homeworkDueDate = homeworkDueDate;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getCloudwareType() {
            return cloudwareType;
        }

        public void setCloudwareType(String cloudwareType) {
            this.cloudwareType = cloudwareType;
        }
    }


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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<ResClassHomeworkModule> getModules() {
        return modules;
    }

    public void setModules(List<ResClassHomeworkModule> modules) {
        this.modules = modules;
    }
}
