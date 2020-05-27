package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

@Entity(primaryKeys = {DatabaseStringConstants.COL_STAFF_ROLE},
        tableName = DatabaseStringConstants.CATEGORY_TABLE)
public class Category {

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ROLE)
    @NonNull
    private String role;

    @ColumnInfo(name = DatabaseStringConstants.COL_CATEGORY)
    private String category;

    public Category(@NonNull String role, String category) {
        this.role = role;
        this.category = category;
    }

    @NonNull
    public String getRole() {
        return role;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
