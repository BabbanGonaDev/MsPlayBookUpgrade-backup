package com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler;

public class FieldListRecyclerModel {

    private String unique_field_id;

    private String field_r_id;

    private String member_name;

    private String phone_number;

    private String village_name;

    private String min_lat;

    private String max_lat;

    private String min_lng;

    private String max_lng;

    private String field_size;

    public FieldListRecyclerModel(String unique_field_id, String field_r_id, String member_name,
                                  String phone_number, String village_name, String min_lat,
                                  String max_lat, String min_lng, String max_lng,
                                  String field_size) {
        this.unique_field_id = unique_field_id;
        this.field_r_id = field_r_id;
        this.member_name = member_name;
        this.phone_number = phone_number;
        this.village_name = village_name;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_lng = min_lng;
        this.max_lng = max_lng;
        this.field_size = field_size;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getField_r_id() {
        return field_r_id;
    }

    public void setField_r_id(String field_r_id) {
        this.field_r_id = field_r_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getMin_lat() {
        return min_lat;
    }

    public void setMin_lat(String min_lat) {
        this.min_lat = min_lat;
    }

    public String getMax_lat() {
        return max_lat;
    }

    public void setMax_lat(String max_lat) {
        this.max_lat = max_lat;
    }

    public String getMin_lng() {
        return min_lng;
    }

    public void setMin_lng(String min_lng) {
        this.min_lng = min_lng;
    }

    public String getMax_lng() {
        return max_lng;
    }

    public void setMax_lng(String max_lng) {
        this.max_lng = max_lng;
    }

    public String getField_size() {
        return field_size;
    }

    public void setField_size(String field_size) {
        this.field_size = field_size;
    }
}
