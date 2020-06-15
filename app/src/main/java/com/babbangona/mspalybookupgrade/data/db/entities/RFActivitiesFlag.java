package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID_RF_ACTIVITY,
        DatabaseStringConstants.COL_RF_TYPE},
        tableName = DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE)
public class RFActivitiesFlag {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_HG_ACTIVITY)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_RF_TYPE)
    @NonNull
    private String rf_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_RF_DATE)
    private String rf_date;

    @ColumnInfo(name = DatabaseStringConstants.COL_RF_STATUS)
    private String rf_status;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_RF_ACTIVITIES)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_RF_ACTIVITIES)
    private String sync_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_RF_ACTIVITIES)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_CROP_TYPE_RF_ACTIVITIES)
    private String crop_type;

    public RFActivitiesFlag(@NonNull String unique_field_id, @NonNull String rf_type, String rf_date,
                            String rf_status, String staff_id, String sync_flag, String ik_number,
                            String crop_type) {
        this.unique_field_id = unique_field_id;
        this.rf_type = rf_type;
        this.rf_date = rf_date;
        this.rf_status = rf_status;
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
    public String getRf_type() {
        return rf_type;
    }

    public void setRf_type(@NonNull String rf_type) {
        this.rf_type = rf_type;
    }

    public String getRf_date() {
        return rf_date;
    }

    public void setRf_date(String rf_date) {
        this.rf_date = rf_date;
    }

    public String getRf_status() {
        return rf_status;
    }

    public void setRf_status(String rf_status) {
        this.rf_status = rf_status;
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
