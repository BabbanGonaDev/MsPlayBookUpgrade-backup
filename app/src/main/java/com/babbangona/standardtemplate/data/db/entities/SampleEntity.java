package com.babbangona.standardtemplate.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {"col1"}, tableName = "table1")
public class SampleEntity {



    @ColumnInfo(name = "col1")
    @NonNull
    private String col1;

    @ColumnInfo(name = "col2")
    @NonNull
    private String col2;

    public SampleEntity(@NonNull String col1, String col2) {
        this.col1 = col1;
        this.col2 = col2;

    }

    @NonNull
    public String getCol1() {
        return col1;
    }

    @NonNull
    public String getCol2() {
        return col2;
    }
}
