package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_PWS_ID},
        tableName = DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE)
public class PCPWSActivitiesFlag {

    @ColumnInfo(name = DatabaseStringConstants.COL_PWS_ID)
    @NonNull
    private String pws_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_PWS)
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_PWS)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_CATEGORY_PWS)
    private String category;

    @ColumnInfo(name = DatabaseStringConstants.COL_PWS_AREA)
    private String pws_area;

    @ColumnInfo(name = DatabaseStringConstants.COL_PWS_LAT_LNG)
    private String lat_long;

    @ColumnInfo(name = DatabaseStringConstants.COL_MIN_LAT_PWS)
    private String min_lat;

    @ColumnInfo(name = DatabaseStringConstants.COL_MAX_LAT_PWS)
    private String max_lat;

    @ColumnInfo(name = DatabaseStringConstants.COL_MIN_LONG_PWS)
    private String min_long;

    @ColumnInfo(name = DatabaseStringConstants.COL_MAX_LONG_PWS)
    private String max_long;

    @ColumnInfo(name = DatabaseStringConstants.COL_LATITUDE_PWS)
    private String latitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_LONGITUDE_PWS)
    private String longitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_PWS)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_SOLVE_PWS)
    private String solve;

    @ColumnInfo(name = DatabaseStringConstants.COL_DEACTIVATE_PWS)
    private String deactivate;

    @ColumnInfo(name = DatabaseStringConstants.COL_DATE_LOGGED_PWS)
    private String date_logged;

    @ColumnInfo(name = DatabaseStringConstants.COL_DESCRIPTION)
    private String description;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_PWS)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_LOGGER_NAME)
    private String member_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_IMEI_PWS)
    private String imei;

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_PWS)
    private String unique_member_id;

    public PCPWSActivitiesFlag(@NonNull String pws_id, String unique_field_id, String ik_number,
                               String category, String pws_area, String lat_long, String min_lat,
                               String max_lat, String min_long, String max_long, String latitude,
                               String longitude, String staff_id, String solve, String deactivate,
                               String date_logged, String description, String sync_flag, String member_name,
                               String imei, String unique_member_id) {
        this.pws_id = pws_id;
        this.unique_field_id = unique_field_id;
        this.ik_number = ik_number;
        this.category = category;
        this.pws_area = pws_area;
        this.lat_long = lat_long;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_long = min_long;
        this.max_long = max_long;
        this.latitude = latitude;
        this.longitude = longitude;
        this.staff_id = staff_id;
        this.solve = solve;
        this.deactivate = deactivate;
        this.date_logged = date_logged;
        this.description = description;
        this.sync_flag = sync_flag;
        this.member_name = member_name;
        this.imei = imei;
        this.unique_member_id = unique_member_id;
    }

    @NonNull
    public String getPws_id() {
        return pws_id;
    }

    public void setPws_id(@NonNull String pws_id) {
        this.pws_id = pws_id;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPws_area() {
        return pws_area;
    }

    public void setPws_area(String pws_area) {
        this.pws_area = pws_area;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }

    public String getMin_lat() {
        return min_lat;
    }

    public void setMin_lat(String min_lat) {
        this.min_lat = min_lat;
    }

    public String getMax_lat() {
        return max_lat;
    }

    public void setMax_lat(String max_lat) {
        this.max_lat = max_lat;
    }

    public String getMin_long() {
        return min_long;
    }

    public void setMin_long(String min_long) {
        this.min_long = min_long;
    }

    public String getMax_long() {
        return max_long;
    }

    public void setMax_long(String max_long) {
        this.max_long = max_long;
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

    public String getSolve() {
        return solve;
    }

    public void setSolve(String solve) {
        this.solve = solve;
    }

    public String getDeactivate() {
        return deactivate;
    }

    public void setDeactivate(String deactivate) {
        this.deactivate = deactivate;
    }

    public String getDate_logged() {
        return date_logged;
    }

    public void setDate_logged(String date_logged) {
        this.date_logged = date_logged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(String sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }
}
