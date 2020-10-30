package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;


@Entity(primaryKeys = {DatabaseStringConstants.VARIABLE_ID},
        tableName = DatabaseStringConstants.APP_VARIABLES)

public class AppVariables {

    @ColumnInfo(name = DatabaseStringConstants.VARIABLE_ID)
    @NonNull
    private String variable_id;

    @ColumnInfo(name = DatabaseStringConstants.EDIT_HARVEST_LOCATION_FLAG)
    private String edit_harvest_location_flag;

    @ColumnInfo(name = DatabaseStringConstants.MINIMUM_LOG_DATE_FLAG)
    private String minimum_log_date;

    @ColumnInfo(name = DatabaseStringConstants.MAXIMUM_LOG_DATE_FLAG)
    private String maximum_log_date;

    @ColumnInfo(name = DatabaseStringConstants.FIELDS_TRAVEL_TIME)
    private String fields_travel_time;

    @ColumnInfo(name = DatabaseStringConstants.AVERAGE_TRANSITION_TIME)
    private String average_transition_time;

    @ColumnInfo(name = DatabaseStringConstants.TIME_PER_HA)
    private String time_per_ha;

    @ColumnInfo(name = DatabaseStringConstants.MAXIMUM_SCHEDULE_DATE)
    private String maximum_schedule_date;

    @ColumnInfo(name = DatabaseStringConstants.LUXAND_FLAG)
    private String luxand_flag;

    @ColumnInfo(name = DatabaseStringConstants.FERTILIZER_LUXAND_FLAG)
    private String fertilizer_luxand_flag;

    @ColumnInfo(name = DatabaseStringConstants.ISSUES_LIST)
    private String issues_list;

    @ColumnInfo(name = DatabaseStringConstants.LOCATION_TRACKER_FLAG)
    private String bgt_location_tracker_flag;

    @ColumnInfo(name = DatabaseStringConstants.LOCATION_TRACKER_DAYS)
    private String bgt_location_tracker_days;

    @ColumnInfo(name = DatabaseStringConstants.LOCATION_TRACKER_HOURS)
    private String bgt_location_tracker_hours;

    public AppVariables(@NonNull String variable_id, String edit_harvest_location_flag,
                        String minimum_log_date, String maximum_log_date, String fields_travel_time,
                        String average_transition_time, String time_per_ha, String maximum_schedule_date,
                        String luxand_flag, String fertilizer_luxand_flag, String issues_list,
                        String bgt_location_tracker_flag, String bgt_location_tracker_days, String bgt_location_tracker_hours) {

        this.variable_id = variable_id;
        this.edit_harvest_location_flag = edit_harvest_location_flag;
        this.minimum_log_date = minimum_log_date;
        this.maximum_log_date = maximum_log_date;
        this.fields_travel_time = fields_travel_time;
        this.average_transition_time = average_transition_time;
        this.time_per_ha = time_per_ha;
        this.maximum_schedule_date = maximum_schedule_date;
        this.luxand_flag = luxand_flag;
        this.fertilizer_luxand_flag = fertilizer_luxand_flag;
        this.issues_list = issues_list;
        this.bgt_location_tracker_flag = bgt_location_tracker_flag;
        this.bgt_location_tracker_days = bgt_location_tracker_days;
        this.bgt_location_tracker_hours = bgt_location_tracker_hours;
    }

    public String getFields_travel_time() {
        return fields_travel_time;
    }

    public void setFields_travel_time(String fields_travel_time) {
        this.fields_travel_time = fields_travel_time;
    }

    public String getAverage_transition_time() {
        return average_transition_time;
    }

    public void setAverage_transition_time(String average_transition_time) {
        this.average_transition_time = average_transition_time;
    }

    public String getTime_per_ha() {
        return time_per_ha;
    }

    public void setTime_per_ha(String time_per_ha) {
        this.time_per_ha = time_per_ha;
    }

    @NonNull
    public String getVariable_id() {
        return variable_id;
    }

    public void setVariable_id(@NonNull String variable_id) {
        this.variable_id = variable_id;
    }

    public String getEdit_harvest_location_flag() {
        return edit_harvest_location_flag;
    }

    public void setEdit_harvest_location_flag(String edit_harvest_location_flag) {
        this.edit_harvest_location_flag = edit_harvest_location_flag;
    }

    public String getMinimum_log_date() {
        return minimum_log_date;
    }

    public void setMinimum_log_date(String minimum_log_date) {
        this.minimum_log_date = minimum_log_date;
    }

    public String getMaximum_log_date() {
        return maximum_log_date;
    }

    public void setMaximum_log_date(String maximum_log_date) {
        this.maximum_log_date = maximum_log_date;
    }

    public String getMaximum_schedule_date() {
        return maximum_schedule_date;
    }

    public void setMaximum_schedule_date(String maximum_schedule_date) {
        this.maximum_schedule_date = maximum_schedule_date;
    }

    public String getLuxand_flag() {
        return luxand_flag;
    }

    public void setLuxand_flag(String luxand_flag) {
        this.luxand_flag = luxand_flag;
    }

    public String getFertilizer_luxand_flag() {
        return fertilizer_luxand_flag;
    }

    public void setFertilizer_luxand_flag(String fertilizer_luxand_flag) {
        this.fertilizer_luxand_flag = fertilizer_luxand_flag;
    }

    public String getIssues_list() {
        return issues_list;
    }

    public void setIssues_list(String issues_list) {
        this.issues_list = issues_list;
    }

    public String getBgt_location_tracker_flag() {
        return bgt_location_tracker_flag;
    }

    public void setBgt_location_tracker_flag(String bgt_location_tracker_flag) {
        this.bgt_location_tracker_flag = bgt_location_tracker_flag;
    }

    public String getBgt_location_tracker_days() {
        return bgt_location_tracker_days;
    }

    public void setBgt_location_tracker_days(String bgt_location_tracker_days) {
        this.bgt_location_tracker_days = bgt_location_tracker_days;
    }

    public String getBgt_location_tracker_hours() {
        return bgt_location_tracker_hours;
    }

    public void setBgt_location_tracker_hours(String bgt_location_tracker_hours) {
        this.bgt_location_tracker_hours = bgt_location_tracker_hours;
    }
}
