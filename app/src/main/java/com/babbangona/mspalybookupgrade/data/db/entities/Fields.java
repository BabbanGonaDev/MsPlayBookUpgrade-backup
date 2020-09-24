package com.babbangona.mspalybookupgrade.data.db.entities;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

import org.jetbrains.annotations.NotNull;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_UNIQUE_FIELD_ID_FIELDS},
        tableName = DatabaseStringConstants.FIELDS_TABLE)
public class Fields {

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_FIELD_ID_FIELDS)
    @NonNull
    private String unique_field_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_UNIQUE_MEMBER_ID)
    private String unique_member_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_FIELD_SIZE)
    private String field_size;

    @ColumnInfo(name = DatabaseStringConstants.COL_STAFF_ID_FIELDS)
    private String staff_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_MIDDLE)
    private String middle;

    @ColumnInfo(name = DatabaseStringConstants.COL_MIN_LAT)
    private String min_lat;

    @ColumnInfo(name = DatabaseStringConstants.COL_MAX_LAT)
    private String max_lat;

    @ColumnInfo(name = DatabaseStringConstants.COL_MIN_LNG)
    private String min_lng;

    @ColumnInfo(name = DatabaseStringConstants.COL_MAX_LNG)
    private String max_lng;

    @ColumnInfo(name = DatabaseStringConstants.COL_DEACTIVATE)
    private String deactivate;

    @ColumnInfo(name = DatabaseStringConstants.COL_MSS)
    private String mss;

    @ColumnInfo(name = DatabaseStringConstants.COL_CROP_TYPE_FIELDS)
    private String crop_type;

    @ColumnInfo(name = DatabaseStringConstants.COL_CODE)
    private String field_code;

    public Fields() {
    }

    public Fields(@NonNull String unique_field_id, String unique_member_id, String field_size,
                  String staff_id, String middle, String min_lat, String max_lat, String min_lng,
                  String max_lng, String deactivate, String mss, String crop_type, String field_code) {
        this.unique_field_id = unique_field_id;
        this.unique_member_id = unique_member_id;
        this.field_size = field_size;
        this.staff_id = staff_id;
        this.middle = middle;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_lng = min_lng;
        this.max_lng = max_lng;
        this.deactivate = deactivate;
        this.mss = mss;
        this.crop_type = crop_type;
        this.field_code = field_code;
    }

    @NotNull
    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(@NotNull String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getField_size() {
        return field_size;
    }

    public void setField_size(String field_size) {
        this.field_size = field_size;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getMin_lat() {
        return min_lat;
    }

    public void setMin_lat(String min_lat) {
        this.min_lat = min_lat;
    }

    public String getMax_lat() {
        return max_lat;
    }

    public void setMax_lat(String max_lat) {
        this.max_lat = max_lat;
    }

    public String getMin_lng() {
        return min_lng;
    }

    public void setMin_lng(String min_lng) {
        this.min_lng = min_lng;
    }

    public String getMax_lng() {
        return max_lng;
    }

    public void setMax_lng(String max_lng) {
        this.max_lng = max_lng;
    }

    public String getDeactivate() {
        return deactivate;
    }

    public void setDeactivate(String deactivate) {
        this.deactivate = deactivate;
    }

    public String getMss() {
        return mss;
    }

    public void setMss(String mss) {
        this.mss = mss;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }

    public String getField_code() {
        return field_code;
    }

    public void setField_code(String field_code) {
        this.field_code = field_code;
    }

    public static Fields fromContentValues(ContentValues contentValues) {

        Fields fields = new Fields();
        if (contentValues.containsKey(DatabaseStringConstants.COL_UNIQUE_FIELD_ID_FIELDS)) {
            fields.setUnique_field_id(contentValues.getAsString(DatabaseStringConstants.COL_UNIQUE_FIELD_ID_FIELDS));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_UNIQUE_MEMBER_ID)) {
            fields.setUnique_member_id(contentValues.getAsString(DatabaseStringConstants.COL_UNIQUE_MEMBER_ID));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_FIELD_SIZE)) {
            fields.setField_size(contentValues.getAsString(DatabaseStringConstants.COL_FIELD_SIZE));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_STAFF_ID_FIELDS)) {
            fields.setStaff_id(contentValues.getAsString(DatabaseStringConstants.COL_STAFF_ID_FIELDS));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_MIDDLE)) {
            fields.setMiddle(contentValues.getAsString(DatabaseStringConstants.COL_MIDDLE));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_MIN_LAT)) {
            fields.setMin_lat(contentValues.getAsString(DatabaseStringConstants.COL_MIN_LAT));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_MAX_LAT)) {
            fields.setMax_lat(contentValues.getAsString(DatabaseStringConstants.COL_MAX_LAT));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_MIN_LNG)) {
            fields.setMin_lng(contentValues.getAsString(DatabaseStringConstants.COL_MIN_LNG));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_MAX_LNG)) {
            fields.setMax_lng(contentValues.getAsString(DatabaseStringConstants.COL_MAX_LNG));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_DEACTIVATE)) {
            fields.setDeactivate(contentValues.getAsString(DatabaseStringConstants.COL_DEACTIVATE));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_MSS)) {
            fields.setMss(contentValues.getAsString(DatabaseStringConstants.COL_MSS));
        }

        if (contentValues.containsKey(DatabaseStringConstants.COL_CROP_TYPE_FIELDS)) {
            fields.setCrop_type(contentValues.getAsString(DatabaseStringConstants.COL_CROP_TYPE_FIELDS));
        }

        return fields;
    }
}
