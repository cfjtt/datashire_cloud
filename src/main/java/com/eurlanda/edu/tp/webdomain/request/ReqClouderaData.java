package com.eurlanda.edu.tp.webdomain.request;

import java.util.Date;

/**
 * Created by test on 2018/4/23.
 */
public class ReqClouderaData {
    private Long from;
    private Long to;
    private String query;


    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
