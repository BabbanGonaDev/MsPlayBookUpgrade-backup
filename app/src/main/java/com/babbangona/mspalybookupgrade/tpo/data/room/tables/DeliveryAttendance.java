package com.babbangona.mspalybookupgrade.tpo.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "delivery_attendance_table", primaryKeys = {"unique_member_id", "cc_id", "qty_delivered", "date_delivered"})
public class DeliveryAttendance {

    @NonNull
    private String unique_member_id;
    private String ik_number;
    @NonNull
    private String cc_id;
    @NonNull
    private String qty_delivered;
    private String imei;
    private String app_version;
    @NonNull
    private String date_delivered;
    private Integer sync_flag;

    public DeliveryAttendance(@NonNull String unique_member_id, String ik_number, @NonNull String cc_id, @NonNull String qty_delivered, String imei, String app_version, @NonNull String date_delivered, Integer sync_flag) {
        this.unique_member_id = unique_member_id;
        this.ik_number = ik_number;
        this.cc_id = cc_id;
        this.qty_delivered = qty_delivered;
        this.imei = imei;
        this.app_version = app_version;
        this.date_delivered = date_delivered;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getUnique_member_id() {
        return unique_member_id;
    }

    public String getIk_number() {
        return ik_number;
    }

    @NonNull
    public String getCc_id() {
        return cc_id;
    }

    @NonNull
    public String getQty_delivered() {
        return qty_delivered;
    }

    public String getImei() {
        return imei;
    }

    public String getApp_version() {
        return app_version;
    }

    @NonNull
    public String getDate_delivered() {
        return date_delivered;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }
}
