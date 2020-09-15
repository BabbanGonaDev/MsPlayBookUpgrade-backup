package com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler;

public class MemberListRecyclerModel {

    private String unique_member_id;

    private String member_name;

    private String role;

    private String village;

    private String ik_number;

    private String member_r_id;

    public MemberListRecyclerModel(String unique_member_id, String member_name, String role, String village, String ik_number, String member_r_id) {
        this.unique_member_id = unique_member_id;
        this.member_name = member_name;
        this.role = role;
        this.village = village;
        this.ik_number = ik_number;
        this.member_r_id = member_r_id;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getMember_r_id() {
        return member_r_id;
    }

    public void setMember_r_id(String member_r_id) {
        this.member_r_id = member_r_id;
    }
}
