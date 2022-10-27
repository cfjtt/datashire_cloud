package com.eurlanda.edu.tp.dao.entity;

import java.util.Date;

public class TProjectDoc {
    private Integer id;
    private Integer folderId;
    private String content;
    private Integer creator;
    private Integer editor;
    private Date createTime;
    private Date updateTime;
    private String docName;

    public TProjectDoc(Integer id, Integer folderId, String content, Integer creator, Integer editor, Date createTime, Date updateTime,String docName) {
        this.id = id;
        this.folderId = folderId;
        this.content = content;
        this.creator = creator;
        this.editor = editor;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.docName = docName;
    }
    public TProjectDoc(Integer id, Integer folderId, String content, Date createTime, Date updateTime,Integer creator, Integer editor, String docName) {
        this.id = id;
        this.folderId = folderId;
        this.content = content;
        this.creator = creator;
        this.editor = editor;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.docName = docName;
    }



    public TProjectDoc() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getEditor() {
        return editor;
    }

    public void setEditor(Integer editor) {
        this.editor = editor;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
