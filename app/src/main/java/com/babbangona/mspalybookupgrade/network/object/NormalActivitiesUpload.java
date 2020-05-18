package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.SerializedName;

public class NormalActivitiesUpload {

    @SerializedName("unique_field_id")
    private String unique_field_id;

    public NormalActivitiesUpload(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }
}
