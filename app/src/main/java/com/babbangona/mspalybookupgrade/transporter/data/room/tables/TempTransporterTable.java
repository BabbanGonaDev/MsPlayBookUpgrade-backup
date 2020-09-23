package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "temp_transporter_table")
public class TempTransporterTable {

    @NonNull
    @PrimaryKey
    private String temp_transporter_id;
    private String first_name;
    private String last_name;
    private String date_updated;
    private Integer sync_flag;

    public TempTransporterTable(@NonNull String temp_transporter_id, String first_name, String last_name, String date_updated, Integer sync_flag) {
        this.temp_transporter_id = temp_transporter_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_updated = date_updated;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getTemp_transporter_id() {
        return temp_transporter_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }
}
