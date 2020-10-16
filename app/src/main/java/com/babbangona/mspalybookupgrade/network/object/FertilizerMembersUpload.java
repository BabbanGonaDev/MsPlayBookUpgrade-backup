package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.SerializedName;

public class FertilizerMembersUpload {

    @SerializedName("unique_member_id")
    private String unique_member_id;

    @SerializedName("last_synced")
    private String last_synced;

    public FertilizerMembersUpload(String unique_member_id, String last_synced) {
        this.unique_member_id = unique_member_id;
        this.last_synced = last_synced;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getLast_synced() {
        return last_synced;
    }

    public void setLast_synced(String last_synced) {
        this.last_synced = last_synced;
    }
}
