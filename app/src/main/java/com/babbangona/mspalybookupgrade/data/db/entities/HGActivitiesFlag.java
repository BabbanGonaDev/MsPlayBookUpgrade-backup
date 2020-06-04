package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID_HG_ACTIVITY,
        DatabaseStringConstants.COL_HG_TYPE},
        tableName = DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE)
public class HGActivitiesFlag {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_HG_ACTIVITY)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_HG_TYPE)
    @NonNull
    private String hg_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_HG_DATE)
    private String hg_date;

    @ColumnInfo(name = DatabaseStringConstants.COL_HG_STATUS)
    private String hg_status;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_HG_ACTIVITIES)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_HG_ACTIVITIES)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_HG_ACTIVITIES)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_CROP_TYPE_HG_ACTIVITIES)
    private String crop_type;

    public HGActivitiesFlag(@NonNull String unique_field_id, @NonNull String hg_type, String hg_date,
                            String hg_status, String staff_id, String sync_flag, String ik_number,
                            String crop_type) {
        this.unique_field_id = unique_field_id;
        this.hg_type = hg_type;
        this.hg_date = hg_date;
        this.hg_status = hg_status;
        this.staff_id = staff_id;
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
    public String getHg_type() {
        return hg_type;
    }

    public void setHg_type(@NonNull String hg_type) {
        this.hg_type = hg_type;
    }

    public String getHg_date() {
        return hg_date;
    }

    public void setHg_date(String hg_date) {
        this.hg_date = hg_date;
    }

    public String getHg_status() {
        return hg_status;
    }

    public void setHg_status(String hg_status) {
        this.hg_status = hg_status;
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
}
