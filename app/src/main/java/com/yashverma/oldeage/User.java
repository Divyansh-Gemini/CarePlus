package com.yashverma.oldeage;

public class User {
    String guestid;
    String guestName;

    public User() {
    }

    public User(String guestid, String guestName) {
        this.guestid = guestid;
        this.guestName = guestName;
    }

    public String getGuestid() {
        return guestid;
    }

    public String getGuestName() {
        return guestName;
    }


}
