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
    private String staff_id;
    private String imei;
    private String app_version;
    private String reg_date;
    private Integer sync_flag;

    public TempTransporterTable(@NonNull String temp_transporter_id, String first_name, String last_name, String staff_id, String imei, String app_version, String reg_date, Integer sync_flag) {
        this.temp_transporter_id = temp_transporter_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.staff_id = staff_id;
        this.imei = imei;
        this.app_version = app_version;
        this.reg_date = reg_date;
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

    public String getStaff_id() {
        return staff_id;
    }

    public String getImei() {
        return imei;
    }

    public String getApp_version() {
        return app_version;
    }

    public String getReg_date() {
        return reg_date;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }

    /**
     * Tuple for sync_up response
     */

    public static class Response {
        private String temp_transporter_id;
        private Integer sync_flag;

        public Response(String temp_transporter_id, Integer sync_flag) {
            this.temp_transporter_id = temp_transporter_id;
            this.sync_flag = sync_flag;
        }

        public String getTemp_transporter_id() {
            return temp_transporter_id;
        }

        public Integer getSync_flag() {
            return sync_flag;
        }
    }
}
