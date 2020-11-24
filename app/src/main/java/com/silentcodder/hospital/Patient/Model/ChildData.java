package com.silentcodder.hospital.Patient.Model;

public class ChildData {
    String ChildName;
    String ChildDOB;
    String ChildGender;
    String FileNumber;
    Long TimeStamp;

    public ChildData() {
    }

    public ChildData(String childName, String childDOB, String childGender, String fileNumber, Long timeStamp) {
        ChildName = childName;
        ChildDOB = childDOB;
        ChildGender = childGender;
        FileNumber = fileNumber;
        TimeStamp = timeStamp;
    }

    public String getChildName() {
        return ChildName;
    }

    public void setChildName(String childName) {
        ChildName = childName;
    }

    public String getChildDOB() {
        return ChildDOB;
    }

    public void setChildDOB(String childDOB) {
        ChildDOB = childDOB;
    }

    public String getChildGender() {
        return ChildGender;
    }

    public void setChildGender(String childGender) {
        ChildGender = childGender;
    }

    public String getFileNumber() {
        return FileNumber;
    }

    public void setFileNumber(String fileNumber) {
        FileNumber = fileNumber;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }
}
