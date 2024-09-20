package com.ioad.wac.model;

public class Locations {

    private String id;
    private String location;
    private String bookmark;
    private String saveDate;
    private String deleteStatus;
    private String deleteDate;


    public Locations(String id, String location, String bookmark, String saveDate, String deleteStatus, String deleteDate) {
        this.id = id;
        this.location = location;
        this.bookmark = bookmark;
        this.saveDate = saveDate;
        this.deleteStatus = deleteStatus;
        this.deleteDate = deleteDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }
}
