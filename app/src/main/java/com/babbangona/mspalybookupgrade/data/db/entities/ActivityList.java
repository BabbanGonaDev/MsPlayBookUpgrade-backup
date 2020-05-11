package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;

//TODO: Revamp this page following this structure to your desired entity class
@Entity(primaryKeys = {DatabaseStringConstants.COL_ACTIVITY_ID,DatabaseStringConstants.COL_LANGUAGE_ID},
        tableName = DatabaseStringConstants.ACTIVITY_LIST_TABLE)
public class ActivityList {

    @ColumnInfo(name = DatabaseStringConstants.COL_ACTIVITY_ID)
    @NonNull
    private String activity_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_LANGUAGE_ID)
    @NonNull
    private String language_id;

    @ColumnInfo(name = DatabaseStringConstants.COL_ACTIVITY_NAME)
    private String activity_name;

    @ColumnInfo(name = DatabaseStringConstants.COL_ACTIVITY_DESC)
    private String activity_desc;

    @ColumnInfo(name = DatabaseStringConstants.COL_ACTIVITY_DESTINATION)
    private String activity_destination;

    @ColumnInfo(name = DatabaseStringConstants.COL_ACTIVITY_PRIORITY)
    private String activity_priority;

    @ColumnInfo(name = DatabaseStringConstants.COL_USER_CATEGORY)
    private String user_category;

    @ColumnInfo(name = DatabaseStringConstants.COL_DEACTIVATED)
    private String deactivated;

    public ActivityList(@NonNull String activity_id, @NonNull String language_id,
                        String activity_name, String activity_desc, String activity_destination,
                        String activity_priority, String user_category, String deactivated) {
        this.activity_id = activity_id;
        this.language_id = language_id;
        this.activity_name = activity_name;
        this.activity_desc = activity_desc;
        this.activity_destination = activity_destination;
        this.activity_priority = activity_priority;
        this.user_category = user_category;
        this.deactivated = deactivated;
    }

    @NonNull
    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(@NonNull String activity_id) {
        this.activity_id = activity_id;
    }

    @NonNull
    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(@NonNull String language_id) {
        this.language_id = language_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getActivity_desc() {
        return activity_desc;
    }

    public void setActivity_desc(String activity_desc) {
        this.activity_desc = activity_desc;
    }

    public String getActivity_destination() {
        return activity_destination;
    }

    public void setActivity_destination(String activity_destination) {
        this.activity_destination = activity_destination;
    }

    public String getActivity_priority() {
        return activity_priority;
    }

    public void setActivity_priority(String activity_priority) {
        this.activity_priority = activity_priority;
    }

    public String getUser_category() {
        return user_category;
    }

    public void setUser_category(String user_category) {
        this.user_category = user_category;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }
}
