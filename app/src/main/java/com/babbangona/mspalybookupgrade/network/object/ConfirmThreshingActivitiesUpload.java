package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.SerializedName;

public class ConfirmThreshingActivitiesUpload {

    @SerializedName("unique_field_id")
    private String unique_field_id;

    @SerializedName("last_synced")
    private String last_synced;

    public ConfirmThreshingActivitiesUpload(String unique_field_id, String last_synced) {
        this.unique_field_id = unique_field_id;
        this.last_synced = last_synced;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getLast_synced() {
        return last_synced;
    }

    public void setLast_synced(String last_synced) {
        this.last_synced = last_synced;
    }
}
