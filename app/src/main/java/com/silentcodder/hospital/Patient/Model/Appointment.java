package com.silentcodder.hospital.Patient.Model;

import com.silentcodder.hospital.Counter.Model.AppointmentId;

public class Appointment extends AppointmentId {
    String AppointmentDate;
    String ChildName;
    String Problem;
    String UserId;
    String Gender;
    String FileNumber;

    public Appointment() {
    }

    public Appointment(String appointmentDate, String childName, String problem, String userId,String gender,String fileNumber) {
        AppointmentDate = appointmentDate;
        ChildName = childName;
        Problem = problem;
        UserId = userId;
        Gender = gender;
        FileNumber = fileNumber;
    }

    public String getFileNumber() {
        return FileNumber;
    }

    public String setFileNumber(String fileNumber) {
        FileNumber = fileNumber;
        return fileNumber;
    }

    public String getGender() {
        return Gender;
    }

    public String setGender(String gender) {
        Gender = gender;
        return gender;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getChildName() {
        return ChildName;
    }

    public String setChildName(String childName) {
        ChildName = childName;
        return childName;
    }

    public String getProblem() {
        return Problem;
    }

    public String setProblem(String problem) {
        Problem = problem;
        return problem;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


}
