package com.babbangona.mspalybookupgrade.donotpay.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "donotpay_table")
public class DoNotPayTable {

    @PrimaryKey
    @NonNull
    private String ik_number;
    private String donotpay_reason;
    private String staff_id;
    private String imei;
    private String app_version;
    private String date_updated;
    private Integer sync_flag;

    public DoNotPayTable(@NonNull String ik_number, String donotpay_reason, String staff_id, String imei, String app_version, String date_updated, Integer sync_flag) {
        this.ik_number = ik_number;
        this.donotpay_reason = donotpay_reason;
        this.staff_id = staff_id;
        this.imei = imei;
        this.app_version = app_version;
        this.date_updated = date_updated;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getIk_number() {
        return ik_number;
    }

    public String getDonotpay_reason() {
        return donotpay_reason;
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

    public String getDate_updated() {
        return date_updated;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }

    /**
     * Tuple for sync_up response
     */
    public static class Response {
        private String ik_number;
        private Integer sync_flag;

        public Response(String ik_number, Integer sync_flag) {
            this.ik_number = ik_number;
            this.sync_flag = sync_flag;
        }

        public String getIk_number() {
            return ik_number;
        }

        public Integer getSync_flag() {
            return sync_flag;
        }
    }
}
