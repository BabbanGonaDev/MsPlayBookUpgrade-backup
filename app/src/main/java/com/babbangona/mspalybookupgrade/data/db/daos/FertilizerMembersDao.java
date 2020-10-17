package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.FertilizerMembers;

import java.util.List;

@Dao
public abstract class FertilizerMembersDao {

    @Query(" SELECT COUNT(unique_member_id) FROM fertilizer_members WHERE deactivate = '0' " +
            "AND unique_member_id = :unique_member_id")
    public abstract int getFertilizerMemberCount(String unique_member_id);

    @Query("UPDATE fertilizer_members SET sync_flag = '1' WHERE unique_member_id = :unique_member_id")
    public abstract void updateFertilizerMembersSyncFlag(String unique_member_id);

    @Query(" SELECT COUNT(unique_member_id) FROM fertilizer_members WHERE sync_flag != '1' ")
    public abstract int getUnSyncedFertilizerMembersCount();

    @Query(" SELECT * FROM fertilizer_members WHERE sync_flag != '1' ")
    public abstract List<FertilizerMembers> getUnSyncedFertilizerMembers();

    @Query("UPDATE fertilizer_members SET distribution_centre = :collection_center, deactivate = '0', " +
            "sync_flag='0' WHERE unique_member_id = :unique_member_id" )
    public abstract void updateCollectionCenter(String collection_center, String unique_member_id);

    /**
     * Insert the object in database
     * @param fertilizerMembers, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(FertilizerMembers fertilizerMembers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<FertilizerMembers> fertilizerMembers);

    /**
     * update the object in database
     * @param fertilizerMembers, object to be updated
     */
    @Update
    public abstract void update(FertilizerMembers fertilizerMembers);

    /**
     * delete the object from database
     * @param fertilizerMembers, object to be deleted
     */
    @Delete
    public abstract void delete(FertilizerMembers fertilizerMembers);



}
