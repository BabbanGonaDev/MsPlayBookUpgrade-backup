package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.SerializedName;

public class HGActivitiesUpload {

    @SerializedName("unique_field_id")
    private String unique_field_id;

    @SerializedName("hg_type")
    private String hg_type;

    public HGActivitiesUpload(String unique_field_id, String hg_type) {
        this.unique_field_id = unique_field_id;
        this.hg_type = hg_type;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getHg_type() {
        return hg_type;
    }

    public void setHg_type(String hg_type) {
        this.hg_type = hg_type;
    }
}
