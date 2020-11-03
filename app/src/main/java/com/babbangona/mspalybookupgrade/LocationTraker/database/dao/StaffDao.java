package com.babbangona.mspalybookupgrade.LocationTraker.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.LocationTraker.database.entity.StaffDetails;

import java.util.List;

@Dao
public abstract class StaffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(StaffDetails... staff);

    @Query("SELECT * FROM location_tracker")
    public abstract List<StaffDetails> getAll();

    @Query("SELECT * FROM location_tracker WHERE bgtId=:staffID")
    public abstract StaffDetails getStaffByID(String staffID);

    @Update
    public abstract void update(StaffDetails staff);

    @Query("SELECT staff_id AS staff_id,middle AS lat_long FROM fields WHERE staff_id=:staffID")
    public abstract List<StaffDetails.StaffWithLocation> getAllLocation(String staffID);

    @Query("SELECT staff_id,latitude,longitude FROM village_locations WHERE staff_id=:staffID")
    public abstract List<StaffDetails.StaffWithVillageLocation> getAllVillageLocation(String staffID);

    /*@Query("SELECT bgtId ," +
            "field.latitude AS field_lat," +
            "field.longitude AS field_long," +
            "village.latitude AS village_lat," +
            "village.longitude AS village_long " +
            "FROM staff " +
            "LEFT JOIN field on staff.coach_id=field.coach_id " +
            "LEFT JOIN village on staff.coach_id=village.coach_id " +
            "WHERE staff.bgtId=:staffID")
    public abstract Staff.StaffWithLocation getStaffWithLocation(String staffID);*/

}
