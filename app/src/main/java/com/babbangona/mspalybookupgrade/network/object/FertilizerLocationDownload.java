package com.babbangona.mspalybookupgrade.network.object;

import com.babbangona.mspalybookupgrade.data.db.entities.FertilizerLocationsTable;
import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FertilizerLocationDownload {

    @SerializedName("download_list")
    private List<FertilizerLocationsTable> download_list;

    @SerializedName("last_synced_time")
    private String last_synced_time;

    public List<FertilizerLocationsTable> getDownload_list() {
        return download_list;
    }

    public void setDownload_list(List<FertilizerLocationsTable> download_list) {
        this.download_list = download_list;
    }

    public String getLast_sync_time() {
        return last_synced_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_synced_time = last_sync_time;
    }
}
