package com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler;

public class ActivityListRecyclerModel {

    private String activity_id;

    private String activity_name;

    private String total_field_count;

    private String logged_field_count;

    private String activity_statistics;

    private String activity_destination;

    private String activity_priority;

    public ActivityListRecyclerModel(String activity_id, String activity_name, String total_field_count,
                                     String logged_field_count, String activity_statistics,
                                     String activity_destination, String activity_priority) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.total_field_count = total_field_count;
        this.logged_field_count = logged_field_count;
        this.activity_statistics = activity_statistics;
        this.activity_destination = activity_destination;
        this.activity_priority = activity_priority;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getTotal_field_count() {
        return total_field_count;
    }

    public void setTotal_field_count(String total_field_count) {
        this.total_field_count = total_field_count;
    }

    public String getLogged_field_count() {
        return logged_field_count;
    }

    public void setLogged_field_count(String logged_field_count) {
        this.logged_field_count = logged_field_count;
    }

    public String getActivity_statistics() {
        return activity_statistics;
    }

    public void setActivity_statistics(String activity_statistics) {
        this.activity_statistics = activity_statistics;
    }

    public String getActivity_destination() {
        return activity_destination;
    }

    public void setActivity_destination(String activity_destination) {
        this.activity_destination = activity_destination;
    }

    public String getActivity_priority() {
        return activity_priority;
    }

    public void setActivity_priority(String activity_priority) {
        this.activity_priority = activity_priority;
    }
}
