package com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler;

import android.os.Parcel;
import android.os.Parcelable;

public class PWSFieldListRecyclerModel {

    private String unique_field_id;

    private String field_r_id;

    private String member_name;

    private String phone_number;

    private String village_name;

    private String min_lat;

    private String max_lat;

    private String min_lng;

    private String max_lng;

    private String field_size;

    private String ik_number;

    private String crop_type;

    public PWSFieldListRecyclerModel() {
    }

    public PWSFieldListRecyclerModel(String unique_field_id, String field_r_id, String member_name,
                                     String phone_number, String village_name, String min_lat,
                                     String max_lat, String min_lng, String max_lng, String field_size,
                                     String ik_number, String crop_type) {
        this.unique_field_id = unique_field_id;
        this.field_r_id = field_r_id;
        this.member_name = member_name;
        this.phone_number = phone_number;
        this.village_name = village_name;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_lng = min_lng;
        this.max_lng = max_lng;
        this.field_size = field_size;
        this.ik_number = ik_number;
        this.crop_type = crop_type;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getField_r_id() {
        return field_r_id;
    }

    public void setField_r_id(String field_r_id) {
        this.field_r_id = field_r_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
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

    public String getField_size() {
        return field_size;
    }

    public void setField_size(String field_size) {
        this.field_size = field_size;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }

    public static class PWSListModel{
        private String pws_id;
        private String unique_field_id;
        private String category;
        private String pws_area;
        private String date_logged;

        public PWSListModel(String pws_id, String unique_field_id, String category, String pws_area, String date_logged) {
            this.pws_id = pws_id;
            this.unique_field_id = unique_field_id;
            this.category = category;
            this.pws_area = pws_area;
            this.date_logged = date_logged;
        }

        public String getPws_id() {
            return pws_id;
        }

        public String getUnique_field_id() {
            return unique_field_id;
        }

        public void setUnique_field_id(String unique_field_id) {
            this.unique_field_id = unique_field_id;
        }

        public void setPws_id(String pws_id) {
            this.pws_id = pws_id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPws_area() {
            return pws_area;
        }

        public void setPws_area(String pws_area) {
            this.pws_area = pws_area;
        }

        public String getDate_logged() {
            return date_logged;
        }

        public void setDate_logged(String date_logged) {
            this.date_logged = date_logged;
        }
    }

    public static class PWSMapModel implements Parcelable {
        private String fieldSize;

        private String lat_longs;
        private String min_lat;
        private String max_lat;
        private String min_long;
        private String max_long;
        private String latitude;
        private String longitude;

        public PWSMapModel() {
            this.fieldSize = "";
            this.lat_longs = "";
            this.min_lat = "";
            this.max_lat = "";
            this.min_long = "";
            this.max_long = "";
            this.latitude = "";
            this.longitude = "";
        }

        public PWSMapModel(String fieldSize, String lat_longs, String min_lat, String max_lat,
                           String min_long, String max_long, String latitude, String longitude) {
            this.fieldSize = fieldSize;
            this.lat_longs = lat_longs;
            this.min_lat = min_lat;
            this.max_lat = max_lat;
            this.min_long = min_long;
            this.max_long = max_long;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public PWSMapModel(Parcel in){
            String[] data = new String[8];

            in.readStringArray(data);
            // the order needs to be the same as in writeToParcel() method
            this.fieldSize  = data[0];
            this.lat_longs  = data[1];
            this.min_lat    = data[2];
            this.max_lat    = data[3];
            this.min_long   = data[4];
            this.max_long   = data[5];
            this.latitude   = data[6];
            this.longitude  = data[7];
        }

        public String getFieldSize() {
            return fieldSize;
        }

        public void setFieldSize(String fieldSize) {
            this.fieldSize = fieldSize;
        }

        public String getLat_longs() {
            return lat_longs;
        }

        public void setLat_longs(String lat_longs) {
            this.lat_longs = lat_longs;
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

        public String getMin_long() {
            return min_long;
        }

        public void setMin_long(String min_long) {
            this.min_long = min_long;
        }

        public String getMax_long() {
            return max_long;
        }

        public void setMax_long(String max_long) {
            this.max_long = max_long;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(new String[] {
                    this.fieldSize,
                    this.lat_longs,
                    this.min_lat,
                    this.max_lat,
                    this.min_long,
                    this.max_long,
                    this.latitude,
                    this.longitude
            });
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public PWSMapModel createFromParcel(Parcel in) {
                return new PWSMapModel(in);
            }

            public PWSMapModel[] newArray(int size) {
                return new PWSMapModel[size];
            }
        };
    }

    public static class PWSDetails{
        private String category;
        private String mik_field_size;
        private String pc_field_size;
        private String mik_description;
        private String pc_description;

        public PWSDetails() {
            this.category = "";
            this.mik_field_size = "";
            this.pc_field_size = "";
            this.mik_description = "";
            this.pc_description = "";
        }

        public PWSDetails(String category, String mik_field_size, String pc_field_size, String mik_description,
                          String pc_description) {
            this.category = category;
            this.mik_field_size = mik_field_size;
            this.pc_field_size = pc_field_size;
            this.mik_description = mik_description;
            this.pc_description = pc_description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getMik_field_size() {
            return mik_field_size;
        }

        public void setMik_field_size(String mik_field_size) {
            this.mik_field_size = mik_field_size;
        }

        public String getPc_field_size() {
            return pc_field_size;
        }

        public void setPc_field_size(String pc_field_size) {
            this.pc_field_size = pc_field_size;
        }

        public String getMik_description() {
            return mik_description;
        }

        public void setMik_description(String mik_description) {
            this.mik_description = mik_description;
        }

        public String getPc_description() {
            return pc_description;
        }

        public void setPc_description(String pc_description) {
            this.pc_description = pc_description;
        }
    }
}
