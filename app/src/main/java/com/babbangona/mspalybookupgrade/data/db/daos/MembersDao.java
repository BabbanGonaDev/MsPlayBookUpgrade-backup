package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpHomeRecycler.FertilizerHomeRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpMembersRecycler.FertilizerMembersRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler.MemberListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.donotpay.data.models.TGList;
import com.babbangona.mspalybookupgrade.tpo.data.models.MemberModel;

import java.util.List;

@Dao
public abstract class MembersDao {

    @Query(" SELECT COUNT(unique_field_id) FROM fields WHERE deactivate = '0' " +
            "AND staff_id = :staff_id ")
    public abstract int getTotalFieldsCount(String staff_id);

    @Query(" SELECT template FROM members WHERE unique_member_id = :unique_member_id ")
    public abstract String getMemberTemplate(String unique_member_id);

    @Query(" UPDATE members SET coach_id = 'T-10000000000000AA'")
    public abstract void updateCoach();

    @Query(" UPDATE members SET bgt_id = 'T-10000000000000BB'")
    public abstract void updateMemberBgtID();

    //String unique_member_id, String member_name, String role, String village, String ik_number, String member_r_id

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, b.bgt_id as staff_id," +
            "b.village_name as village, b.phone_number, b.ik_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.coach_id = :mss ")
    public abstract DataSource.Factory<Integer, MemberListRecyclerModel> getMemberListByCoach(String mss);

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, b.bgt_id as staff_id," +
            "b.village_name as village, b.phone_number, b.ik_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.coach_id = :mss AND LOWER(b.first_name || ' ' || b.last_name || b.phone_number || " +
            "b.village_name || b.ik_number || 'R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:search) ")
    public abstract DataSource.Factory<Integer, MemberListRecyclerModel> getMemberListBySearch(String mss, String search);

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, " +
            "b.village_name as village, b.ik_number, b.phone_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.coach_id = :mss AND LOWER(b.role) = LOWER('Leader')")
    public abstract DataSource.Factory<Integer, FertilizerHomeRecyclerModel> getLeaderMemberList(String mss);

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, " +
            "b.village_name as village, b.ik_number, b.phone_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.coach_id = :mss AND LOWER(b.role) = LOWER('Leader') AND LOWER(b.first_name || ' ' || " +
            "b.last_name || b.phone_number || b.village_name || b.ik_number || 'R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:search) ")
    public abstract DataSource.Factory<Integer, FertilizerHomeRecyclerModel> getLeaderMemberListBySearch(String mss, String search);

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, " +
            "b.village_name as village, b.ik_number, b.phone_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.ik_number = :ik_number ")
    public abstract DataSource.Factory<Integer, FertilizerMembersRecyclerModel> getTrustGroupMemberList(String ik_number);

    @Query("SELECT first_name, last_name, ik_number, village_name FROM members WHERE unique_member_id = :unique_member_id")
    public abstract Members.MemberDetails getMemberDetails(String unique_member_id);


    /**
     * Get Trust Group list for Do Not Pay module
     */
    @Query("SELECT unique_member_id, ik_number, first_name, last_name, village_name FROM members WHERE role = 'Leader' GROUP by ik_number")
    public abstract LiveData<List<TGList>> getTrustGroupsList();

    /**
     * Functions below are used by the TPO application module
     */
    @Query("SELECT unique_member_id, first_name, last_name, ik_number, phone_number, coach_id FROM members")
    public abstract DataSource.Factory<Integer, MemberModel> getAllTpoMembers();

    @Query("SELECT unique_member_id, first_name, last_name, ik_number, phone_number, coach_id FROM members " +
            "WHERE LOWER(first_name) LIKE :filter OR LOWER(last_name) LIKE :filter OR LOWER(ik_number) LIKE :filter OR phone_number LIKE :filter OR coach_id LIKE :filter")
    public abstract DataSource.Factory<Integer, MemberModel> getAllTpoMembersFilter(String filter);


    /**
     * Insert the object in database
     *
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
