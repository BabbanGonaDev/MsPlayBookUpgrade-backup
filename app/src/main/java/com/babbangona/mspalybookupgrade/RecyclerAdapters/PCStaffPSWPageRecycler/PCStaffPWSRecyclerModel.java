package com.babbangona.mspalybookupgrade.RecyclerAdapters.PCStaffPSWPageRecycler;

public class PCStaffPWSRecyclerModel {

    private String staff_name;

    private String staff_id;

    private int number_of_claims;

    private int number_reviewed_claims;

    public PCStaffPWSRecyclerModel(String staff_name, String staff_id, int number_of_claims, int number_reviewed_claims) {
        this.staff_name = staff_name;
        this.staff_id = staff_id;
        this.number_of_claims = number_of_claims;
        this.number_reviewed_claims = number_reviewed_claims;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public int getNumber_of_claims() {
        return number_of_claims;
    }

    public void setNumber_of_claims(int number_of_claims) {
        this.number_of_claims = number_of_claims;
    }

    public int getNumber_reviewed_claims() {
        return number_reviewed_claims;
    }

    public void setNumber_reviewed_claims(int number_reviewed_claims) {
        this.number_reviewed_claims = number_reviewed_claims;
    }
}
