package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_RF_TYPE_RF_LIST,
        DatabaseStringConstants.COL_USER_CATEGORY_RF_LIST},
        tableName = DatabaseStringConstants.RF_LIST_TABLE)
public class RFList {

    @ColumnInfo(name = DatabaseStringConstants.COL_RF_TYPE_RF_LIST)
    @NonNull
    private String rf_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_RF_TYPE_DEACTIVATED_STATUS)
    private String deactivated_status;

    @ColumnInfo(name = DatabaseStringConstants.COL_USER_CATEGORY_RF_LIST)
    @NonNull
    private String user_category;

    public RFList(@NonNull String rf_type, String deactivated_status, @NonNull String user_category) {
        this.rf_type = rf_type;
        this.deactivated_status = deactivated_status;
        this.user_category = user_category;
    }

    @NonNull
    public String getRf_type() {
        return rf_type;
    }

    public void setRf_type(@NonNull String rf_type) {
        this.rf_type = rf_type;
    }

    public String getDeactivated_status() {
        return deactivated_status;
    }

    public void setDeactivated_status(String deactivated_status) {
        this.deactivated_status = deactivated_status;
    }

    @NonNull
    public String getUser_category() {
        return user_category;
    }

    public void setUser_category(@NonNull String user_category) {
        this.user_category = user_category;
    }
}
