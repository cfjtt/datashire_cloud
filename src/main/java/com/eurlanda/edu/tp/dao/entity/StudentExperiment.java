package com.eurlanda.edu.tp.dao.entity;

public class StudentExperiment {
    private Integer id;

    private Integer studentId;

    private Integer experimentId;

    private Integer cloudwareId;

    public StudentExperiment(Integer id, Integer studentId, Integer experimentId, Integer cloudwareId) {
        this.id = id;
        this.studentId = studentId;
        this.experimentId = experimentId;
        this.cloudwareId = cloudwareId;
    }

    public StudentExperiment(Integer studentId, Integer experimentId, Integer cloudwareId) {
        this(0, studentId, experimentId, cloudwareId);
    }

    public StudentExperiment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public Integer getCloudwareId() {
        return cloudwareId;
    }

    public void setCloudwareId(Integer cloudwareId) {
        this.cloudwareId = cloudwareId;
    }
}