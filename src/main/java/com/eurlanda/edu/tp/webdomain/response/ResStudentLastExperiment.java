package com.eurlanda.edu.tp.webdomain.response;

public class ResStudentLastExperiment {
    private int lastExperimentId;
    private String lastExperimentName;
    private String lastModuleName;

    public ResStudentLastExperiment(int lastExperimentId, String lastExperimentName, String lastModuleName) {
        this.lastExperimentId = lastExperimentId;
        this.lastExperimentName = lastExperimentName;
        this.lastModuleName = lastModuleName;
    }

    public ResStudentLastExperiment() {
        this.lastExperimentId = 0;
    }

    public int getLastExperimentId() {
        return lastExperimentId;
    }

    public void setLastExperimentId(int lastExperimentId) {
        this.lastExperimentId = lastExperimentId;
    }

    public String getLastExperimentName() {
        return lastExperimentName;
    }

    public void setLastExperimentName(String lastExperimentName) {
        this.lastExperimentName = lastExperimentName;
    }

    public String getLastModuleName() {
        return lastModuleName;
    }

    public void setLastModuleName(String lastModuleName) {
        this.lastModuleName = lastModuleName;
    }
}
