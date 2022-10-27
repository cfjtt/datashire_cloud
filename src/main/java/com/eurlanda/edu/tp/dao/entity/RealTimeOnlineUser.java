package com.eurlanda.edu.tp.dao.entity;

/**
 * Created by test on 2018/4/24.
 */
public class RealTimeOnlineUser {

    private Integer onlineCount;

    private Long onlineTimeStamp;


    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Long getOnlineTimeStamp() {
        return onlineTimeStamp;
    }

    public void setOnlineTimeStamp(Long onlineTimeStamp) {
        this.onlineTimeStamp = onlineTimeStamp;
    }
}
