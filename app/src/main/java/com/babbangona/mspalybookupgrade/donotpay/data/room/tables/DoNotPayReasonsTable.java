package com.babbangona.mspalybookupgrade.donotpay.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "donotpay_reasons_table")
public class DoNotPayReasonsTable {

    @PrimaryKey
    @NonNull
    private String id;
    private String reason;
    private Integer active_flag;
    private String date_updated;

    public DoNotPayReasonsTable(@NonNull String id, String reason, Integer active_flag, String date_updated) {
        this.id = id;
        this.reason = reason;
        this.active_flag = active_flag;
        this.date_updated = date_updated;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Integer getActive_flag() {
        return active_flag;
    }

    public String getDate_updated() {
        return date_updated;
    }
}
