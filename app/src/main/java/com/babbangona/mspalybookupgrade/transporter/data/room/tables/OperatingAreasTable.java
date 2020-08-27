package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "operating_areas_table", primaryKeys = {"phone_number", "cc_id"})
public class OperatingAreasTable {

    @NonNull
    private String phone_number;

    @NonNull
    private String cc_id;
    private String date_updated;
    private Integer sync_flag;

    public OperatingAreasTable(@NonNull String phone_number, @NonNull String cc_id, String date_updated, Integer sync_flag) {
        this.phone_number = phone_number;
        this.cc_id = cc_id;
        this.date_updated = date_updated;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getPhone_number() {
        return phone_number;
    }

    @NonNull
    public String getCc_id() {
        return cc_id;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }

    /**
     * Tuple for sync_up response.
     */

    public static class Response {
        public String phone_number;
        public String cc_id;
        public Integer sync_flag;
    }
}
