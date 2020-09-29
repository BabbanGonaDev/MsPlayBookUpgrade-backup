package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "favourites_table", primaryKeys = {"staff_id", "phone_number"})
public class FavouritesTable {

    @NonNull
    private String staff_id;
    @NonNull
    private String phone_number;
    private Integer active_flag;
    private Integer sync_flag;

    public FavouritesTable(@NonNull String staff_id, @NonNull String phone_number, Integer active_flag, Integer sync_flag) {
        this.staff_id = staff_id;
        this.phone_number = phone_number;
        this.active_flag = active_flag;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getStaff_id() {
        return staff_id;
    }

    @NonNull
    public String getPhone_number() {
        return phone_number;
    }

    public Integer getActive_flag() {
        return active_flag;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }

    /**
     * Tuple for sync down
     */

    public static class Download {
        private String staff_id;
        private String phone_number;
        private Integer active_flag;
        private Integer sync_flag;
        private String last_sync;

        public Download(String staff_id, String phone_number, Integer active_flag, Integer sync_flag, String last_sync) {
            this.staff_id = staff_id;
            this.phone_number = phone_number;
            this.active_flag = active_flag;
            this.sync_flag = sync_flag;
            this.last_sync = last_sync;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public Integer getActive_flag() {
            return active_flag;
        }

        public Integer getSync_flag() {
            return sync_flag;
        }

        public String getLast_sync() {
            return last_sync;
        }
    }

    /**
     * Tuple for sync up response
     */
    public static class Response {
        private String staff_id;
        private String phone_number;
        private Integer sync_flag;

        public Response(String staff_id, String phone_number, Integer sync_flag) {
            this.staff_id = staff_id;
            this.phone_number = phone_number;
            this.sync_flag = sync_flag;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public Integer getSync_flag() {
            return sync_flag;
        }
    }
}
