package com.babbangona.mspalybookupgrade.transporter.data.models;

public class CustomTransporter {

    private String first_name;
    private String last_name;
    private String areas;
    private String phone_number;
    private Integer bags_transported;
    private Integer active_favourite;

    public CustomTransporter(String first_name, String last_name, String areas, String phone_number, Integer bags_transported, Integer active_favourite) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.areas = areas;
        this.phone_number = phone_number;
        this.bags_transported = bags_transported;
        this.active_favourite = active_favourite;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAreas() {
        return areas;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Integer getBags_transported() {
        return bags_transported;
    }

    public boolean isFavourite() {
        return active_favourite == 1;
    }
}
