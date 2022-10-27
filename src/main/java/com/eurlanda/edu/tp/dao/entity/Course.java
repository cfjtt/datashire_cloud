package com.eurlanda.edu.tp.dao.entity;

public class Course {
    private Integer id;



    private String name;

    private String description;

    private String imageUrl;
    //该课程的文件名--创建该课程时的时间戳
    private String folderName;

    private boolean programExist;

    private boolean shulieyunExist;

    private String volumeId;

    private String author;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Course(Integer id,  String name, String description, String volumeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.volumeId = volumeId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return getId().equals(course.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public Course(String name, String description) {
        this(0,  name, description,null);
    }

    public Course() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isProgramExist() {
        return programExist;
    }

    public void setProgramExist(boolean programExist) {
        this.programExist = programExist;
    }

    public boolean isShulieyunExist() {
        return shulieyunExist;
    }

    public void setShulieyunExist(boolean shulieyunExist) {
        this.shulieyunExist = shulieyunExist;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }
}