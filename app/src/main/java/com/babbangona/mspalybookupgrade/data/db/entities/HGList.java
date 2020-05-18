package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_HG_TYPE_HG_LIST},
        tableName = DatabaseStringConstants.HG_LIST_TABLE)
public class HGList {

    @ColumnInfo(name = DatabaseStringConstants.COL_HG_TYPE_HG_LIST)
    @NonNull
    private String hg_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_HG_TYPE_DEACTIVATED_STATUS)
    private String deactivated_status;

    @ColumnInfo(name = DatabaseStringConstants.COL_USER_CATEGORY_HG_LIST)
    private String user_category;

    public HGList(@NonNull String hg_type, String deactivated_status, String user_category) {
        this.hg_type = hg_type;
        this.deactivated_status = deactivated_status;
        this.user_category = user_category;
    }

    @NonNull
    public String getHg_type() {
        return hg_type;
    }

    public void setHg_type(@NonNull String hg_type) {
        this.hg_type = hg_type;
    }

    public String getDeactivated_status() {
        return deactivated_status;
    }

    public void setDeactivated_status(String deactivated_status) {
        this.deactivated_status = deactivated_status;
    }

    public String getUser_category() {
        return user_category;
    }

    public void setUser_category(String user_category) {
        this.user_category = user_category;
    }
}
