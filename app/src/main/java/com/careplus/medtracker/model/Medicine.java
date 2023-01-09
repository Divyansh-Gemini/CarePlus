package com.careplus.medtracker.model;

// #################################################################################################
// - This is an Entity class for Guest, also called as Model.
// - An Entity class represents a data object that can be persisted to a database.
// - It contains fields that correspond to the columns of a table in the database.

// In short --> - Koi bhi data database me daalne k liye hm uska object bnate h, fir usey send kr dete h, esa.
//              - Isme (properties, default & parameterized constructor, getter & setter methods) hote h.
// ##################################################################################################

public class Medicine {
    int medicineID;
    String medicineName;
    String medicineCompany;
    String medicineType;
    int medicineStockCount;

    // Default constructor
    public Medicine()
    {   }

    // Parameterized constructor
    public Medicine(int medicineID, String medicineName, String medicineCompany,
                    String medicineType, int medicineStockCount) {
        this.medicineID = medicineID;
        this.medicineName = medicineName;
        this.medicineCompany = medicineCompany;
        this.medicineType = medicineType;
        this.medicineStockCount = medicineStockCount;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineCompany() {
        return medicineCompany;
    }

    public void setMedicineCompany(String medicineCompany) {
        this.medicineCompany = medicineCompany;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public int getMedicineStockCount() {
        return medicineStockCount;
    }

    public void setMedicineStockCount(int medicineStockCount) {
        this.medicineStockCount = medicineStockCount;
    }
}