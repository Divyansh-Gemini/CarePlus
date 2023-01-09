package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// #################################################################################################

public class Medication {
    int medicationID;
    int guestID;
    int medicineID;
    String schedule;
    String dose;
    String startDate;
    String endDate;
    String remarks;
    String Reason;

    // Default constructor
    public Medication()
    {   }

    // Parameterized constructor
    public Medication(int medicationID, int guestID, int medicineID, String schedule,
                      String dose, String startDate, String endDate, String remarks, String reason) {
        this.medicationID = medicationID;
        this.guestID = guestID;
        this.medicineID = medicineID;
        this.schedule = schedule;
        this.dose = dose;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remarks = remarks;
        Reason = reason;
    }

    public int getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(int medicationID) {
        this.medicationID = medicationID;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}