package com.silentcodder.hospital.Patient.Model;

public class ChildData {
    String ChildName;
    String ChildDOB;
    String ChildGender;
    String FileNumber;
    String ParentId;
    Long TimeStamp;

    public ChildData() {
    }

    public ChildData(String childName, String childDOB, String childGender, String fileNumber, Long timeStamp, String parentId) {
        ChildName = childName;
        ChildDOB = childDOB;
        ChildGender = childGender;
        FileNumber = fileNumber;
        ParentId = parentId;
        TimeStamp = timeStamp;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getChildName() {
        return ChildName;
    }

    public String setChildName(String childName) {
        ChildName = childName;
        return childName;
    }

    public String getChildDOB() {
        return ChildDOB;
    }

    public String setChildDOB(String childDOB) {
        ChildDOB = childDOB;
        return childDOB;
    }

    public String getChildGender() {
        return ChildGender;
    }

    public String setChildGender(String childGender) {
        ChildGender = childGender;
        return childGender;
    }

    public String getFileNumber() {
        return FileNumber;
    }

    public String setFileNumber(String fileNumber) {
        FileNumber = fileNumber;
        return fileNumber;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }
}
