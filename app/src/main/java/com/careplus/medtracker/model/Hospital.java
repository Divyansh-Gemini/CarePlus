package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// #################################################################################################

public class Hospital {
    int hospitalID;
    String hospitalName;
    String hospitalMobile;
    String HospitalEmail;
    String hospitalAddress;

    // Default constructor
    public Hospital()
    {   }

    // Parameterized constructor
    public Hospital(int hospitalID, String hospitalName, String hospitalMobile,
                    String hospitalEmail, String hospitalAddress) {
        this.hospitalID = hospitalID;
        this.hospitalName = hospitalName;
        this.hospitalMobile = hospitalMobile;
        HospitalEmail = hospitalEmail;
        this.hospitalAddress = hospitalAddress;
    }

    public int getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(int hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalMobile() {
        return hospitalMobile;
    }

    public void setHospitalMobile(String hospitalMobile) {
        this.hospitalMobile = hospitalMobile;
    }

    public String getHospitalEmail() {
        return HospitalEmail;
    }

    public void setHospitalEmail(String hospitalEmail) {
        HospitalEmail = hospitalEmail;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }
}