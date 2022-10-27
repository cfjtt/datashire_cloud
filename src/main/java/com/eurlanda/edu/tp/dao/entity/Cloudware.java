package com.eurlanda.edu.tp.dao.entity;

public class Cloudware {
    private Integer id;

    private String webSocket;

    private String serviceId;

    private String instanceId;

    private String serviceName;

    private String pulsarId;

    public Cloudware(Integer id, String webSocket, String serviceId, String instanceId, String serviceName, String pulsarId) {
        this.id = id;
        this.webSocket = webSocket;
        this.serviceId = serviceId;
        this.instanceId = instanceId;
        this.serviceName = serviceName;
        this.pulsarId = pulsarId;
    }

    public Cloudware(String webSocket, String serviceId, String instanceId, String serviceName, String pulsarId) {
        this(0, webSocket, serviceId, instanceId, serviceName, pulsarId);
    }

    public Cloudware() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(String webSocket) {
        this.webSocket = webSocket == null ? null : webSocket.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public String getPulsarId() {
        return pulsarId;
    }

    public void setPulsarId(String pulsarId) {
        this.pulsarId = pulsarId == null ? null : pulsarId.trim();
    }
}