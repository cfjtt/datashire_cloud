package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResModuleImages {

    private List<ResModuleImage> imageList;

    public List<ResModuleImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<ResModuleImage> imageList) {
        this.imageList = imageList;
    }

    public static class ResModuleImage {
        private int resourceId;
        private String name;
        private String imageUrl;
        private int width;
        private int height;

        public ResModuleImage(int resourceId, String name, String imageUrl, int width, int height) {
            this.resourceId = resourceId;
            this.name = name;
            this.imageUrl = imageUrl;
            this.width = width;
            this.height = height;
        }

        public ResModuleImage() {
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
