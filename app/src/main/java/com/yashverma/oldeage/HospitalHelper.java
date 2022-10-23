package com.yashverma.oldeage;

public class HospitalHelper {
    String hospitalId,HospitalName,HospitalNumber,HospitalEmail,HospitalAddress;

    public HospitalHelper() {
    }

    public HospitalHelper(String hospitalId, String hospitalName, String hospitalNumber, String hospitalEmail, String hospitalAddress) {
        this.hospitalId = hospitalId;
        HospitalName = hospitalName;
        HospitalNumber = hospitalNumber;
        HospitalEmail = hospitalEmail;
        HospitalAddress = hospitalAddress;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHospitalNumber() {
        return HospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        HospitalNumber = hospitalNumber;
    }

    public String getHospitalEmail() {
        return HospitalEmail;
    }

    public void setHospitalEmail(String hospitalEmail) {
        HospitalEmail = hospitalEmail;
    }

    public String getHospitalAddress() {
        return HospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        HospitalAddress = hospitalAddress;
    }
}
