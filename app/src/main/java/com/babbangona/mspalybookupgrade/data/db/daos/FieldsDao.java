package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler.GridDetailsRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;

import java.util.List;

@Dao
public abstract class FieldsDao {

    @Query(" SELECT COUNT(unique_field_id) FROM fields WHERE deactivate = '0' " +
            "AND LOWER(staff_id || mss) LIKE LOWER(:staff_id) ")
    public abstract int getTotalFieldsCount(String staff_id);

    @Query(" SELECT ((MAX(max_lat*1.0)+MAX(min_lat*1.0))/2) as max_lat, " +
            "((MIN(max_lat*1.0)+MIN(min_lat*1.0))/2) as min_lat, " +
            "((MAX(max_lng*1.0)+MAX(min_lng*1.0))/2) as max_lng, " +
            "((MIN(max_lng*1.0)+MIN(min_lng*1.0))/2) as min_lng " +
            "FROM fields WHERE LOWER(staff_id || mss) LIKE LOWER(:staff_id) ")
    public abstract LiveData<GridDetailsRecyclerModel.InitialGridDetailsModel> viewLongLatRange(String staff_id);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, c.fertilizer_1_status, c.fertilizer_2_status " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "LEFT OUTER JOIN normal_activities_flag c ON a.unique_field_id = c.unique_field_id " +
            "WHERE ((a.min_lat+a.max_lat)/2) > :min_lat AND ((a.min_lat+a.max_lat)/2) <= :max_lat " +
            "AND ((a.min_lng+a.max_lng)/2) > :min_lng AND ((a.min_lng+a.max_lng)/2) <= :max_lng " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) ")
    public abstract DataSource.Factory<Integer, FieldListRecyclerModel> getFieldListByGrid(String staff_id,
                                                                                           double min_lat,
                                                                                           double max_lat,
                                                                                           double min_lng,
                                                                                           double max_lng);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, c.fertilizer_1_status, c.fertilizer_2_status " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "LEFT OUTER JOIN normal_activities_flag c ON a.unique_field_id = c.unique_field_id " +
            "WHERE ((a.min_lat+a.max_lat)/2) > :min_lat AND ((a.min_lat+a.max_lat)/2) <= :max_lat " +
            "AND ((a.min_lng+a.max_lng)/2) > :min_lng AND ((a.min_lng+a.max_lng)/2) <= :max_lng " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || 'R20-' || b.ik_number || '-' || b.member_id) " +
            "LIKE LOWER(:search)")
    public abstract DataSource.Factory<Integer, FieldListRecyclerModel> getFieldListByGrid(String staff_id,
                                                                                                 double min_lat,
                                                                                                 double max_lat,
                                                                                                 double min_lng,
                                                                                                 double max_lng,
                                                                                                 String search);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, c.fertilizer_1_status, c.fertilizer_2_status " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "LEFT OUTER JOIN normal_activities_flag c ON a.unique_field_id = c.unique_field_id " +
            "WHERE a.unique_field_id = :unique_field_id AND LOWER(b.ik_number) LIKE LOWER(:ik_number) " +
            "AND LOWER('R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:member_id) " +
            "AND LOWER(b.first_name || ' ' || b.last_name) LIKE LOWER(:member_name) " +
            "AND LOWER(b.village_name) LIKE LOWER(:village) " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || 'R20-' || b.ik_number || '-' || b.member_id) " +
            "LIKE LOWER(:search) ")
    public abstract DataSource.Factory<Integer, FieldListRecyclerModel> getFieldListByActivitySearch(String staff_id,
                                                                                                     String unique_field_id,
                                                                                                     String ik_number,
                                                                                                     String member_id,
                                                                                                     String member_name,
                                                                                                     String village,
                                                                                                     String search);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, c.fertilizer_1_status, c.fertilizer_2_status " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "LEFT OUTER JOIN normal_activities_flag c ON a.unique_field_id = c.unique_field_id " +
            "WHERE a.unique_field_id = :unique_field_id AND LOWER(b.ik_number) LIKE LOWER(:ik_number) " +
            "AND LOWER('R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:member_id) " +
            "AND LOWER(b.first_name || ' ' || b.last_name) LIKE LOWER(:member_name) " +
            "AND LOWER(b.village_name) LIKE LOWER(:village) AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) ")
    public abstract DataSource.Factory<Integer, FieldListRecyclerModel> getFieldListByActivitySearch(String staff_id,
                                                                                                     String unique_field_id,
                                                                                                     String ik_number,
                                                                                                     String member_id,
                                                                                                     String member_name,
                                                                                                     String village);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, c.fertilizer_1_status, c.fertilizer_2_status " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "LEFT OUTER JOIN normal_activities_flag c ON a.unique_field_id = c.unique_field_id " +
            "WHERE LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) ")
    public abstract DataSource.Factory<Integer, FieldListRecyclerModel> getFieldListByStaffID(String staff_id);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id, c.fertilizer_1_status, c.fertilizer_2_status " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "LEFT OUTER JOIN normal_activities_flag c ON a.unique_field_id = c.unique_field_id " +
            "WHERE LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || 'R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:search) ")
    public abstract DataSource.Factory<Integer, FieldListRecyclerModel> getFieldListBySearch(String staff_id, String search);

    //HG Query

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) ")
    public abstract DataSource.Factory<Integer, HGFieldListRecyclerModel> getFieldListByStaffIDHG(String staff_id);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || 'R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:search) ")
    public abstract DataSource.Factory<Integer, HGFieldListRecyclerModel> getFieldListBySearchHG(String staff_id, String search);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE ((a.min_lat+a.max_lat)/2) > :min_lat AND ((a.min_lat+a.max_lat)/2) <= :max_lat " +
            "AND ((a.min_lng+a.max_lng)/2) > :min_lng AND ((a.min_lng+a.max_lng)/2) <= :max_lng " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) ")
    public abstract DataSource.Factory<Integer, HGFieldListRecyclerModel> getFieldListByGridHG(String staff_id,
                                                                                           double min_lat,
                                                                                           double max_lat,
                                                                                           double min_lng,
                                                                                           double max_lng);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE ((a.min_lat+a.max_lat)/2) > :min_lat AND ((a.min_lat+a.max_lat)/2) <= :max_lat " +
            "AND ((a.min_lng+a.max_lng)/2) > :min_lng AND ((a.min_lng+a.max_lng)/2) <= :max_lng " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || 'R20-' || b.ik_number || '-' || b.member_id) " +
            "LIKE LOWER(:search)")
    public abstract DataSource.Factory<Integer, HGFieldListRecyclerModel> getFieldListByGridHG(String staff_id,
                                                                                           double min_lat,
                                                                                           double max_lat,
                                                                                           double min_lng,
                                                                                           double max_lng,
                                                                                           String search);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE a.unique_field_id = :unique_field_id AND LOWER(b.ik_number) LIKE LOWER(:ik_number) " +
            "AND LOWER('R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:member_id) " +
            "AND LOWER(b.first_name || ' ' || b.last_name) LIKE LOWER(:member_name) " +
            "AND LOWER(b.village_name) LIKE LOWER(:village) " +
            "AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || 'R20-' || b.ik_number || '-' || b.member_id) " +
            "LIKE LOWER(:search) ")
    public abstract DataSource.Factory<Integer, HGFieldListRecyclerModel> getFieldListByActivitySearchHG(String staff_id,
                                                                                                     String unique_field_id,
                                                                                                     String ik_number,
                                                                                                     String member_id,
                                                                                                     String member_name,
                                                                                                     String village,
                                                                                                     String search);

    @Query(" SELECT a.unique_field_id, a.min_lat, a.max_lat, a.min_lng, a.max_lng, a.field_size, " +
            "b.first_name || ' ' || b.last_name as member_name, b.phone_number, b.village_name, " +
            "'R20-' || b.ik_number || '-' || b.member_id as field_r_id " +
            "FROM fields a LEFT OUTER JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE a.unique_field_id = :unique_field_id AND LOWER(b.ik_number) LIKE LOWER(:ik_number) " +
            "AND LOWER('R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:member_id) " +
            "AND LOWER(b.first_name || ' ' || b.last_name) LIKE LOWER(:member_name) " +
            "AND LOWER(b.village_name) LIKE LOWER(:village) AND LOWER(a.staff_id || a.mss) LIKE LOWER(:staff_id) ")
    public abstract DataSource.Factory<Integer, HGFieldListRecyclerModel> getFieldListByActivitySearchHG(String staff_id,
                                                                                                     String unique_field_id,
                                                                                                     String ik_number,
                                                                                                     String member_id,
                                                                                                     String member_name,
                                                                                                     String village);

    @Query("DELETE FROM fields WHERE staff_id = :staff_id")
    public abstract void deleteRecords(String staff_id);

    /**
     * Insert the object in database
     * @param fields, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Fields fields);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<Fields> fields);

    /**
     * update the object in database
     * @param fields, object to be updated
     */
    @Update
    public abstract void update(Fields fields);

    /**
     * delete the object from database
     * @param fields, object to be deleted
     */
    @Delete
    public abstract void delete(Fields fields);



}
