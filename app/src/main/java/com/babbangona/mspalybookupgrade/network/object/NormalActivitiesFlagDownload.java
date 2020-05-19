package com.babbangona.mspalybookupgrade.network.object;

import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NormalActivitiesFlagDownload {

    @SerializedName("download_list")
    private List<NormalActivitiesFlag> download_list;

    @SerializedName("last_sync_time")
    private String last_sync_time;

    public List<NormalActivitiesFlag> getDownload_list() {
        return download_list;
    }

    public void setDownload_list(List<NormalActivitiesFlag> download_list) {
        this.download_list = download_list;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
