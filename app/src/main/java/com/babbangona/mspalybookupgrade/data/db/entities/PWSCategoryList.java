package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_PWS_CATEGORY,
        DatabaseStringConstants.COL_USER_CATEGORY_PWS},
        tableName = DatabaseStringConstants.PWS_CATEGORY_LIST_TABLE)
public class PWSCategoryList {

    @ColumnInfo(name = DatabaseStringConstants.COL_PWS_CATEGORY)
    @NonNull
    private String pws_category;

    @ColumnInfo(name = DatabaseStringConstants.COL_USER_CATEGORY_PWS)
    @NonNull
    private String user_category;

    @ColumnInfo(name = DatabaseStringConstants.COL_PWS_CATEGORY_DEACTIVATED_STATUS)
    private String deactivated_status;

    public PWSCategoryList(@NonNull String pws_category, @NonNull String user_category, String deactivated_status) {
        this.pws_category = pws_category;
        this.user_category = user_category;
        this.deactivated_status = deactivated_status;
    }

    @NonNull
    public String getPws_category() {
        return pws_category;
    }

    public void setPws_category(@NonNull String pws_category) {
        this.pws_category = pws_category;
    }

    @NonNull
    public String getUser_category() {
        return user_category;
    }

    public void setUser_category(@NonNull String user_category) {
        this.user_category = user_category;
    }

    public String getDeactivated_status() {
        return deactivated_status;
    }

    public void setDeactivated_status(String deactivated_status) {
        this.deactivated_status = deactivated_status;
    }
}
