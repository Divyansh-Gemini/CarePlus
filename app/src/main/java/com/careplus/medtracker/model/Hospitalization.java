package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// #################################################################################################

public class Hospitalization {
    int hospitalizationID;
    int guestID;
    int hospitalID;
    String admitDate;
    String treatment;

    // Default constructor
    public Hospitalization()
    {   }

    // Parameterized constructor
    public Hospitalization(int hospitalizationID, int guestID, int hospitalID,
                           String admitDate, String treatment) {
        this.hospitalizationID = hospitalizationID;
        this.guestID = guestID;
        this.hospitalID = hospitalID;
        this.admitDate = admitDate;
        this.treatment = treatment;
    }

    public int getHospitalizationID() {
        return hospitalizationID;
    }

    public void setHospitalizationID(int hospitalizationID) {
        this.hospitalizationID = hospitalizationID;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(int hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getAdmitDate() {
        return admitDate;
    }

    public void setAdmitDate(String admitDate) {
        this.admitDate = admitDate;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
}