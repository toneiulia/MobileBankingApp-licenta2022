package com.example.mobilebankinglicenta.classes.cards;

public class Deposit {
    private int amount;
    private float interestRate;
    private String name;
    private int period;
    private int timeLeft;

    public Deposit(int amount, float interestRate, String name, int period, int timeLeft) {
        this.amount = amount;
        this.interestRate = interestRate;
        this.name = name;
        this.period = period;
        this.timeLeft = timeLeft;
    }

    public Deposit() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Deposit{");
        sb.append("Amount=").append(amount);
        sb.append(", InterestRate=").append(interestRate);
        sb.append(", Name='").append(name).append('\'');
        sb.append(", Period=").append(period);
        sb.append(", TimeLeft=").append(timeLeft);
        sb.append('}');
        return sb.toString();
    }
}
