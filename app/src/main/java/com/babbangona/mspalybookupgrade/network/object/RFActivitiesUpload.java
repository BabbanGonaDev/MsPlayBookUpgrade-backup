package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.SerializedName;

public class RFActivitiesUpload {

    @SerializedName("unique_field_id")
    private String unique_field_id;

    @SerializedName("hg_type")
    private String hg_type;

    @SerializedName("last_synced")
    private String last_synced;

    public RFActivitiesUpload(String unique_field_id, String hg_type, String last_synced) {
        this.unique_field_id = unique_field_id;
        this.hg_type = hg_type;
        this.last_synced = last_synced;
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

    public String getLast_synced() {
        return last_synced;
    }

    public void setLast_synced(String last_synced) {
        this.last_synced = last_synced;
    }
}
