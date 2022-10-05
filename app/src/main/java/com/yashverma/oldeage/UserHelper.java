package com.yashverma.oldeage;

public class UserHelper {
    public UserHelper()
    {}
    public UserHelper(String email, String name, String mobileNo, String age, String gender) {
        Email = email;
        Name = name;
        MobileNo = mobileNo;
        Age = age;
        Gender = gender;
    }

    String Email,Name,MobileNo,Age,Gender;


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
