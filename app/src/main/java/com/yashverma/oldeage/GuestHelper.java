package com.yashverma.oldeage;

public class GuestHelper {
    String Guestid;
    String GuestName;
    String Guestage;
    String GuestDateofJoining;
    String GuestAddress;
    String GuestKnownNumber;
    String GuestKnownName;
    String caketakerId;

    public String getGuestid() {
        return Guestid;
    }

    public void setGuestid(String guestid) {
        Guestid = guestid;
    }

    public String getGuestName() {
        return GuestName;
    }

    public void setGuestName(String guestName) {
        GuestName = guestName;
    }

    public String getGuestage() {
        return Guestage;
    }

    public void setGuestage(String guestage) {
        Guestage = guestage;
    }

    public String getGuestDateofJoining() {
        return GuestDateofJoining;
    }

    public void setGuestDateofJoining(String guestDateofJoining) {
        GuestDateofJoining = guestDateofJoining;
    }

    public String getGuestAddress() {
        return GuestAddress;
    }

    public void setGuestAddress(String guestAddress) {
        GuestAddress = guestAddress;
    }

    public String getGuestKnownNumber() {
        return GuestKnownNumber;
    }

    public void setGuestKnownNumber(String guestKnownNumber) {
        GuestKnownNumber = guestKnownNumber;
    }

    public String getGuestKnownName() {
        return GuestKnownName;
    }

    public void setGuestKnownName(String guestKnownName) {
        GuestKnownName = guestKnownName;
    }

    public String getCaketakerId() {
        return caketakerId;
    }

    public void setCaketakerId(String caketakerId) {
        this.caketakerId = caketakerId;
    }

    public GuestHelper(String guestid, String guestName, String guestage, String guestDateofJoining, String guestAddress, String guestKnownNumber, String guestKnownName, String caketakerId) {
        Guestid = guestid;
        GuestName = guestName;
        Guestage = guestage;
        GuestDateofJoining = guestDateofJoining;
        GuestAddress = guestAddress;
        GuestKnownNumber = guestKnownNumber;
        GuestKnownName = guestKnownName;
        this.caketakerId = caketakerId;
    }

    public GuestHelper(){}
}
