package com.example.mobilebankinglicenta.classes;

public class Provider {

    private String Domain;
    private String IBAN;
    private String Name;
    private boolean isUtility;

    public Provider() {
    }

    public Provider(String domain, String IBAN, String name, boolean isUtility) {
        Domain = domain;
        this.IBAN = IBAN;
        Name = name;
        this.isUtility = isUtility;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean getIsUtility() {
        return isUtility;
    }

    public void setIsUtility(boolean utility) {
        isUtility = utility;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(Name).append(" - ").append(Domain);
        return sb.toString();
    }
}
