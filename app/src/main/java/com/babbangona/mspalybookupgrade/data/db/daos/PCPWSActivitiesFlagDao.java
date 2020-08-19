package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler.PCPWSRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.PCPWSActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivitiesFlag;

import java.util.List;

@Dao
public abstract class PCPWSActivitiesFlagDao {

    @Query(" SELECT COUNT(DISTINCT a.unique_field_id) FROM pws_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.solve = '0' AND a.deactivate = '0' " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND b.deactivate = '0' ")
    public abstract int getPWSCount(String staff_id);

    @Query(" SELECT COUNT(DISTINCT a.unique_field_id) FROM pws_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.solve = '0' AND a.deactivate = '0' " +
            "AND ((b.min_lat+b.max_lat)/2) > :min_lat AND ((b.min_lat+b.max_lat)/2) <= :max_lat AND ((b.min_lng+b.max_lng)/2) > :min_lng " +
            "AND ((b.min_lng+b.max_lng)/2) <= :max_lng AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND b.deactivate = '0' ")
    public abstract int fieldPortionCountForPWS(String staff_id, double min_lat, double max_lat, double min_lng, double max_lng);

    @Query("SELECT COUNT(unique_field_id) FROM pws_activities_flag WHERE unique_field_id = :unique_field_id AND solve = '0' AND deactivate = '0'")
    public abstract int countFieldInPWSActivity(String unique_field_id);

    @Query("SELECT DISTINCT category FROM pws_activities_flag WHERE unique_field_id = :unique_field_id AND solve = '0' AND deactivate = '0'")
    public abstract List<String> getAllFieldPWS(String unique_field_id);

    @Query(" SELECT COUNT(pws_id) FROM pc_pws_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedPCPWSActivitiesCount();

    @Query(" SELECT * FROM pc_pws_activities_flag WHERE sync_flag != '1' ")
    public abstract List<PCPWSActivitiesFlag> getUnSyncedPCPWSActivities();

    @Query(" UPDATE pc_pws_activities_flag SET sync_flag = '1' WHERE pws_id = :pws_id ")
    public abstract void updatePCPWSActivitiesSyncFlags(String pws_id);

    @Query(" SELECT a.pws_id, a.unique_field_id, a.category, a.date_logged, " +
            "c.first_name || ' ' || c.last_name as member_name, " +
            "'R20-' || c.ik_number || '-' || c.member_id as member_r_id " +
            "FROM pws_activities_flag a LEFT OUTER JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "LEFT OUTER JOIN members c ON b.unique_member_id = c.unique_member_id " +
            "LEFT OUTER JOIN pc_pws_activities_flag d ON d.pws_id = a.pws_id " +
            "WHERE LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND a.deactivate = '0' AND d.pws_id IS NULL ")
    public abstract DataSource.Factory<Integer, PCPWSRecyclerModel> getPendingClaims(String staff_id);

    @Query(" SELECT COUNT(DISTINCT a.pws_id) " +
            "FROM pws_activities_flag a LEFT OUTER JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "LEFT OUTER JOIN members c ON b.unique_member_id = c.unique_member_id " +
            "LEFT OUTER JOIN pc_pws_activities_flag d ON d.pws_id = a.pws_id " +
            "WHERE LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND a.deactivate = '0' AND d.pws_id IS NULL ")
    public abstract int getPendingClaimsCount(String staff_id);

    @Query(" SELECT a.pws_id, a.unique_field_id, a.category, a.date_logged, " +
            "c.first_name || ' ' || c.last_name as member_name, " +
            "'R20-' || c.ik_number || '-' || c.member_id as member_r_id " +
            "FROM pws_activities_flag a LEFT OUTER JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "LEFT OUTER JOIN members c ON b.unique_member_id = c.unique_member_id " +
            "LEFT OUTER JOIN pc_pws_activities_flag d ON d.pws_id = a.pws_id " +
            "WHERE LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND " +
            "LOWER(a.unique_field_id || a.category || a.date_logged || c.first_name || ' ' || c.last_name || 'R20-' || c.ik_number || '-' || c.member_id) LIKE LOWER(:search) " +
            "AND a.deactivate = '0' AND d.pws_id IS NULL ")
    public abstract DataSource.Factory<Integer, PCPWSRecyclerModel> getPendingClaims(String staff_id, String search);

    @Query(" SELECT a.pws_id, a.unique_field_id, a.category, a.date_logged, " +
            "c.first_name || ' ' || c.last_name as member_name, " +
            "'R20-' || c.ik_number || '-' || c.member_id as member_r_id " +
            "FROM pc_pws_activities_flag a LEFT OUTER JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "LEFT OUTER JOIN members c ON b.unique_member_id = c.unique_member_id " +
            "LEFT OUTER JOIN pws_activities_flag d ON d.pws_id = a.pws_id " +
            "WHERE LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND a.deactivate = '0' ORDER BY a.date_logged DESC ")
    public abstract DataSource.Factory<Integer, PCPWSRecyclerModel> getReviewedClaims(String staff_id);

    @Query(" SELECT COUNT(DISTINCT a.pws_id) " +
            "FROM pc_pws_activities_flag a LEFT OUTER JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "LEFT OUTER JOIN members c ON b.unique_member_id = c.unique_member_id " +
            "LEFT OUTER JOIN pws_activities_flag d ON d.pws_id = a.pws_id " +
            "WHERE LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND a.deactivate = '0' ")
    public abstract int getReviewedClaimsCount(String staff_id);

    @Query(" SELECT a.pws_id, a.unique_field_id, a.category, a.date_logged, " +
            "c.first_name || ' ' || c.last_name as member_name, " +
            "'R20-' || c.ik_number || '-' || c.member_id as member_r_id " +
            "FROM pc_pws_activities_flag a LEFT OUTER JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "LEFT OUTER JOIN members c ON b.unique_member_id = c.unique_member_id " +
            "LEFT OUTER JOIN pws_activities_flag d ON d.pws_id = a.pws_id " +
            "WHERE LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND " +
            "LOWER(a.unique_field_id || a.category || a.date_logged || c.first_name || ' ' || c.last_name || 'R20-' || c.ik_number || '-' || c.member_id) LIKE LOWER(:search) " +
            "AND a.deactivate = '0' ORDER BY a.date_logged DESC ")
    public abstract DataSource.Factory<Integer, PCPWSRecyclerModel> getReviewedClaims(String staff_id, String search);

    /**
     * Insert the object in database
     * @param pcpwsActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(PCPWSActivitiesFlag pcpwsActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<PCPWSActivitiesFlag> pcpwsActivitiesFlags);

    /**
     * update the object in database
     * @param pcpwsActivitiesFlags, object to be updated
     */
    @Update
    public abstract void update(PCPWSActivitiesFlag pcpwsActivitiesFlags);

    /**
     * delete the object from database
     * @param pcpwsActivitiesFlags, object to be deleted
     */
    @Delete
    public abstract void delete(PCPWSActivitiesFlag pcpwsActivitiesFlags);



}
