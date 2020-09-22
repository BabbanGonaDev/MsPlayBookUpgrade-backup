package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;


@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_L,},
        tableName = DatabaseStringConstants.THRESHING_LOCATION)
public class ThreshingLocation {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_L)
    @NonNull
    private String unique_member_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_LOCATION_ID_L)
    private String location_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_VILLAGE_NAME_L)
    private String village_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_L)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_LATITUDE_L)
    private String latitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_LONGITUDE_L)
    private String longitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_L)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_STATE_L)
    private String state;

    @ColumnInfo(name = DatabaseStringConstants.COL_LGA_L)
    private String lga;

    @ColumnInfo(name = DatabaseStringConstants.COL_WARD_L)
    private String ward;

    @ColumnInfo(name = DatabaseStringConstants.COL_APP_VERSION)
    private String app_version;

    public ThreshingLocation(@NonNull String unique_member_id, String location_id, String village_name,
                             String staff_id, String latitude, String longitude, String sync_flag,
                             String state, String lga, String ward, String app_version) {
        this.unique_member_id = unique_member_id;
        this.location_id = location_id;
        this.village_name = village_name;
        this.staff_id = staff_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sync_flag = sync_flag;
        this.state = state;
        this.lga = lga;
        this.ward = ward;
        this.app_version = app_version;
    }

    @NonNull
    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(@NonNull String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
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

    public String getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(String sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }
}
