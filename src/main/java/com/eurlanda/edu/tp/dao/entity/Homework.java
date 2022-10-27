package com.eurlanda.edu.tp.dao.entity;

import com.eurlanda.edu.tp.Util.Utility;

import java.text.ParseException;
import java.util.Date;

public class Homework {
    private Integer id;

    private Integer moduleId;

    private String name;

    private String description;

    private Integer cloudwareType;

    private Date publishDate;

    private Date deadlineDate;

    private Integer classId;

    public Homework(Integer id, Integer moduleId, String name, String description, Integer cloudwareType, Date publishDate, Date deadlineDate, Integer classId) {
        this.id = id;
        this.moduleId = moduleId;
        this.name = name;
        this.description = description;
        this.cloudwareType = cloudwareType;
        this.publishDate = publishDate;
        this.deadlineDate = deadlineDate;
        this.classId = classId;
    }

    public Homework() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getCloudwareType() {
        return cloudwareType;
    }

    public void setCloudwareType(Integer cloudwareType) {
        this.cloudwareType = cloudwareType;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setPublishDate(String publishDate) {
        try {
            this.publishDate = Utility.parseShortDate(publishDate);

        } catch (ParseException e){}
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        try {
            this.deadlineDate = Utility.parseShortDate(deadlineDate);

        } catch (ParseException e){}
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }
}