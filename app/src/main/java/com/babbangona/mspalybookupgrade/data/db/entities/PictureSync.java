package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_PICTURE_NAME},
        tableName = DatabaseStringConstants.TABLE_PICTURE_SYNC)

public class PictureSync {

    @ColumnInfo(name = DatabaseStringConstants.COL_PICTURE_NAME)
    @NonNull
    private String picture_name;

    public PictureSync(){

    }

    public PictureSync(@NonNull String picture_name) {
        this.picture_name = picture_name;
    }

    @NonNull
    public String getPicture_name() {
        return picture_name;
    }

    public void setPicture_name(@NonNull String picture_name) {
        this.picture_name = picture_name;
    }
}
