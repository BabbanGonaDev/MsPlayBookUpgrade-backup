package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_TABLE_ID,
        DatabaseStringConstants.COL_STAFF_ID_SYNC_SUMMARY},
        tableName = DatabaseStringConstants.SYNC_SUMMARY_TABLE)
public class SyncSummary {

    @ColumnInfo(name = DatabaseStringConstants.COL_TABLE_ID)
    @NonNull
    private String table_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_SYNC_SUMMARY)
    @NonNull
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_TABLE_NAME)
    private String table_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_STATUS)
    private String status;

    @ColumnInfo(name = DatabaseStringConstants.COL_REMARKS)
    private String remarks;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_TIME)
    private String sync_time;

    public SyncSummary(@NonNull String table_id, @NonNull String staff_id, String table_name,
                       String status, String remarks, String sync_time) {
        this.table_id = table_id;
        this.staff_id = staff_id;
        this.table_name = table_name;
        this.status = status;
        this.remarks = remarks;
        this.sync_time = sync_time;
    }

    @NonNull
    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(@NonNull String table_id) {
        this.table_id = table_id;
    }

    @NonNull
    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(@NonNull String staff_id) {
        this.staff_id = staff_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSync_time() {
        return sync_time;
    }

    public void setSync_time(String sync_time) {
        this.sync_time = sync_time;
    }
}
