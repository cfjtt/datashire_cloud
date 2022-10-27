package com.eurlanda.edu.tp.webdomain.response;

import java.util.List;

public class ResLogin {
    private int id;
    private int role;
    private String name;
    private String token;
    private List<ResLoginNavlist> resLoginNavlists;


    public class ResLoginNavlist {
        private String name;
        private String url;
        private String uiClass;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUiClass() {
            return uiClass;
        }

        public void setUiClass(String uiClass) {
            this.uiClass = uiClass;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ResLoginNavlist> getResLoginNavlists() {
        return resLoginNavlists;
    }

    public void setResLoginNavlists(List<ResLoginNavlist> resLoginNavlists) {
        this.resLoginNavlists = resLoginNavlists;
    }
}
