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
    private List<String> adminsList;

    public List<String> getAdminsList() {
        return adminsList;
    }

    public void setAdminsList(List<String> adminsList) {
        this.adminsList = adminsList;
    }

    public Configurations() {}

    public Configurations(List<String> areaNames, List<String> stateOptions, List<String> rentOptions, List<String> categoriesOptions, List<String> rentTimes, List<String> adminsList) {
        this.areaNames = areaNames;
        this.stateOptions = stateOptions;
        this.rentOptions = rentOptions;
        this.categoriesOptions = categoriesOptions;
        this.rentTimes = rentTimes;
        this.adminsList = adminsList;
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
