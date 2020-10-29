package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_CENTER}, tableName = DatabaseStringConstants.HARVEST_LOCATION_TABLE)
public class DistributionCenterTable {

    @ColumnInfo(name = DatabaseStringConstants.COL_CENTER)
    @NonNull
    private String centre;

    @ColumnInfo(name = DatabaseStringConstants.COL_STATE)
    private String state;

    @ColumnInfo(name = DatabaseStringConstants.COL_LGA)
    private String lga;

    @ColumnInfo(name = DatabaseStringConstants.COL_WARD)
    private String ward;

    @ColumnInfo(name = DatabaseStringConstants.COL_DEACTIVATE_HARVEST)
    private String deactivate;

    public DistributionCenterTable(@NonNull String centre, String state, String lga, String ward, String deactivate) {
        this.centre = centre;
        this.state = state;
        this.lga = lga;
        this.ward = ward;
        this.deactivate = deactivate;
    }

}
