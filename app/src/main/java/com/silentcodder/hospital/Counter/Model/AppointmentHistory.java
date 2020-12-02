package com.silentcodder.hospital.Counter.Model;

public class AppointmentHistory {
    String ChildFileNumber,ChildGender,ChildName,ChildProblem;
    String ParentName,ParentAddress,ParentMobile;
    String ID;

    public AppointmentHistory() {
    }

    public AppointmentHistory(String childFileNumber, String childGender, String childName,
                              String childProblem, String parentName, String parentAddress, String parentMobile, String ID) {
        ChildFileNumber = childFileNumber;
        ChildGender = childGender;
        ChildName = childName;
        ChildProblem = childProblem;
        ParentName = parentName;
        ParentAddress = parentAddress;
        ParentMobile = parentMobile;
        this.ID = ID;
    }

    public String getChildFileNumber() {
        return ChildFileNumber;
    }

    public void setChildFileNumber(String childFileNumber) {
        ChildFileNumber = childFileNumber;
    }

    public String getChildGender() {
        return ChildGender;
    }

    public void setChildGender(String childGender) {
        ChildGender = childGender;
    }

    public String getChildName() {
        return ChildName;
    }

    public void setChildName(String childName) {
        ChildName = childName;
    }

    public String getChildProblem() {
        return ChildProblem;
    }

    public void setChildProblem(String childProblem) {
        ChildProblem = childProblem;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }

    public String getParentAddress() {
        return ParentAddress;
    }

    public void setParentAddress(String parentAddress) {
        ParentAddress = parentAddress;
    }

    public String getParentMobile() {
        return ParentMobile;
    }

    public void setParentMobile(String parentMobile) {
        ParentMobile = parentMobile;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
