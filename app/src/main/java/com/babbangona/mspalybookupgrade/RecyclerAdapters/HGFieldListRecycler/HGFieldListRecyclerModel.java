package com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler;

public class HGFieldListRecyclerModel {

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

    private String ik_number;

    public HGFieldListRecyclerModel(String unique_field_id, String field_r_id, String member_name,
                                    String phone_number, String village_name, String min_lat,
                                    String max_lat, String min_lng, String max_lng, String field_size,
                                    String ik_number) {
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
        this.ik_number = ik_number;
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

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
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

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public static class HGListModel{
        private String hg_type;

        private String hg_status;

        public HGListModel(String hg_type, String hg_status) {
            this.hg_type = hg_type;
            this.hg_status = hg_status;
        }

        public String getHg_type() {
            return hg_type;
        }

        public void setHg_type(String hg_type) {
            this.hg_type = hg_type;
        }

        public String getHg_status() {
            return hg_status;
        }

        public void setHg_status(String hg_status) {
            this.hg_status = hg_status;
        }
    }
}
