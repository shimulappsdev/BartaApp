package com.example.bartaapp.model;

import java.util.List;

public class UserStatus {
    private String name, profileImage;
    private long lastUpdate;
    private List<Status> statusList;

    public UserStatus() {
    }

    public UserStatus(String name, String profileImage, long lastUpdate, List<Status> statusList) {
        this.name = name;
        this.profileImage = profileImage;
        this.lastUpdate = lastUpdate;
        this.statusList = statusList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Status> statusList) {
        this.statusList = statusList;
    }
}
