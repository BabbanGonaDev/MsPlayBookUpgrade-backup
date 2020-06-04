package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID},
        tableName = DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE)
public class NormalActivitiesFlag {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_FERTILIZER_1_STATUS)
    private String fertilizer_1_status;

    @ColumnInfo(name = DatabaseStringConstants.COL_FERTILIZER_1_DATE)
    private String fertilizer_1_date;

    @ColumnInfo(name = DatabaseStringConstants.COL_FERTILIZER_2_STATUS)
    private String fertilizer_2_status;

    @ColumnInfo(name = DatabaseStringConstants.COL_FERTILIZER_2_DATE)
    private String fertilizer_2_date;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_NORMAL_ACTIVITIES)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_CROP_TYPE_NORMAL_ACTIVITIES)
    private String crop_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_CC_HARVEST)
    private String cc_harvest;

    public NormalActivitiesFlag(@NonNull String unique_field_id, String fertilizer_1_status,
                                String fertilizer_1_date, String fertilizer_2_status,
                                String fertilizer_2_date, String staff_id, String sync_flag,
                                String ik_number, String crop_type, String cc_harvest) {
        this.unique_field_id = unique_field_id;
        this.fertilizer_1_status = fertilizer_1_status;
        this.fertilizer_1_date = fertilizer_1_date;
        this.fertilizer_2_status = fertilizer_2_status;
        this.fertilizer_2_date = fertilizer_2_date;
        this.staff_id = staff_id;
        this.sync_flag = sync_flag;
        this.ik_number = ik_number;
        this.crop_type = crop_type;
        this.cc_harvest = cc_harvest;
    }

    @NonNull
    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(@NonNull String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getFertilizer_1_status() {
        return fertilizer_1_status;
    }

    public void setFertilizer_1_status(String fertilizer_1_status) {
        this.fertilizer_1_status = fertilizer_1_status;
    }

    public String getFertilizer_1_date() {
        return fertilizer_1_date;
    }

    public void setFertilizer_1_date(String fertilizer_1_date) {
        this.fertilizer_1_date = fertilizer_1_date;
    }

    public String getFertilizer_2_status() {
        return fertilizer_2_status;
    }

    public void setFertilizer_2_status(String fertilizer_2_status) {
        this.fertilizer_2_status = fertilizer_2_status;
    }

    public String getFertilizer_2_date() {
        return fertilizer_2_date;
    }

    public void setFertilizer_2_date(String fertilizer_2_date) {
        this.fertilizer_2_date = fertilizer_2_date;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
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

    public String getCc_harvest() {
        return cc_harvest;
    }

    public void setCc_harvest(String cc_harvest) {
        this.cc_harvest = cc_harvest;
    }
}
