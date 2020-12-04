package com.silentcodder.hospital.Model;

public class ChildFile {

    String Prescription,UserId,ImageUrl;
    Long TimeStamp;

    public ChildFile() {
    }

    public ChildFile(String prescription, String userId, String imageUrl, Long timeStamp) {
        Prescription = prescription;
        UserId = userId;
        ImageUrl = imageUrl;
        TimeStamp = timeStamp;
    }

    public String getPrescription() {
        return Prescription;
    }

    public void setPrescription(String prescription) {
        Prescription = prescription;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }
}
