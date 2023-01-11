package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// #################################################################################################

import java.util.ArrayList;
import java.util.List;

public class Medication {
    int hospitalization_id, guest_id, medicine_id;
    String schedule, start_date, end_date;
    List<String> dates= new ArrayList<String>();

    public Medication(int hospitalization_id, int guest_id, int medicine_id, String schedule,ArrayList<String> dtes) {
        this.hospitalization_id = hospitalization_id;
        this.guest_id = guest_id;
        this.medicine_id = medicine_id;
        this.schedule = schedule;
        dates=dtes;
    }

    public int getHospitalization_id() {
        return hospitalization_id;
    }

    public void setHospitalization_id(int hospitalization_id) {
        this.hospitalization_id = hospitalization_id;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}