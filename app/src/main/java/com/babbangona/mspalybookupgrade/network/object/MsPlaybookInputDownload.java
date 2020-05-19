package com.babbangona.mspalybookupgrade.network.object;

import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MsPlaybookInputDownload {

    @SerializedName("members")
    private List<Members> members;

    @SerializedName("fields")
    private List<Fields> fields;

    @SerializedName("last_sync_time")
    private String last_sync_time;

    public List<Members> getMembers() {
        return members;
    }

    public void setMembers(List<Members> members) {
        this.members = members;
    }

    public List<Fields> getFields() {
        return fields;
    }

    public void setFields(List<Fields> fields) {
        this.fields = fields;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
