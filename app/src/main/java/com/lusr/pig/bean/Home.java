package com.lusr.pig.bean;

public class Home {
    private String homeLink;
    private String homeNum;
    //    房间可容纳数量
    private Integer homeSize;
    private Integer homeStatus;
    private Integer homeType;
    private String id;
    private Integer pigNum;

    public Integer getHomeType() {
        return homeType;
    }

    public void setHomeType(Integer homeType) {
        this.homeType = homeType;
    }

    public Integer getHomeStatus() {
        return homeStatus;
    }

    public void setHomeStatus(Integer homeStatus) {
        this.homeStatus = homeStatus;
    }

    public String getHomeLink() {
        return homeLink;
    }

    public void setHomeLink(String homeLink) {
        this.homeLink = homeLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeNum() {
        return homeNum;
    }

    public void setHomeNum(String homeNum) {
        this.homeNum = homeNum;
    }

    public Integer getPigNum() {
        return pigNum;
    }

    public void setPigNum(Integer pigNum) {
        this.pigNum = pigNum;
    }

    public Integer getHomeSize() {
        return homeSize;
    }

    public void setHomeSize(Integer homeSize) {
        this.homeSize = homeSize;
    }
}
