package com.example.mobilebankinglicenta.classes.cards;

public class CardType {
    private String Name;
    private String Type;
    private boolean isMastercard;
    private boolean isVisa;
    private boolean TravelInsurance;

    //FOR CREDIT ONLY
    private int AnnualInterest;
    private int MaximumAmount;
    private int MinimumIncome;

    public CardType(String name, String type, boolean isMastercard, boolean isVisa, boolean travelInsurance) {
        Name = name;
        Type = type;
        this.isMastercard = isMastercard;
        this.isVisa = isVisa;
        TravelInsurance = travelInsurance;

    }

    public CardType(String name, String type, boolean isMastercard, boolean isVisa, boolean travelInsurance, int annualInterest, int maximumAmount, int minimumIncome) {
        Name = name;
        Type = type;
        this.isMastercard = isMastercard;
        this.isVisa = isVisa;
        TravelInsurance = travelInsurance;
        AnnualInterest = annualInterest;
        MaximumAmount = maximumAmount;
        MinimumIncome = minimumIncome;
    }

    public CardType() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean getIsMastercard() {
        return isMastercard;
    }

    public void setIsMastercard(boolean mastercard) {
        isMastercard = mastercard;
    }

    public boolean getIsVisa() {
        return isVisa;
    }

    public void setIsVisa(boolean visa) {
        isVisa = visa;
    }

    public boolean getTravelInsurance() {
        return TravelInsurance;
    }

    public void setTravelInsurance(boolean travelInsurance) {
        TravelInsurance = travelInsurance;
    }

    public int getAnnualInterest() {
        return AnnualInterest;
    }

    public void setAnnualInterest(int annualInterest) {
        AnnualInterest = annualInterest;
    }

    public int getMaximumAmount() {
        return MaximumAmount;
    }

    public void setMaximumAmount(int maximumAmount) {
        MaximumAmount = maximumAmount;
    }

    public int getMinimumIncome() {
        return MinimumIncome;
    }

    public void setMinimumIncome(int minimumIncome) {
        MinimumIncome = minimumIncome;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CardType{");
        sb.append("Name='").append(Name).append('\'');
        sb.append(", Type='").append(Type).append('\'');
        sb.append(", isMastercard=").append(isMastercard);
        sb.append(", isVisa=").append(isVisa);
        sb.append(", TravelInsurance=").append(TravelInsurance);

        if (this.getType().equals("Credit")){

            sb.append(", AnnualInterest=").append(AnnualInterest).append("%");
            sb.append(", MaximumAmount=").append(MaximumAmount);
            sb.append(", MinimumIncome=").append(MinimumIncome);
            sb.append('}');
        }

        return sb.toString();
    }


}
