package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "collection_center_table")
public class CollectionCenterTable {

    @PrimaryKey
    @NonNull
    private String cc_id;
    private String state;
    private String lga;
    private String cc_name;
    private String date_updated;

    public CollectionCenterTable(@NonNull String cc_id, String state, String lga, String cc_name, String date_updated) {
        this.cc_id = cc_id;
        this.state = state;
        this.lga = lga;
        this.cc_name = cc_name;
        this.date_updated = date_updated;
    }

    @NonNull
    public String getCc_id() {
        return cc_id;
    }

    public String getState() {
        return state;
    }

    public String getLga() {
        return lga;
    }

    public String getCc_name() {
        return cc_name;
    }

    public String getDate_updated() {
        return date_updated;
    }
}
