package com.yashverma.oldeage;

public class medicationHElper {
    public String getMedication_id() {
        return Medication_id;
    }

    public void setMedication_id(String medication_id) {
        Medication_id = medication_id;
    }

    public String getGuest_id() {
        return Guest_id;
    }

    public void setGuest_id(String guest_id) {
        Guest_id = guest_id;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getDose() {
        return Dose;
    }

    public void setDose(String dose) {
        Dose = dose;
    }

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(String end_Date) {
        End_Date = end_Date;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCause_Disease_Reason() {
        return Cause_Disease_Reason;
    }

    public void setCause_Disease_Reason(String cause_Disease_Reason) {
        Cause_Disease_Reason = cause_Disease_Reason;
    }

    String Medication_id,Guest_id,medicine_id,Schedule,Dose,Start_Date,End_Date,Remarks,Cause_Disease_Reason;

    public medicationHElper(String medication_id, String guest_id, String medicine_id, String schedule, String dose, String start_Date, String end_Date, String remarks, String cause_Disease_Reason) {
        Medication_id = medication_id;
        Guest_id = guest_id;
        this.medicine_id = medicine_id;
        Schedule = schedule;
        Dose = dose;
        Start_Date = start_Date;
        End_Date = end_Date;
        Remarks = remarks;
        Cause_Disease_Reason = cause_Disease_Reason;
    }
    public medicationHElper(){}
}

