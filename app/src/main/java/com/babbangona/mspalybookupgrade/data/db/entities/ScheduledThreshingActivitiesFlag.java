package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID},
        tableName = DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE)
public class ScheduledThreshingActivitiesFlag {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_SCHEDULE)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_THRESHER_SCHEDULE)
    private String thresher;

    @ColumnInfo(name = DatabaseStringConstants.COL_FACE_SCAN_FLAG_SCHEDULE)
    private String face_scan_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_TEMPLATE_SCHEDULE)
    private String template;

    @ColumnInfo(name = DatabaseStringConstants.COL_SCHEDULE_DATE)
    private String schedule_date;

    @ColumnInfo(name = DatabaseStringConstants.COL_COLLECTION_CENTER)
    private String collection_center;

    @ColumnInfo(name = DatabaseStringConstants.COL_PHONE_NUMBER_SCHEDULE)
    private String phone_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_IMEI_SCHEDULE)
    private String imei;

    @ColumnInfo(name = DatabaseStringConstants.COL_APP_VERSION_SCHEDULE)
    private String app_version;

    @ColumnInfo(name = DatabaseStringConstants.COL_LATITUDE_SCHEDULE)
    private String latitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_LONGITUDE_SCHEDULE)
    private String longitude;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_SCHEDULE)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_DATE_LOGGED_SCHEDULE)
    private String date_logged;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_SCHEDULE)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_RESCHEDULE_REASON)
    private String reschedule_reason;

    public ScheduledThreshingActivitiesFlag(@NonNull String unique_field_id, String thresher, String face_scan_flag,
                                            String template, String schedule_date, String collection_center,
                                            String phone_number, String imei, String app_version,
                                            String latitude, String longitude, String staff_id, String date_logged,
                                            String sync_flag, String reschedule_reason) {
        this.unique_field_id = unique_field_id;
        this.thresher = thresher;
        this.face_scan_flag = face_scan_flag;
        this.template = template;
        this.schedule_date = schedule_date;
        this.collection_center = collection_center;
        this.phone_number = phone_number;
        this.imei = imei;
        this.app_version = app_version;
        this.latitude = latitude;
        this.longitude = longitude;
        this.staff_id = staff_id;
        this.date_logged = date_logged;
        this.sync_flag = sync_flag;
        this.reschedule_reason = reschedule_reason;
    }

    @NonNull
    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(@NonNull String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getThresher() {
        return thresher;
    }

    public void setThresher(String thresher) {
        this.thresher = thresher;
    }

    public String getFace_scan_flag() {
        return face_scan_flag;
    }

    public void setFace_scan_flag(String face_scan_flag) {
        this.face_scan_flag = face_scan_flag;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getCollection_center() {
        return collection_center;
    }

    public void setCollection_center(String collection_center) {
        this.collection_center = collection_center;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getDate_logged() {
        return date_logged;
    }

    public void setDate_logged(String date_logged) {
        this.date_logged = date_logged;
    }

    public String getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(String sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getReschedule_reason() {
        return reschedule_reason;
    }

    public void setReschedule_reason(String reschedule_reason) {
        this.reschedule_reason = reschedule_reason;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public static class ScheduleCalculationModel{

        private String unique_field_id;
        private String field_size;

        public ScheduleCalculationModel() {
            unique_field_id = "";
            field_size = "";
        }

        public ScheduleCalculationModel(String unique_field_id, String field_size) {
            this.unique_field_id = unique_field_id;
            this.field_size = field_size;
        }

        public String getUnique_field_id() {
            return unique_field_id;
        }

        public void setUnique_field_id(String unique_field_id) {
            this.unique_field_id = unique_field_id;
        }

        public String getField_size() {
            return field_size;
        }

        public void setField_size(String field_size) {
            this.field_size = field_size;
        }
    }
}
