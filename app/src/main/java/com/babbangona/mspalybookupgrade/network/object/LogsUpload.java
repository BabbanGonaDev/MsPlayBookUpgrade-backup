package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.SerializedName;

public class LogsUpload {

    @SerializedName("unique_field_id")
    private String unique_field_id;

    @SerializedName("staff_id")
    private String staff_id;

    @SerializedName("activity_type")
    private String activity_type;

    @SerializedName("date_logged")
    private String date_logged;

    public LogsUpload(String unique_field_id, String staff_id, String activity_type, String date_logged) {
        this.unique_field_id = unique_field_id;
        this.staff_id = staff_id;
        this.activity_type = activity_type;
        this.date_logged = date_logged;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getDate_logged() {
        return date_logged;
    }

    public void setDate_logged(String date_logged) {
        this.date_logged = date_logged;
    }
}
