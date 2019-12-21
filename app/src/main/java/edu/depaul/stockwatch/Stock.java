package edu.depaul.stockwatch;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Stock{
    private String symbol;
    private String companyName;
    private double latestPrice;
    private double changeAmount;
    private double changePercentage;
    private String direction;
    private String changeAggregate;

    Stock(String symbol, String companyName,double latestPrice,double changeAmount,double changePercentage ){
        this.symbol=symbol;
        this.companyName=companyName;
        this.latestPrice=latestPrice;
        this.changePercentage=changePercentage;
        this.direction=this.changePercentage>0.0? "▲":"▼";
        this.changeAggregate=direction+" "+Double.toString(changeAmount)+"("+ Double.toString(changePercentage)+ "%"+")";
    }


    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public String getDirection() {
        return direction;
    }

    public String getChangeAggregate() {
        return changeAggregate;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setChangePercentage(Double changePercentage) {
        this.changePercentage = changePercentage;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setChangeAggregate(String changeAggregate) {
        this.changeAggregate = changeAggregate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", latestPrice='" + latestPrice + '\'' +
                ", changePercentage=" + changePercentage +
                '}';
    }
}
