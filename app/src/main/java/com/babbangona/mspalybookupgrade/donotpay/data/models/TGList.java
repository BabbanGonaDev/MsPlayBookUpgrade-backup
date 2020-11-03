package com.babbangona.mspalybookupgrade.donotpay.data.models;

public class TGList {

    private String unique_member_id;
    private String ik_number;
    private String first_name;
    private String last_name;
    private String village_name;

    public TGList(String unique_member_id, String ik_number, String first_name, String last_name, String village_name) {
        this.unique_member_id = unique_member_id;
        this.ik_number = ik_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.village_name = village_name;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public String getIk_number() {
        return ik_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getVillage_name() {
        return village_name;
    }
}
