package com.example.rentme.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Configurations implements Serializable {

    private List<String> areaNames;
    private List<String> stateOptions;
    private List<String> rentOptions;
    private List<String> categoriesOptions;
    private List<String> rentTimes;

    public Configurations() {
        this.areaNames = Arrays.asList("מרכז","דרום");
        this.stateOptions = Arrays.asList("בחר מצב...", "חדש", "כמו חדש", "משומש", "שבור");
        this.rentOptions = Arrays.asList("לשעה", "ליום", "לשבוע", "לחודש", "לשנה");
        this.categoriesOptions = Arrays.asList("מוצרי חשמל","אביזרים","קטגורייה לדוגמא");
        this.rentTimes = Arrays.asList("יום","שבוע");
    }

    // Setters and Getters
    public List<String> getAreaNames() {
        return areaNames;
    }

    public void setAreaNames(List<String> areaNames) {
        this.areaNames = areaNames;
    }

    public List<String> getStateOptions() {
        return stateOptions;
    }

    public void setStateOptions(List<String> stateOptions) {
        this.stateOptions = stateOptions;
    }

    public List<String> getRentOptions() {
        return rentOptions;
    }

    public void setRentOptions(List<String> rentOptions) {
        this.rentOptions = rentOptions;
    }

    public List<String> getCategoriesOptions() {
        return categoriesOptions;
    }

    public void setCategoriesOptions(List<String> categoriesOptions) {
        this.categoriesOptions = categoriesOptions;
    }

    public List<String> getRentTimes() {
        return rentTimes;
    }

    public void setRentTimes(List<String> rentTimes) {
        this.rentTimes = rentTimes;
    }

}
