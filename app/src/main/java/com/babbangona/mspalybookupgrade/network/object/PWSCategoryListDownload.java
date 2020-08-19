package com.babbangona.mspalybookupgrade.network.object;

import com.babbangona.mspalybookupgrade.data.db.entities.PWSCategoryList;
import com.babbangona.mspalybookupgrade.data.db.entities.RFList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PWSCategoryListDownload {

    @SerializedName("download_list")
    private List<PWSCategoryList> download_list;

    @SerializedName("last_sync_time")
    private String last_sync_time;

    public List<PWSCategoryList> getDownload_list() {
        return download_list;
    }

    public void setDownload_list(List<PWSCategoryList> download_list) {
        this.download_list = download_list;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
