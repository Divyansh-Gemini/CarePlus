package com.yashverma.oldeage;

public class Hospitalization_Info {
String hospitalization_Id,Guest_id,Hospital_Id,Admit_date,Discharge_date,Treatment;

    public Hospitalization_Info() {
    }

    public Hospitalization_Info(String hospitalization_Id, String guest_id, String hospital_Id, String admit_date, String discharge_date, String treatment) {
        this.hospitalization_Id = hospitalization_Id;
        Guest_id = guest_id;
        Hospital_Id = hospital_Id;
        Admit_date = admit_date;
        Discharge_date = discharge_date;
        Treatment = treatment;
    }

    public String getHospitalization_Id() {
        return hospitalization_Id;
    }

    public void setHospitalization_Id(String hospitalization_Id) {
        this.hospitalization_Id = hospitalization_Id;
    }

    public String getGuest_id() {
        return Guest_id;
    }

    public void setGuest_id(String guest_id) {
        Guest_id = guest_id;
    }

    public String getHospital_Id() {
        return Hospital_Id;
    }

    public void setHospital_Id(String hospital_Id) {
        Hospital_Id = hospital_Id;
    }

    public String getAdmit_date() {
        return Admit_date;
    }

    public void setAdmit_date(String admit_date) {
        Admit_date = admit_date;
    }

    public String getDischarge_date() {
        return Discharge_date;
    }

    public void setDischarge_date(String discharge_date) {
        Discharge_date = discharge_date;
    }

    public String getTreatment() {
        return Treatment;
    }

    public void setTreatment(String treatment) {
        Treatment = treatment;
    }
}
