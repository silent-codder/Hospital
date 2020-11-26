package com.silentcodder.hospital.Patient.Model;

public class Appointment {
    String AppointmentDate;
    String ChildName;
    String Problem;
    String UserId;
    String Gender;

    public Appointment() {
    }

    public Appointment(String appointmentDate, String childName, String problem, String userId,String gender) {
        AppointmentDate = appointmentDate;
        ChildName = childName;
        Problem = problem;
        UserId = userId;
        Gender = gender;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
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

    public void setChildName(String childName) {
        ChildName = childName;
    }

    public String getProblem() {
        return Problem;
    }

    public void setProblem(String problem) {
        Problem = problem;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
