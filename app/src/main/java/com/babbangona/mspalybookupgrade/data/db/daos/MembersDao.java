package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler.MemberListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;

import java.util.List;

@Dao
public abstract class MembersDao {

    @Query(" SELECT COUNT(unique_field_id) FROM fields WHERE deactivate = '0' " +
            "AND staff_id = :staff_id ")
    public abstract int getTotalFieldsCount(String staff_id);

    @Query(" SELECT template FROM members WHERE unique_member_id = :unique_member_id ")
    public abstract String getMemberTemplate(String unique_member_id);

    //String unique_member_id, String member_name, String role, String village, String ik_number, String member_r_id

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, a.staff_id," +
            "b.village_name as village, b.ik_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM fields a JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE a.mss = :mss AND a.deactivate = '0' ")
    public abstract DataSource.Factory<Integer, MemberListRecyclerModel> getMemberListByCoach(String mss);

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, " +
            "b.village_name as village, b.ik_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM fields a JOIN members b ON a.unique_member_id = b.unique_member_id " +
            "WHERE a.mss = :mss AND LOWER(b.first_name || ' ' || b.last_name || b.phone_number || " +
            "b.village_name || b.ik_number || 'R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:search) AND a.deactivate = '0' ")
    public abstract DataSource.Factory<Integer, MemberListRecyclerModel> getMemberListBySearch(String mss, String search);

    /**
     * Insert the object in database
     * @param members, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Members members);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<Members> members);

    /**
     * update the object in database
     * @param members, object to be updated
     */
    @Update
    public abstract void update(Members members);

    /**
     * delete the object from database
     * @param members, object to be deleted
     */
    @Delete
    public abstract void delete(Members members);



}
