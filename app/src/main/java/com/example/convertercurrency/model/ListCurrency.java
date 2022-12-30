package com.example.convertercurrency.model;

public class ListCurrency {
    private String numberCode, nameCode, units, currencyName, course;

    public ListCurrency(String numberCode, String nameCode, String units, String currencyName, String course) {
        this.numberCode = numberCode;
        this.nameCode = nameCode;
        this.units = units;
        this.currencyName = currencyName;
        this.course = course;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
