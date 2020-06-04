package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_CENTER}, tableName = DatabaseStringConstants.HARVEST_LOCATION_TABLE)
public class HarvestLocationsTable {

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

    public HarvestLocationsTable(@NonNull String centre, String state, String lga, String ward, String deactivate) {
        this.centre = centre;
        this.state = state;
        this.lga = lga;
        this.ward = ward;
        this.deactivate = deactivate;
    }

    @NonNull
    public String getCentre() {
        return centre;
    }

    public void setCentre(@NonNull String centre) {
        this.centre = centre;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDeactivate() {
        return deactivate;
    }

    public void setDeactivate(String deactivate) {
        this.deactivate = deactivate;
    }
}
