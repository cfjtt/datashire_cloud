package com.eurlanda.edu.tp.webdomain.request;

import com.eurlanda.edu.tp.dao.entity.Cloudware;

public class ReqDeleteCloudware {
    private String serviceName;
    private String serviceId;
    private String pulsarId;
    private String secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmb28iOiJiYXIiLCJpYXQiOjE1MDU4MTM0NTd9.Ftw1yHeUrqdNvymFZcIpuEoS0RHBFZqu4MfUZON9Zm0";

    public ReqDeleteCloudware(Cloudware cloudware){
        this.serviceId = cloudware.getServiceId();
        this.serviceName = cloudware.getServiceName();
        this.pulsarId = cloudware.getPulsarId();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPulsarId() {
        return pulsarId;
    }

    public void setPulsarId(String pulsarId) {
        this.pulsarId = pulsarId;
    }

    public String getSecret() {
        return secret;
    }
}
