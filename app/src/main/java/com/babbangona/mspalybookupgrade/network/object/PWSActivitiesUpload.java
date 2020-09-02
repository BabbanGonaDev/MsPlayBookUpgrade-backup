package com.babbangona.mspalybookupgrade.network.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PWSActivitiesUpload {

    @SerializedName("pws_id")
    @Expose
    private String pws_id;

    @SerializedName("last_synced")
    @Expose
    private String last_synced;

    public PWSActivitiesUpload(String pws_id, String last_synced) {
        this.pws_id = pws_id;
        this.last_synced = last_synced;
    }

    public String getPws_id() {
        return pws_id;
    }

    public void setPws_id(String pws_id) {
        this.pws_id = pws_id;
    }

    public String getLast_synced() {
        return last_synced;
    }

    public void setLast_synced(String last_synced) {
        this.last_synced = last_synced;
    }
}
