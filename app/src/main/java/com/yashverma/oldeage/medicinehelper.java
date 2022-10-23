package com.yashverma.oldeage;

public class medicinehelper {
    String medicine_id,medicine_name,medicine_type,medicine_stock_count;

    public medicinehelper(String med_id, String med_name, String med_company, String med_Type, String med_Stock) {
    }

    public medicinehelper(String medicine_id, String medicine_name, String medicine_type, String medicine_stock_count) {
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.medicine_type = medicine_type;
        this.medicine_stock_count = medicine_stock_count;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_type() {
        return medicine_type;
    }

    public void setMedicine_type(String medicine_type) {
        this.medicine_type = medicine_type;
    }

    public String getMedicine_stock_count() {
        return medicine_stock_count;
    }

    public void setMedicine_stock_count(String medicine_stock_count) {
        this.medicine_stock_count = medicine_stock_count;
    }
}
