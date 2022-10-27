package com.eurlanda.edu.tp.webdomain.response;

import com.eurlanda.edu.tp.dao.entity.Resource;

import java.util.List;

public class ResCourseModuleExperiments {
    private  int courseId;

    private String courseName;

    private Integer teacherId;


    private String description;

    //该课程的文件名--创建该课程时的时间戳
    private String folderName;


    private String volumeId;

    private String author;
    private String phone;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private boolean isHavingDatashireExperiment;


    public boolean isHavingDatashireExperiment() {
        return isHavingDatashireExperiment;
    }

    public void setHavingDatashireExperiment(boolean havingDatashireExperiment) {
        isHavingDatashireExperiment = havingDatashireExperiment;
    }

    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    private List<ModuleInfo> moduleList;

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

    public List<ModuleInfo> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<ModuleInfo> moduleList) {
        this.moduleList = moduleList;
    }

    public static class ModuleInfo {

        private int moduleId;

        private String moduleName;
        private Integer orderId;

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        private List<ResExperimentInfo> moduleContent;

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

        public List<ResExperimentInfo> getModuleContent() {
            return moduleContent;
        }

        public void setModuleContent(List<ResExperimentInfo> moduleContent) {
            this.moduleContent = moduleContent;
        }
    }

}
