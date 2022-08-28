package com.example.mobilebankinglicenta.classes;

public class Address {
    private String City;
    private String Country;
    private String District;
    private int PostalCode;
    private String AddressLine1;

    public Address() {
    }

    public Address(String city, String country, String district, int postalCode, String addressLine1) {
        City = city;
        Country = country;
        District = district;
        PostalCode = postalCode;
        AddressLine1 = addressLine1;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public int getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(int postalCode) {
        PostalCode = postalCode;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Address{");
        sb.append("City='").append(City).append('\'');
        sb.append(", Country='").append(Country).append('\'');
        sb.append(", District='").append(District).append('\'');
        sb.append(", PostalCode=").append(PostalCode);
        sb.append(", AddressLine1='").append(AddressLine1).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
