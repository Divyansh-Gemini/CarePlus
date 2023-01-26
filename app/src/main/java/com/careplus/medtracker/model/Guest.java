package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// #################################################################################################

public class Guest {
    int guestID;
    String guest_image_uri;
    String guestName;
    int guestAge;
    String guestGender;
    String guestDateOfAdmit;
    String guestKnownName;
    String guestKnownNumber;
    String guestAddress;

    // Default constructor
    public Guest ()
    {   }

    // Parameterized constructor
    public Guest(int guestID, String guest_image_uri, String guestName, int guestAge, String guestGender,
                 String guestDateOfAdmit, String guestKnownName, String guestKnownNumber,
                 String guestAddress) {
        this.guestID = guestID;
        this.guest_image_uri = guest_image_uri;
        this.guestName = guestName;
        this.guestAge = guestAge;
        this.guestGender = guestGender;
        this.guestDateOfAdmit = guestDateOfAdmit;
        this.guestKnownName = guestKnownName;
        this.guestKnownNumber = guestKnownNumber;
        this.guestAddress = guestAddress;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public String getGuestImageURI() {
        return guest_image_uri;
    }

    public void setGuestImageURI(String guest_image_uri) {
        this.guest_image_uri = guest_image_uri;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getGuestAge() {
        return guestAge;
    }

    public void setGuestAge(int guestAge) {
        this.guestAge = guestAge;
    }

    public String getGuestGender() {
        return guestGender;
    }

    public void setGuestGender(String guestGender) {
        this.guestGender = guestGender;
    }

    public String getGuestDateOfAdmit() {
        return guestDateOfAdmit;
    }

    public void setGuestDateOfAdmit(String guestDateOfAdmit) {
        this.guestDateOfAdmit = guestDateOfAdmit;
    }

    public String getGuestKnownName() {
        return guestKnownName;
    }

    public void setGuestKnownName(String guestKnownName) {
        this.guestKnownName = guestKnownName;
    }

    public String getGuestKnownNumber() {
        return guestKnownNumber;
    }

    public void setGuestKnownNumber(String guestKnownNumber) {
        this.guestKnownNumber = guestKnownNumber;
    }

    public String getGuestAddress() {
        return guestAddress;
    }

    public void setGuestAddress(String guestAddress) {
        this.guestAddress = guestAddress;
    }
}