package com.babbangona.mspalybookupgrade.tpo.data.models;

public class DeliveryDownload {

    private String unique_member_id;
    private String ik_number;
    private String cc_id;
    private String qty_delivered;
    private String imei;
    private String app_version;
    private String date_delivered;
    private Integer sync_flag;
    private String last_sync_time;

    public DeliveryDownload(String unique_member_id, String ik_number, String cc_id, String qty_delivered, String imei, String app_version, String date_delivered, Integer sync_flag, String last_sync_time) {
        this.unique_member_id = unique_member_id;
        this.ik_number = ik_number;
        this.cc_id = cc_id;
        this.qty_delivered = qty_delivered;
        this.imei = imei;
        this.app_version = app_version;
        this.date_delivered = date_delivered;
        this.sync_flag = sync_flag;
        this.last_sync_time = last_sync_time;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getQty_delivered() {
        return qty_delivered;
    }

    public void setQty_delivered(String qty_delivered) {
        this.qty_delivered = qty_delivered;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDate_delivered() {
        return date_delivered;
    }

    public void setDate_delivered(String date_delivered) {
        this.date_delivered = date_delivered;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(Integer sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getLast_sync_time() {
        return last_sync_time;
    }

    public void setLast_sync_time(String last_sync_time) {
        this.last_sync_time = last_sync_time;
    }
}
