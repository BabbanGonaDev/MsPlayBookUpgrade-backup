package com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler;

public class ViewScheduleRecyclerModel {


    private String member_name;

    private String phone_number;

    private String location;

    private String field_id;

    private String threshing_date;

    private String field_size;

    private String thresher_id;



    public ViewScheduleRecyclerModel(String member_name, String phone_number, String location,
                                     String field_id, String threshing_date, String field_size,
                                     String thresher_id) {
        this.member_name = member_name;
        this.phone_number = phone_number;
        this.location = location;
        this.field_id  = field_id;

        this.threshing_date = threshing_date;
        this.field_size  = field_size;
        this.thresher_id  = thresher_id;

    }

    public String getMember_name() {
        return member_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getLocation() {
        return location;
    }

    public String getField_id() {
        return field_id;
    }

    public String getThreshing_date() {
        return threshing_date;
    }

    public String getField_size() {
        return field_size;
    }

    public String getThresher_id() {
        return thresher_id;
    }
}
