package com.example.mobilebankinglicenta.classes.cards;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private double Balance;
    private String Currency;
    private String EndDate;
    private String StartDate;
    private String IBAN;
    private String Number;
    private String Owner;
    private String Type;
    private int CVV;
    private int Pin;
    private boolean Blocat;

    //se completeaza dupa
    private List<Transaction> Transactions=new ArrayList<>();
    private List<Withdrawal> Withdrawals=new ArrayList<>();
    private List<Deposit> Deposits=new ArrayList<>();

    public Card(double balance, String currency, String endDate, String startDate, String IBAN, String number, String owner, String type, int cvv) {
        Balance = balance;
        Currency = currency;
        EndDate = endDate;
        StartDate = startDate;
        this.IBAN = IBAN;
        Number = number;
        Owner = owner;
        Type = type;
        CVV=cvv;
    }

    public Card() {
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public List<Transaction> getTransactions() {
        return Transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        Transactions = transactions;
    }

    public List<Withdrawal> getWithdrawals() {
        return Withdrawals;
    }

    public void setWithdrawals(List<Withdrawal> withdrawals) {
        Withdrawals = withdrawals;
    }

    public List<Deposit> getDeposits() {
        return Deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        Deposits = deposits;
    }

    public int getCVV() {
        return CVV;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    public int getPin() {
        return Pin;
    }

    public void setPin(int pin) {
        Pin = pin;
    }

    public boolean isBlocat() {
        return Blocat;
    }

    public void setBlocat(boolean blocat) {
        Blocat = blocat;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Card{");
        sb.append("Balance=").append(Balance);
        sb.append(", Currency='").append(Currency).append('\'');
        sb.append(", EndDate='").append(EndDate).append('\'');
        sb.append(", StartDate='").append(StartDate).append('\'');
        sb.append(", IBAN='").append(IBAN).append('\'');
        sb.append(", Number='").append(Number).append('\'');
        sb.append(", Owner='").append(Owner).append('\'');
        sb.append(", Type='").append(Type).append('\'');
        sb.append(", CVV=").append(CVV);
        sb.append('}');
        return sb.toString();
    }
}
