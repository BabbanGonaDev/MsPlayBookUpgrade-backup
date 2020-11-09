package com.babbangona.mspalybookupgrade.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "village_locations")
public class VillageLocations {

    @NonNull
    @PrimaryKey
    private String unique_member_id;
    private String village_name;
    private String village_id;
    private String latitude;
    private String longitude;
    private String staff_id;

    public VillageLocations(@NonNull String unique_member_id, String village_name, String village_id, String latitude, String longitude, String staff_id) {
        this.unique_member_id = unique_member_id;
        this.village_name = village_name;
        this.village_id = village_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.staff_id = staff_id;
    }

    @NonNull
    public String getUnique_member_id() {
        return unique_member_id;
    }

    public String getVillage_name() {
        return village_name;
    }

    public String getVillage_id() {
        return village_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStaff_id() {
        return staff_id;
    }

    /**
     * Tuple to get the locations list for BGT location tracker
     */
    public static class villages {
        private String village_id;
        private String latitude;
        private String longitude;

        public String getVillage_id() {
            return village_id;
        }

        public void setVillage_id(String village_id) {
            this.village_id = village_id;
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
