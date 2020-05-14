package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_STAFF_ID_STAFF},
        tableName = DatabaseStringConstants.STAFF_TABLE)
public class StaffList {

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_STAFF)
    @NonNull
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_NAME)
    @NonNull
    private String staff_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_HUB)
    private String staff_hub;

    public StaffList(@NonNull String staff_id, @NonNull String staff_name, String staff_hub) {
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.staff_hub = staff_hub;
    }

    @NonNull
    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(@NonNull String staff_id) {
        this.staff_id = staff_id;
    }

    @NonNull
    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(@NonNull String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_hub() {
        return staff_hub;
    }

    public void setStaff_hub(String staff_hub) {
        this.staff_hub = staff_hub;
    }
}
