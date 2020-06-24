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

    public AppVariables(@NonNull String variable_id, String edit_harvest_location_flag,
                        String minimum_log_date, String maximum_log_date) {
        this.variable_id = variable_id;
        this.edit_harvest_location_flag = edit_harvest_location_flag;
        this.minimum_log_date = minimum_log_date;
        this.maximum_log_date = maximum_log_date;
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
}
