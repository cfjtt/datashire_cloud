package com.eurlanda.edu.tp.dao.entity;

public class ModuleResource {
    private Integer id;

    private Integer moduleId;

    private Integer resourceId;

    private Integer type;

    public ModuleResource(Integer id, Integer moduleId, Integer resourceId, Integer type) {
        this.id = id;
        this.moduleId = moduleId;
        this.resourceId = resourceId;
        this.type = type;
    }

    public ModuleResource(Integer moduleId, Integer resourceId, Integer type) {
        this(0, moduleId, resourceId, type);
    }

    public ModuleResource() {
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

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}