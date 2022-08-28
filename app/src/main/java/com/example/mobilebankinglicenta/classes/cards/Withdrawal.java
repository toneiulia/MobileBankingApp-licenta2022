package com.example.mobilebankinglicenta.classes.cards;

public class Withdrawal {
    private String Location;
    private int Amount;
    private String Date;

    public Withdrawal() {
    }
    public Withdrawal(String location, int amount, String date) {
        Location = location;
        Amount = amount;
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Withdrawal{");
        sb.append("Location='").append(Location).append('\'');
        sb.append(", Amount=").append(Amount);
        sb.append(", Date='").append(Date).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
