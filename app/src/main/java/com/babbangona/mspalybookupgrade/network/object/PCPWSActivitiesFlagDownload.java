package com.babbangona.mspalybookupgrade.network.object;

import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PCPWSActivitiesFlag;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PCPWSActivitiesFlagDownload {

    @SerializedName("download_list")
    private List<PCPWSActivitiesFlag> download_list;

    @SerializedName("last_sync_time")
    private String last_sync_time;

    public List<PCPWSActivitiesFlag> getDownload_list() {
        return download_list;
    }

    public void setDownload_list(List<PCPWSActivitiesFlag> download_list) {
        this.download_list = download_list;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
