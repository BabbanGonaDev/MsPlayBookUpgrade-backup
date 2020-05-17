package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_MEMBERS},
        tableName = DatabaseStringConstants.MEMBERS_TABLE)
public class Members {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_MEMBERS)
    @NonNull
    private String unique_member_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_MEMBER_ID)
    private String member_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_FIRST_NAME)
    private String first_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_LAST_NAME)
    private String last_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_PHONE_NUMBER)
    private String phone_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_VILLAGE_NAME)
    private String village_name;

    public Members(@NonNull String unique_member_id, String ik_number, String member_id,
                   String first_name, String last_name, String phone_number, String village_name) {
        this.unique_member_id = unique_member_id;
        this.ik_number = ik_number;
        this.member_id = member_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.village_name = village_name;
    }

    @NonNull
    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(@NonNull String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }
}
