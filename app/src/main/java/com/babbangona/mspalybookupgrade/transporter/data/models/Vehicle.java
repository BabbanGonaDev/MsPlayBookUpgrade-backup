package com.babbangona.mspalybookupgrade.transporter.data.models;

public class Vehicle {

    private String vehicle_type;
    private Integer vehicle_image;

    public Vehicle(String vehicle_type, Integer vehicle_image) {
        this.vehicle_type = vehicle_type;
        this.vehicle_image = vehicle_image;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public Integer getVehicle_image() {
        return vehicle_image;
    }

    public void setVehicle_image(Integer vehicle_image) {
        this.vehicle_image = vehicle_image;
    }
}
