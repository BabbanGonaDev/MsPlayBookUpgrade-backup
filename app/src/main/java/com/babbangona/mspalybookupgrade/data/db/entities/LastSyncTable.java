package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;


@Entity(primaryKeys = {DatabaseStringConstants.COL_STAFF_ID},
        tableName = DatabaseStringConstants.LAST_SYNC_TABLE)

public class LastSyncTable {

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID)
    @NonNull
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_ACTIVITY_LIST_TABLE)
    private String last_sync_activity_list;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_NORMAL_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_normal_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_NORMAL_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_normal_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_FIELDS_TABLE)
    private String last_sync_fields;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_STAFF_TABLE)
    private String last_sync_staff;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_MEMBERS_TABLE)
    private String last_sync_members;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_HG_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_hg_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_HG_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_hg_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_HG_LIST_TABLE)
    private String last_sync_hg_list;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_LOGS_TABLE)
    private String last_sync_up_logs;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_LOGS_TABLE)
    private String last_sync_down_logs;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_CATEGORY_TABLE)
    private String last_sync_category;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_HARVEST_LOCATION)
    private String last_sync_harvest_location;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_RF_LIST_TABLE)
    private String last_sync_rf_list;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_RF_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_rf_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_RF_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_rf_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_PWS_CATEGORY_LIST_TABLE)
    private String last_sync_pws_category_list;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_PWS_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_pws_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_PWS_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_pws_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_PC_PWS_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_pc_pws_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_PC_PWS_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_pc_pws_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_PWS_ACTIVITIES_CONTROLLER_TABLE)
    private String last_sync_pws_activities_controller;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_SCHEDULED_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_scheduled_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_SCHEDULED_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_scheduled_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_CONFIRM_ACTIVITY_FLAGS_TABLE)
    private String last_sync_up_confirm_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_CONFIRM_ACTIVITY_FLAGS_TABLE)
    private String last_sync_down_confirm_activities_flag;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_BGT_COACHES_TABLE)
    private String last_sync_bgt_coaches;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_UP_FERTILIZER_MEMBERS)
    private String last_sync_up_fertilizer_members;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_DOWN_FERTILIZER_MEMBERS)
    private String last_sync_down_fertilizer_members;

    @ColumnInfo(name = DatabaseStringConstants.LAST_SYNC_FERTILIZER_LOCATION)
    private String last_sync_fertilizer_location;

    public LastSyncTable() {
    }

    public LastSyncTable(@NonNull String staff_id, String last_sync_activity_list,
                         String last_sync_up_normal_activities_flag, String last_sync_down_normal_activities_flag,
                         String last_sync_fields, String last_sync_staff, String last_sync_members,
                         String last_sync_up_hg_activities_flag, String last_sync_down_hg_activities_flag,
                         String last_sync_hg_list, String last_sync_up_logs, String last_sync_down_logs,
                         String last_sync_category, String last_sync_harvest_location, String last_sync_rf_list,
                         String last_sync_up_rf_activities_flag, String last_sync_down_rf_activities_flag,
                         String last_sync_pws_category_list, String last_sync_up_pws_activities_flag,
                         String last_sync_down_pws_activities_flag, String last_sync_up_pc_pws_activities_flag,
                         String last_sync_down_pc_pws_activities_flag, String last_sync_pws_activities_controller,
                         String last_sync_up_scheduled_activities_flag, String last_sync_down_scheduled_activities_flag,
                         String last_sync_up_confirm_activities_flag, String last_sync_down_confirm_activities_flag,
                         String last_sync_bgt_coaches, String last_sync_up_fertilizer_members, String last_sync_down_fertilizer_members,
                         String last_sync_fertilizer_location) {
        this.staff_id = staff_id;
        this.last_sync_activity_list = last_sync_activity_list;
        this.last_sync_up_normal_activities_flag = last_sync_up_normal_activities_flag;
        this.last_sync_down_normal_activities_flag = last_sync_down_normal_activities_flag;
        this.last_sync_fields = last_sync_fields;
        this.last_sync_staff = last_sync_staff;
        this.last_sync_members = last_sync_members;
        this.last_sync_up_hg_activities_flag = last_sync_up_hg_activities_flag;
        this.last_sync_down_hg_activities_flag = last_sync_down_hg_activities_flag;
        this.last_sync_hg_list = last_sync_hg_list;
        this.last_sync_up_logs = last_sync_up_logs;
        this.last_sync_down_logs = last_sync_down_logs;
        this.last_sync_category = last_sync_category;
        this.last_sync_harvest_location = last_sync_harvest_location;
        this.last_sync_rf_list = last_sync_rf_list;
        this.last_sync_up_rf_activities_flag = last_sync_up_rf_activities_flag;
        this.last_sync_down_rf_activities_flag = last_sync_down_rf_activities_flag;
        this.last_sync_pws_category_list = last_sync_pws_category_list;
        this.last_sync_up_pws_activities_flag = last_sync_up_pws_activities_flag;
        this.last_sync_down_pws_activities_flag = last_sync_down_pws_activities_flag;
        this.last_sync_up_pc_pws_activities_flag = last_sync_up_pc_pws_activities_flag;
        this.last_sync_down_pc_pws_activities_flag = last_sync_down_pc_pws_activities_flag;
        this.last_sync_pws_activities_controller = last_sync_pws_activities_controller;
        this.last_sync_up_scheduled_activities_flag = last_sync_up_scheduled_activities_flag;
        this.last_sync_down_scheduled_activities_flag = last_sync_down_scheduled_activities_flag;
        this.last_sync_up_confirm_activities_flag = last_sync_up_confirm_activities_flag;
        this.last_sync_down_confirm_activities_flag = last_sync_down_confirm_activities_flag;
        this.last_sync_bgt_coaches = last_sync_bgt_coaches;
        this.last_sync_up_fertilizer_members = last_sync_up_fertilizer_members;
        this.last_sync_down_fertilizer_members = last_sync_down_fertilizer_members;
        this.last_sync_fertilizer_location = last_sync_fertilizer_location;
    }

    @NonNull
    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(@NonNull String staff_id) {
        this.staff_id = staff_id;
    }

    public String getLast_sync_activity_list() {
        return last_sync_activity_list;
    }

    public void setLast_sync_activity_list(String last_sync_activity_list) {
        this.last_sync_activity_list = last_sync_activity_list;
    }

    public String getLast_sync_up_normal_activities_flag() {
        return last_sync_up_normal_activities_flag;
    }

    public void setLast_sync_up_normal_activities_flag(String last_sync_up_normal_activities_flag) {
        this.last_sync_up_normal_activities_flag = last_sync_up_normal_activities_flag;
    }

    public String getLast_sync_down_normal_activities_flag() {
        return last_sync_down_normal_activities_flag;
    }

    public void setLast_sync_down_normal_activities_flag(String last_sync_down_normal_activities_flag) {
        this.last_sync_down_normal_activities_flag = last_sync_down_normal_activities_flag;
    }

    public String getLast_sync_fields() {
        return last_sync_fields;
    }

    public void setLast_sync_fields(String last_sync_fields) {
        this.last_sync_fields = last_sync_fields;
    }

    public String getLast_sync_staff() {
        return last_sync_staff;
    }

    public void setLast_sync_staff(String last_sync_staff) {
        this.last_sync_staff = last_sync_staff;
    }

    public String getLast_sync_members() {
        return last_sync_members;
    }

    public void setLast_sync_members(String last_sync_members) {
        this.last_sync_members = last_sync_members;
    }

    public String getLast_sync_up_hg_activities_flag() {
        return last_sync_up_hg_activities_flag;
    }

    public void setLast_sync_up_hg_activities_flag(String last_sync_up_hg_activities_flag) {
        this.last_sync_up_hg_activities_flag = last_sync_up_hg_activities_flag;
    }

    public String getLast_sync_down_hg_activities_flag() {
        return last_sync_down_hg_activities_flag;
    }

    public void setLast_sync_down_hg_activities_flag(String last_sync_down_hg_activities_flag) {
        this.last_sync_down_hg_activities_flag = last_sync_down_hg_activities_flag;
    }

    public String getLast_sync_hg_list() {
        return last_sync_hg_list;
    }

    public void setLast_sync_hg_list(String last_sync_hg_list) {
        this.last_sync_hg_list = last_sync_hg_list;
    }

    public String getLast_sync_up_logs() {
        return last_sync_up_logs;
    }

    public void setLast_sync_up_logs(String last_sync_up_logs) {
        this.last_sync_up_logs = last_sync_up_logs;
    }

    public String getLast_sync_down_logs() {
        return last_sync_down_logs;
    }

    public void setLast_sync_down_logs(String last_sync_down_logs) {
        this.last_sync_down_logs = last_sync_down_logs;
    }

    public String getLast_sync_category() {
        return last_sync_category;
    }

    public void setLast_sync_category(String last_sync_category) {
        this.last_sync_category = last_sync_category;
    }

    public String getLast_sync_harvest_location() {
        return last_sync_harvest_location;
    }

    public void setLast_sync_harvest_location(String last_sync_harvest_location) {
        this.last_sync_harvest_location = last_sync_harvest_location;
    }

    public String getLast_sync_rf_list() {
        return last_sync_rf_list;
    }

    public void setLast_sync_rf_list(String last_sync_rf_list) {
        this.last_sync_rf_list = last_sync_rf_list;
    }

    public String getLast_sync_up_rf_activities_flag() {
        return last_sync_up_rf_activities_flag;
    }

    public void setLast_sync_up_rf_activities_flag(String last_sync_up_rf_activities_flag) {
        this.last_sync_up_rf_activities_flag = last_sync_up_rf_activities_flag;
    }

    public String getLast_sync_down_rf_activities_flag() {
        return last_sync_down_rf_activities_flag;
    }

    public void setLast_sync_down_rf_activities_flag(String last_sync_down_rf_activities_flag) {
        this.last_sync_down_rf_activities_flag = last_sync_down_rf_activities_flag;
    }

    public String getLast_sync_pws_category_list() {
        return last_sync_pws_category_list;
    }

    public void setLast_sync_pws_category_list(String last_sync_pws_category_list) {
        this.last_sync_pws_category_list = last_sync_pws_category_list;
    }

    public String getLast_sync_up_pws_activities_flag() {
        return last_sync_up_pws_activities_flag;
    }

    public void setLast_sync_up_pws_activities_flag(String last_sync_up_pws_activities_flag) {
        this.last_sync_up_pws_activities_flag = last_sync_up_pws_activities_flag;
    }

    public String getLast_sync_down_pws_activities_flag() {
        return last_sync_down_pws_activities_flag;
    }

    public void setLast_sync_down_pws_activities_flag(String last_sync_down_pws_activities_flag) {
        this.last_sync_down_pws_activities_flag = last_sync_down_pws_activities_flag;
    }

    public String getLast_sync_up_pc_pws_activities_flag() {
        return last_sync_up_pc_pws_activities_flag;
    }

    public void setLast_sync_up_pc_pws_activities_flag(String last_sync_up_pc_pws_activities_flag) {
        this.last_sync_up_pc_pws_activities_flag = last_sync_up_pc_pws_activities_flag;
    }

    public String getLast_sync_down_pc_pws_activities_flag() {
        return last_sync_down_pc_pws_activities_flag;
    }

    public void setLast_sync_down_pc_pws_activities_flag(String last_sync_down_pc_pws_activities_flag) {
        this.last_sync_down_pc_pws_activities_flag = last_sync_down_pc_pws_activities_flag;
    }

    public String getLast_sync_pws_activities_controller() {
        return last_sync_pws_activities_controller;
    }

    public void setLast_sync_pws_activities_controller(String last_sync_pws_activities_controller) {
        this.last_sync_pws_activities_controller = last_sync_pws_activities_controller;
    }

    public String getLast_sync_up_scheduled_activities_flag() {
        return last_sync_up_scheduled_activities_flag;
    }

    public void setLast_sync_up_scheduled_activities_flag(String last_sync_up_scheduled_activities_flag) {
        this.last_sync_up_scheduled_activities_flag = last_sync_up_scheduled_activities_flag;
    }

    public String getLast_sync_down_scheduled_activities_flag() {
        return last_sync_down_scheduled_activities_flag;
    }

    public void setLast_sync_down_scheduled_activities_flag(String last_sync_down_scheduled_activities_flag) {
        this.last_sync_down_scheduled_activities_flag = last_sync_down_scheduled_activities_flag;
    }

    public String getLast_sync_up_confirm_activities_flag() {
        return last_sync_up_confirm_activities_flag;
    }

    public void setLast_sync_up_confirm_activities_flag(String last_sync_up_confirm_activities_flag) {
        this.last_sync_up_confirm_activities_flag = last_sync_up_confirm_activities_flag;
    }

    public String getLast_sync_down_confirm_activities_flag() {
        return last_sync_down_confirm_activities_flag;
    }

    public void setLast_sync_down_confirm_activities_flag(String last_sync_down_confirm_activities_flag) {
        this.last_sync_down_confirm_activities_flag = last_sync_down_confirm_activities_flag;
    }

    public String getLast_sync_bgt_coaches() {
        return last_sync_bgt_coaches;
    }

    public void setLast_sync_bgt_coaches(String last_sync_bgt_coaches) {
        this.last_sync_bgt_coaches = last_sync_bgt_coaches;
    }

    public String getLast_sync_up_fertilizer_members() {
        return last_sync_up_fertilizer_members;
    }

    public void setLast_sync_up_fertilizer_members(String last_sync_up_fertilizer_members) {
        this.last_sync_up_fertilizer_members = last_sync_up_fertilizer_members;
    }

    public String getLast_sync_down_fertilizer_members() {
        return last_sync_down_fertilizer_members;
    }

    public void setLast_sync_down_fertilizer_members(String last_sync_down_fertilizer_members) {
        this.last_sync_down_fertilizer_members = last_sync_down_fertilizer_members;
    }

    public String getLast_sync_fertilizer_location() {
        return last_sync_fertilizer_location;
    }

    public void setLast_sync_fertilizer_location(String last_sync_fertilizer_location) {
        this.last_sync_fertilizer_location = last_sync_fertilizer_location;
    }
}
