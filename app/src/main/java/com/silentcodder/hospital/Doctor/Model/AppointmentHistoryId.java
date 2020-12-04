package com.silentcodder.hospital.Doctor.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class AppointmentHistoryId {
    @Exclude

    public String AppointmentHistoryId;
    public <T extends AppointmentHistoryId> T withId(@NonNull final String id){
        this.AppointmentHistoryId = id;
        return (T)this;
    }
}
