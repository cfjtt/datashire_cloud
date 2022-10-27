package com.eurlanda.edu.tp.webdomain.request;

public class ReqStudentHomeworkCloudware {
    private int studentId;
    private int homeworkId;
    private String webSocket;
    private String serviceId;
    private String instanceId;
    private String serviceName;
    private String pulsarId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(String webSocket) {
        this.webSocket = webSocket;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPulsarId() {
        return pulsarId;
    }

    public void setPulsarId(String pulsarId) {
        this.pulsarId = pulsarId;
    }
}
