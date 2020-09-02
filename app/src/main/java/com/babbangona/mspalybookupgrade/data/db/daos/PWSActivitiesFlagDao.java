package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCStaffPSWPageRecycler.PCStaffPWSRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler.SetPortfolioRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivitiesFlag;

import java.util.List;

@Dao
public abstract class PWSActivitiesFlagDao {

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

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, b.ik_number, a.crop_type " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE a.unique_field_id = :unique_field_id " +
            "AND a.deactivate = '0' ")
    public abstract PWSFieldListRecyclerModel getFieldDetails(String unique_field_id);

    @Query(" SELECT a.category, a.pws_area as mik_field_size, a.description as mik_description, " +
            "b.description as pc_description, b.pws_area as pc_field_size " +
            "FROM pws_activities_flag a LEFT OUTER JOIN pc_pws_activities_flag b ON a.pws_id = b.pws_id " +
            "WHERE a.pws_id = :pws_id ")
    public abstract PWSFieldListRecyclerModel.PWSDetails getPWSDetails(String pws_id);

    @Query(" SELECT category " +
            "FROM pws_activities_flag " +
            "WHERE pws_id = :pws_id ")
    public abstract String getPWSCategory(String pws_id);

    /*@Query("SELECT COUNT(unique_field_id) FROM pws_activities_flag " +
            "WHERE unique_field_id = :unique_field_id AND solve = '0' AND deactivate = '0' AND category = :category")
    public abstract int countFieldSpecificPWSActivity(String unique_field_id, String category);

    @Query("UPDATE pws_activities_flag SET hg_status = :status, hg_date = :date," +
            "staff_id = :staff_id, sync_flag = '0', date_logged = :date_logged " +
            "WHERE unique_field_id = :unique_field_id AND hg_type = :hg_type")
    public abstract void updatePWSFlag(String unique_field_id, String hg_type, String status,
                                      String date, String staff_id, String date_logged);*/

    @Query("SELECT DISTINCT category FROM pws_activities_flag WHERE unique_field_id = :unique_field_id AND solve = '0' AND deactivate = '0'")
    public abstract List<String> getAllFieldPWS(String unique_field_id);

    @Query("SELECT pws_id, unique_field_id, category, pws_area, date_logged " +
            "FROM pws_activities_flag WHERE deactivate = '0' AND unique_field_id = :unique_field_id ORDER BY date_logged DESC")
    public abstract DataSource.Factory<Integer, PWSFieldListRecyclerModel.PWSListModel> getAllActivePWS(String unique_field_id);

    @Query(" SELECT COUNT(unique_field_id) FROM pws_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedPWSActivitiesCount();

    @Query(" SELECT * FROM pws_activities_flag WHERE sync_flag != '1' ")
    public abstract List<PWSActivitiesFlag> getUnSyncedPWSActivities();

    @Query(" UPDATE pws_activities_flag SET sync_flag = '1' WHERE pws_id = :pws_id ")
    public abstract void updatePWSActivitiesSyncFlags(String pws_id);

    @Query("SELECT a.staff_id, b.staff_name, COUNT(a.pws_id) as number_of_claims, COUNT(c.pws_id) as number_reviewed_claims " +
            "FROM pws_activities_flag a " +
            "LEFT OUTER JOIN staff b on b.staff_id = a.staff_id " +
            "LEFT OUTER JOIN pc_pws_activities_flag c on a.pws_id = c.pws_id " +
            "LEFT OUTER JOIN fields d on a.unique_field_id = d.unique_field_id " +
            "WHERE d.deactivate = '0' AND a.deactivate = '0' AND a.solve = '0' AND LOWER(d.staff_id || d.mss) LIKE :staff_id " +
            "GROUP BY a.staff_id ORDER BY ( COUNT(a.pws_id) - COUNT(c.pws_id) ) DESC ")
    public abstract DataSource.Factory<Integer, PCStaffPWSRecyclerModel> getPWSStaffList(String staff_id);

    @Query("SELECT a.staff_id, b.staff_name, COUNT(a.pws_id) as number_of_claims, COUNT(c.pws_id) as number_reviewed_claims " +
            "FROM pws_activities_flag a " +
            "LEFT OUTER JOIN staff b on a.staff_id = b.staff_id " +
            "LEFT OUTER JOIN pc_pws_activities_flag c on a.pws_id = c.pws_id " +
            "LEFT OUTER JOIN fields d on a.unique_field_id = d.unique_field_id " +
            "WHERE d.deactivate = '0' AND a.deactivate = '0' AND a.solve = '0' AND LOWER(d.staff_id || d.mss) LIKE LOWER(:staff_id) " +
            "AND LOWER(a.staff_id || b.staff_name) LIKE LOWER(:search) " +
            "GROUP BY a.staff_id ")
    public abstract DataSource.Factory<Integer, PCStaffPWSRecyclerModel> getPWSStaffList(String staff_id,String search);

    /**
     * Insert the object in database
     * @param pwsActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(PWSActivitiesFlag pwsActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<PWSActivitiesFlag> pwsActivitiesFlag);

    /**
     * update the object in database
     * @param pwsActivitiesFlag, object to be updated
     */
    @Update
    public abstract void update(PWSActivitiesFlag pwsActivitiesFlag);

    /**
     * delete the object from database
     * @param pwsActivitiesFlag, object to be deleted
     */
    @Delete
    public abstract void delete(PWSActivitiesFlag pwsActivitiesFlag);



}
