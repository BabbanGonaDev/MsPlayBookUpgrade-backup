package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID_CONFIRM},
        tableName = DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE)
public class ConfirmThreshingActivitiesFlag {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_CONFIRM)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_CONFIRM_THRESHING_FLAG)
    private String confirm_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_CONFIRM_DATE)
    private String confirm_date;

    @ColumnInfo(name = DatabaseStringConstants.COL_IMEI_CONFIRM)
    private String imei;

    @ColumnInfo(name = DatabaseStringConstants.COL_APP_VERSION_CONFIRM)
    private String app_version;

    @ColumnInfo(name = DatabaseStringConstants.COL_LATITUDE_CONFIRM)
    private String latitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_LONGITUDE_CONFIRM)
    private String longitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_CONFIRM)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_USED_CODE)
    private String used_code;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_CONFIRM)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_CONFIRM)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_THRESHER)
    private String thresher;

    @ColumnInfo(name = DatabaseStringConstants.COL_THRESHER_ID)
    private String thresher_id;


    public ConfirmThreshingActivitiesFlag(@NonNull String unique_field_id, String confirm_flag,
                                          String confirm_date, String imei, String app_version,
                                          String latitude, String longitude, String staff_id,
                                          String used_code, String ik_number, String sync_flag, String thresher_id) {
        this.unique_field_id = unique_field_id;
        this.confirm_flag = confirm_flag;
        this.confirm_date = confirm_date;
        this.imei = imei;
        this.app_version = app_version;
        this.latitude = latitude;
        this.longitude = longitude;
        this.staff_id = staff_id;
        this.used_code = used_code;
        this.ik_number = ik_number;
        this.sync_flag = sync_flag;
        this.thresher = thresher;
        this.thresher_id = thresher_id;
    }


    @NonNull
    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(@NonNull String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getConfirm_flag() {
        return confirm_flag;
    }

    public void setConfirm_flag(String confirm_flag) {
        this.confirm_flag = confirm_flag;
    }

    public String getConfirm_date() {
        return confirm_date;
    }

    public void setConfirm_date(String confirm_date) {
        this.confirm_date = confirm_date;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getUsed_code() {
        return used_code;
    }

    public void setUsed_code(String used_code) {
        this.used_code = used_code;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(String sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getThresher() {
        return thresher;
    }

    public void setThresher(String thresher) {
        this.thresher = thresher;
    }

    public String getThresher_id() {
        return thresher_id;
    }

    public void setThresher_id(String thresher_id) {
        this.thresher_id = thresher_id;
    }
}
