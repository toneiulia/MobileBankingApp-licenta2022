package com.example.mobilebankinglicenta.classes;

import java.util.Map;

public class BankLocation {
    private String Address;
    private String Name;
    private Map<String, String> WorkProgram;

    public BankLocation(String name, Map<String, String> workProgram) {
        Name = name;
        WorkProgram = workProgram;
    }

    public BankLocation()
    {
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Map<String, String> getWorkProgram() {
        return WorkProgram;
    }

    public void setWorkProgram(Map<String, String> workProgram) {
        WorkProgram = workProgram;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BankLocation{");
        sb.append("Address='").append(Address).append('\'');
        sb.append(", Name='").append(Name).append('\'');
        sb.append(", WorkProgram=").append(WorkProgram);
        sb.append('}');
        return sb.toString();
    }
}
