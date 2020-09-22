package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;


@Entity(primaryKeys = {DatabaseStringConstants.COL_LOCATION_ID}, tableName = DatabaseStringConstants.LOCATIONS_TABLE)
public class LocationEntity {

    @ColumnInfo(name = DatabaseStringConstants.COL_LOCATION_ID)
    @NonNull
    private String location_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_STATE_LOC)
    private String state;

    @ColumnInfo(name = DatabaseStringConstants.COL_LGA_LOC)
    private String lga;

    @ColumnInfo(name = DatabaseStringConstants.COL_WARD_LOC)
    private String ward;


    public LocationEntity(@NonNull String location_id, String state, String lga, String ward) {
        this.location_id = location_id;
        this.state = state;
        this.lga = lga;
        this.ward = ward;
    }

    @NonNull
    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(@NonNull String location_id) {
        this.location_id = location_id;
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

}
