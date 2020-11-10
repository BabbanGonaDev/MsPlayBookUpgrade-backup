package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.data.db.entities.VillageLocations;

import java.util.List;

@Dao
public interface VillageLocationsDao {

    @Insert
    void insert(VillageLocations locations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<VillageLocations> list);

    @Query("SELECT village_id, latitude, longitude FROM village_locations")
    List<VillageLocations.villages> getVillageLocations();
}
