package com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler;

public class PCPWSRecyclerModel {

    private String pws_id;

    private String unique_field_id;

    private String member_name;

    private String member_r_id;

    private String date_logged;

    private String category;

    public PCPWSRecyclerModel(String pws_id, String unique_field_id, String member_name, String member_r_id,
                              String date_logged, String category) {
        this.pws_id = pws_id;
        this.unique_field_id = unique_field_id;
        this.member_name = member_name;
        this.member_r_id = member_r_id;
        this.date_logged = date_logged;
        this.category = category;
    }

    public String getPws_id() {
        return pws_id;
    }

    public void setPws_id(String pws_id) {
        this.pws_id = pws_id;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_r_id() {
        return member_r_id;
    }

    public void setMember_r_id(String member_r_id) {
        this.member_r_id = member_r_id;
    }

    public String getDate_logged() {
        return date_logged;
    }

    public void setDate_logged(String date_logged) {
        this.date_logged = date_logged;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
