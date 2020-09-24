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

    @Query(" UPDATE members SET coach_id = 'T-10000000000000AA'")
    public abstract void updateCoach();

    //String unique_member_id, String member_name, String role, String village, String ik_number, String member_r_id

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, b.bgt_id as staff_id," +
            "b.village_name as village, b.ik_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.coach_id = :mss ")
    public abstract DataSource.Factory<Integer, MemberListRecyclerModel> getMemberListByCoach(String mss);

    @Query(" SELECT b.unique_member_id, b.first_name || ' ' || b.last_name as member_name, b.role, b.bgt_id as staff_id," +
            "b.village_name as village, b.ik_number, 'R20-' || b.ik_number || '-' || b.member_id as member_r_id " +
            "FROM members b WHERE b.coach_id = :mss AND LOWER(b.first_name || ' ' || b.last_name || b.phone_number || " +
            "b.village_name || b.ik_number || 'R20-' || b.ik_number || '-' || b.member_id) LIKE LOWER(:search) ")
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
     *
     * @param members, object to be deleted
     */
    @Delete
    public abstract void delete(Members members);

    /**
     * =============================================
     * Functions below this line are used in the Transporter application module.
     * =============================================
     */
    @Query("SELECT a.unique_member_id AS uniqueMemberId, a.first_name || ' ' || a.last_name AS fullName," +
            " a.ik_number AS ikNumber, a.member_id AS memberId, a.village_name AS villageName, SUM(b.field_size) AS fieldSize FROM members AS a" +
            " JOIN fields AS b ON a.unique_member_id = b.unique_member_id" +
            " WHERE b.deactivate = '0' GROUP BY b.unique_member_id")
    DataSource.Factory<Integer, com.babbangona.mspalybookupgrade.transporter.data.models.Members> getAllMembers();

    @Query("SELECT a.unique_member_id AS uniqueMemberId, a.first_name || ' ' || a.last_name AS fullName," +
            " a.ik_number AS ikNumber, a.member_id AS memberId, a.village_name AS villageName, SUM(b.field_size) AS fieldSize FROM members AS a" +
            " JOIN fields AS b ON a.unique_member_id = b.unique_member_id" +
            " WHERE b.deactivate = '0' AND (LOWER(a.ik_number) LIKE :filter OR LOWER(a.first_name) LIKE :filter OR LOWER(a.last_name) LIKE :filter) GROUP BY b.unique_member_id")
    DataSource.Factory<Integer, com.babbangona.mspalybookupgrade.transporter.data.models.Members> getAllMembersFilter(String filter);
}
