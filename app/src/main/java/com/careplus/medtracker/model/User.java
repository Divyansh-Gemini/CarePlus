package com.careplus.medtracker.model;

public class User {
    String old_age_home_name, email, password;

    public User(String old_age_home_name, String email, String password) {
        this.old_age_home_name = old_age_home_name;
        this.email = email;
        this.password = password;
    }

    public String getOld_age_home_name() {
        return old_age_home_name;
    }

    public void setOld_age_home_name(String old_age_home_name) {
        this.old_age_home_name = old_age_home_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
