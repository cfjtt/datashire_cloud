package com.eurlanda.edu.tp.webdomain.response;

import javax.print.Doc;
import java.util.Date;
import java.util.List;

public class ResProjectDocList {
    public List<DocInfo> docInfoList;

    public List<DocInfo> getDocInfoList() {
        return docInfoList;
    }

    public void setDocInfoList(List<DocInfo> docInfoList) {
        this.docInfoList = docInfoList;
    }

    public static class DocInfo{
        private Integer id;
        private Integer folderId;
        private String content;
        private Integer creator;
        private Integer editor;
        private Date createTime;
        private Date updateTime;
        private String docName;
        private String creatorName;
        private String editorName;

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public String getEditorName() {
            return editorName;
        }

        public void setEditorName(String editorName) {
            this.editorName = editorName;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setFolderId(Integer folderId) {
            this.folderId = folderId;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreator(Integer creator) {
            this.creator = creator;
        }

        public void setEditor(Integer editor) {
            this.editor = editor;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public Integer getId() {
            return id;
        }

        public Integer getFolderId() {
            return folderId;
        }

        public String getContent() {
            return content;
        }

        public Integer getCreator() {
            return creator;
        }

        public Integer getEditor() {
            return editor;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public String getDocName() {
            return docName;
        }

        public DocInfo() {
        }
        public DocInfo(Integer id, Integer folderId, String content, Integer creator,
                       Integer editor, Date createTime, Date updateTime, String docName,String creatorName,String editorName) {
            this.id = id;
            this.folderId = folderId;
            this.content = content;
            this.creator = creator;
            this.editor = editor;
            this.createTime = createTime;
            this.updateTime = updateTime;
            this.docName = docName;
            this.creatorName=creatorName;
            this.editorName=editorName;
        }

    }
}
