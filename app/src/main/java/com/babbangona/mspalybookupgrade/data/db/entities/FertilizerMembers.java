package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_FERT},
        tableName = DatabaseStringConstants.FERTILIZER_MEMBERS_TABLE)
public class FertilizerMembers {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_MEMBER_ID_FERT)
    @NonNull
    private String unique_member_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_FIRST_NAME_FERT)
    private String first_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_LAST_NAME_FERT)
    private String last_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_IK_NUMBER_FERT)
    private String ik_number;

    @ColumnInfo(name = DatabaseStringConstants.COL_VILLAGE_FERT)
    private String village_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_FACE_SCAN_FLAG)
    private String face_scan_flag;

    @ColumnInfo(name = DatabaseStringConstants.COL_TEMPLATE_FERT)
    private String template;

    @ColumnInfo(name = DatabaseStringConstants.COL_DEACTIVATE_FERT)
    private String deactivate;

    @ColumnInfo(name = DatabaseStringConstants.COL_MEMBER_PRESENCE)
    private String member_presence;

    @ColumnInfo(name = DatabaseStringConstants.COL_COLLECTION_CENTER_FERT)
    private String collection_center;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_FERT)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_APP_VERSION_FERT)
    private String app_version;

    @ColumnInfo(name = DatabaseStringConstants.COL_IMEI_FERT)
    private String imei;

    @ColumnInfo(name = DatabaseStringConstants.COL_SYNC_FLAG_FERT)
    private String sync_flag;

    public FertilizerMembers(@NonNull String unique_member_id, String first_name, String last_name,
                             String ik_number, String village_name, String face_scan_flag, String template,
                             String deactivate, String member_presence, String collection_center,
                             String staff_id, String app_version, String imei, String sync_flag) {
        this.unique_member_id = unique_member_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ik_number = ik_number;
        this.village_name = village_name;
        this.face_scan_flag = face_scan_flag;
        this.template = template;
        this.deactivate = deactivate;
        this.member_presence = member_presence;
        this.collection_center = collection_center;
        this.staff_id = staff_id;
        this.app_version = app_version;
        this.imei = imei;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(@NonNull String unique_member_id) {
        this.unique_member_id = unique_member_id;
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

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getFace_scan_flag() {
        return face_scan_flag;
    }

    public void setFace_scan_flag(String face_scan_flag) {
        this.face_scan_flag = face_scan_flag;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDeactivate() {
        return deactivate;
    }

    public void setDeactivate(String deactivate) {
        this.deactivate = deactivate;
    }

    public String getMember_presence() {
        return member_presence;
    }

    public void setMember_presence(String member_presence) {
        this.member_presence = member_presence;
    }

    public String getCollection_center() {
        return collection_center;
    }

    public void setCollection_center(String collection_center) {
        this.collection_center = collection_center;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(String sync_flag) {
        this.sync_flag = sync_flag;
    }
}
