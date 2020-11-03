package com.babbangona.mspalybookupgrade.LocationTraker.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.babbangona.mspalybookupgrade.LocationTraker.database.Converters;

@Entity(tableName = "location_tracker")
@TypeConverters(Converters.class)
public class StaffDetails {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String bgtId;
    private String latitude;
    private String longitude;
    private String coach_id;
    private String imei;
    private String appVersion;
    private int outside_field_portfolio_flag;
    private int outside_village_portfolio_flag;
    private String dateLogged;
    private String timestamp;
    private int sync_flag;

    @NonNull
    public String getBgtId() {
        return bgtId;
    }

    public void setBgtId(@NonNull String bgtId) {
        this.bgtId = bgtId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getOutside_field_portfolio_flag() {
        return outside_field_portfolio_flag;
    }

    public void setOutside_field_portfolio_flag(int outside_field_portfolio_flag) {
        this.outside_field_portfolio_flag = outside_field_portfolio_flag;
    }

    public int getOutside_village_portfolio_flag() {
        return outside_village_portfolio_flag;
    }

    public void setOutside_village_portfolio_flag(int outside_village_portfolio_flag) {
        this.outside_village_portfolio_flag = outside_village_portfolio_flag;
    }

    public String getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(String dateLogged) {
        this.dateLogged = dateLogged;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSync_flag() {
        return sync_flag;
    }

    public void setSync_flag(int sync_flag) {
        this.sync_flag = sync_flag;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "bgtId='" + bgtId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", coach_id='" + coach_id + '\'' +
                ", imei='" + imei + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", outside_field_portfolio_flag=" + outside_field_portfolio_flag +
                ", outside_village_portfolio_flag=" + outside_village_portfolio_flag +
                ", dateLogged=" + dateLogged +
                ", timestamp=" + timestamp +
                ", sync_flag=" + sync_flag +
                '}';
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public static class StaffWithLocation {
        private String staff_id;
        private String lat_long;

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getLat_long() {
            return lat_long;
        }

        public void setLat_long(String lat_long) {
            this.lat_long = lat_long;
        }
    }

    public static class StaffWithVillageLocation {
        private String staff_id;
        private String latitude;
        private String longitude;

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}