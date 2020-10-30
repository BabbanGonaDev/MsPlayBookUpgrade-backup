package com.babbangona.mspalybookupgrade.HarvestSummary.data.dao;

import com.babbangona.mspalybookupgrade.HarvestSummary.data.entities.CollectionCenterEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CollectionCenterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CollectionCenterEntity...collectionCenterEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CollectionCenterEntity> collectionCenterEntities);

    @Query("SELECT * FROM collection_center_member_info WHERE unique_member_id = :uniqueMemberId")
    List<CollectionCenterEntity> getIndividualHarvestDetails(String uniqueMemberId);

    @Query("SELECT * FROM collection_center_member_info WHERE ik_number = :ikNumber")
    List<CollectionCenterEntity> getAllTgHarvestDetails(String ikNumber);
}
