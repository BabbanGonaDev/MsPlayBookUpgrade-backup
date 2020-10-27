package com.babbangona.mspalybookupgrade.tpo.data.models;

public class MemberModel {

    private String unique_member_id;
    private String first_name;
    private String last_name;
    private String ik_number;
    private String phone_number;
    private String coach_id;

    public MemberModel(String unique_member_id, String first_name, String last_name, String ik_number, String phone_number, String coach_id) {
        this.unique_member_id = unique_member_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ik_number = ik_number;
        this.phone_number = phone_number;
        this.coach_id = coach_id;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }
}
