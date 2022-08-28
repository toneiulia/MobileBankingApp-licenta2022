package com.example.mobilebankinglicenta.classes.cards;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class Transaction {

    private float Amount;
    private String CardFrom;
    private String CardTo;
    private String Currency;
    private String Date;
    private String Description;
    private String Status;
    private boolean isUtilityBill;
    private String IBAN;

    public Transaction() {
    }

    public Transaction(float amount, String cardFrom, String cardTo, String currency, String date, String description, String status, boolean isUtilityBill) {
        Amount = amount;
        CardFrom = cardFrom;
        CardTo = cardTo;
        Currency = currency;
        Date = date;
        Description = description;
        Status = status;
        this.isUtilityBill = isUtilityBill;
    }

    public Transaction(float amount, String cardFrom, String cardTo, String currency, String date, String description, String status, boolean isUtilityBill, String IBAN) {
        Amount = amount;
        CardFrom = cardFrom;
        CardTo = cardTo;
        Currency = currency;
        Date = date;
        Description = description;
        Status = status;
        this.isUtilityBill = isUtilityBill;
        this.IBAN = IBAN;
    }

    public Transaction(float amount, String cardFrom, String date, String description, boolean isUtilityBill) {
        Amount = amount;
        CardFrom = cardFrom;
        Date = date;
        Description = description;
        this.isUtilityBill = isUtilityBill;
        IBAN="RO06PORL5856834318442176";
        Status="Procesata";
        CardTo=cardFrom;
        Currency="RON";
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public String getCardFrom() {
        return CardFrom;
    }

    public void setCardFrom(String cardFrom) {
        CardFrom = cardFrom;
    }

    public String getCardTo() {
        return CardTo;
    }

    public void setCardTo(String cardTo) {
        CardTo = cardTo;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean getIsUtilityBill() {
        return isUtilityBill;
    }

    public void setIsUtilityBill(boolean utilityBill) {
        isUtilityBill = utilityBill;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Transactions{");
        sb.append("Amount=").append(Amount);
        sb.append(", CardFrom='").append(CardFrom).append('\'');
        sb.append(", CardTo='").append(CardTo).append('\'');
        sb.append(", Currency='").append(Currency).append('\'');
        sb.append(", Date='").append(Date).append('\'');
        sb.append(", Description='").append(Description).append('\'');
        sb.append(", Status='").append(Status).append('\'');
        sb.append(", isUtilityBill=").append(isUtilityBill);
        sb.append('}');
        return sb.toString();
    }

    public static Comparator<Transaction> esteCrescator = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction o1, Transaction o2) {

            float amount1=o1.getAmount();
            float amount2=o2.getAmount();

            if(amount1<0) amount1=-amount1;
            if(amount2<0) amount2=-amount2;

            return Float.compare(amount1,amount2);
        }
    };

    public static Comparator<Transaction> esteCronologic = new Comparator<Transaction>() {
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @Override
        public int compare(Transaction o1, Transaction o2) {
            try {
                String date1=o1.getDate();
                String date2=o2.getDate();
                return -dateFormat.parse(date1).compareTo(dateFormat.parse(date2));
            } catch (ParseException e) {
                throw new IllegalArgumentException();
            }
        }
    };
}
