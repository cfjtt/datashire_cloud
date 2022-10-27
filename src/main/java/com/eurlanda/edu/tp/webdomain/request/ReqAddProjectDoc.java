package com.eurlanda.edu.tp.webdomain.request;

import java.util.Date;

public class ReqAddProjectDoc {
    private Integer id;
    private Integer folderId;
    private String content;
    private Integer creator;
    private Integer editor;
    private Date createTime;
    private Date updateTime;
    private String docName;
    private Integer projectId;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public ReqAddProjectDoc(Integer id, Integer folderId, String content, Integer creator, Integer editor, Date createTime, Date updateTime, String docName) {
        this.id = id;
        this.folderId = folderId;
        this.content = content;
        this.creator = creator;
        this.editor = editor;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.docName = docName;
    }
    public ReqAddProjectDoc(Integer id, String content,Integer editor,String docName,Integer projectId,Date updateTime) {
        this.id = id;
        this.content = content;
        this.editor = editor;
        this.projectId=projectId;
        this.docName = docName;
        this.updateTime = updateTime;
    }
    public ReqAddProjectDoc() {
    }
}
