package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class TProjectDocAnnex {
    private Integer id;
    private Integer docId;
    private String fileName;
    private Date createTime;

    public TProjectDocAnnex(Integer id, Integer docId, String fileName, Date createTime) {
        this.id = id;
        this.docId = docId;
        this.fileName = fileName;
        this.createTime = createTime;
    }

    public TProjectDocAnnex() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
