package com.example.currencyconverter;

public class ListItem {
    private String charCode; // название
    private String fullName;  // столица
    private double value; // ресурс флага

    public ListItem(String charCode, String fullName, double value){

        this.charCode = charCode;
        this.fullName =fullName;
        this.value =value;
    }

    public String getCharCode() {
        return this.charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
