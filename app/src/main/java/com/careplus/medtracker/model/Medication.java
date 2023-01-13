package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// #################################################################################################

import java.util.List;
import java.util.Map;

public class Medication {
    int medication_id, guest_id, medicine_id;
    String schedule;
    //List<String> dates;
    Map<String, Boolean> dates_and_status;
            //= new HashMap<String, Integer>();

    public Medication()
    {   }


    public Medication(int medication_id, int guest_id, int medicine_id, String schedule, Map<String, Boolean> dates_and_status) {
        this.medication_id = medication_id;
        this.guest_id = guest_id;
        this.medicine_id = medicine_id;
        this.schedule = schedule;
        //this.dates = dates;
        this.dates_and_status = dates_and_status;
    }

    public int getMedication_id() {
        return medication_id;
    }

    public void setMedication_id(int medication_id) {
        this.medication_id = medication_id;
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

    public Map<String, Boolean> getDatesAndStatus() {
        return dates_and_status;
    }

    public void setDatesAndStatus(Map<String, Boolean> dates_and_status) {
        this.dates_and_status = dates_and_status;
    }
}