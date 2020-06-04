package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID_LOGS,DatabaseStringConstants.COL_STAFF_ID_LOGS,
        DatabaseStringConstants.COL_ACTIVITY_TYPE_LOGS,DatabaseStringConstants.COL_DATE_LOGGED},
        tableName = DatabaseStringConstants.LOGS_TABLE)
public class Logs {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_LOGS)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_LOGS)
    @NonNull
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_ACTIVITY_TYPE_LOGS)
    @NonNull
    private String activity_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_DATE_LOGGED)
    @NonNull
    private String date_logged;

    @ColumnInfo(name = DatabaseStringConstants.COL_ROLE)
    private String role;

    @ColumnInfo(name = DatabaseStringConstants.COL_LATITUDE)
    private String latitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_LONGITUDE)
    private String longitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_IMEI)
    private String imei;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_LOGS)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_LOGS)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_CROP_TYPE_LOGS)
    private String crop_type;

    public Logs(@NonNull String unique_field_id, @NonNull String staff_id, @NonNull String activity_type,
                @NonNull String date_logged, String role, String latitude, String longitude,
                String imei, String sync_flag, String ik_number, String crop_type) {
        this.unique_field_id = unique_field_id;
        this.staff_id = staff_id;
        this.activity_type = activity_type;
        this.date_logged = date_logged;
        this.role = role;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imei = imei;
        this.sync_flag = sync_flag;
        this.ik_number = ik_number;
        this.crop_type = crop_type;
    }

    @NonNull
    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(@NonNull String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    @NonNull
    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(@NonNull String staff_id) {
        this.staff_id = staff_id;
    }

    @NonNull
    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(@NonNull String activity_type) {
        this.activity_type = activity_type;
    }

    @NonNull
    public String getDate_logged() {
        return date_logged;
    }

    public void setDate_logged(@NonNull String date_logged) {
        this.date_logged = date_logged;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(String sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }
}
