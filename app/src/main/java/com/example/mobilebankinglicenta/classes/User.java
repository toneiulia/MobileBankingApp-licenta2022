package com.example.mobilebankinglicenta.classes;

public class User {

    private String Address;
    private String Birthday;
    private String CNP;
    private String Email;
    private String FirstName;
    private String IDCard;
    private String LastName;
    private String Phone;

    public User() {
    }

    public User(String address, String birthday, String CNP, String email, String firstName, String idCard, String lastName, String phone) {
        this.Address = address;
        this.Birthday = birthday;
        this.CNP = CNP;
        this.Email = email;
        this.FirstName = firstName;
        this.IDCard = idCard;
        this.LastName = lastName;
        this.Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("address='").append(Address).append('\'');
        sb.append(", birthday='").append(Birthday).append('\'');
        sb.append(", CNP='").append(CNP).append('\'');
        sb.append(", email='").append(Email).append('\'');
        sb.append(", firstName='").append(FirstName).append('\'');
        sb.append(", idCard='").append(IDCard).append('\'');
        sb.append(", lastName='").append(LastName).append('\'');
        sb.append(", phone='").append(Phone).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
