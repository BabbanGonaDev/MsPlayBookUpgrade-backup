package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_CMP_ID}, tableName = DatabaseStringConstants.FERTILIZER_LOCATION_TABLE)
public class FertilizerLocationsTable {

    @ColumnInfo(name = DatabaseStringConstants.COL_CMP_ID)
    @NonNull
    private String cmp_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_DISTRIBUTION_CENTER)
    private String distribution_center;

    @ColumnInfo(name = DatabaseStringConstants.COL_STATUS_FERT_LOCATION)
    private String status;

    public FertilizerLocationsTable(@NonNull String cmp_id, String distribution_center, String status) {
        this.cmp_id = cmp_id;
        this.distribution_center = distribution_center;
        this.status = status;
    }

    @NonNull
    public String getCmp_id() {
        return cmp_id;
    }

    public void setCmp_id(@NonNull String cmp_id) {
        this.cmp_id = cmp_id;
    }

    public String getDistribution_center() {
        return distribution_center;
    }

    public void setDistribution_center(String distribution_center) {
        this.distribution_center = distribution_center;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
