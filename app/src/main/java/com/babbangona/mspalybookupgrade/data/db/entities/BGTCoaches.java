package com.babbangona.mspalybookupgrade.data.db.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;


@Entity(primaryKeys = {DatabaseStringConstants.COL_BGT_ID},
        tableName = DatabaseStringConstants.BGT_COACHES_TABLE)

public class BGTCoaches {

    @ColumnInfo(name = DatabaseStringConstants.COL_BGT_ID)
    @NonNull
    private String bgt_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_COACH_ID)
    private String coach_id;

    public BGTCoaches(@NonNull String bgt_id, String coach_id) {
        this.bgt_id = bgt_id;
        this.coach_id = coach_id;
    }

    @NonNull
    public String getBgt_id() {
        return bgt_id;
    }

    public void setBgt_id(@NonNull String bgt_id) {
        this.bgt_id = bgt_id;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }
}
