package com.babbangona.mspalybookupgrade.HarvestSummary.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "collection_center_member_info", primaryKeys = {"unique_member_id"})
public class CollectionCenterEntity {

    @NonNull
    @ColumnInfo(name = "unique_member_id")
    @SerializedName("unique_member_id")
    private String unique_member_id;

    @NonNull
    @ColumnInfo(name = "ik_number")
    @SerializedName("ik_number")
    private String ik_number;

    @ColumnInfo(name = "no_of_bags_marketed")
    @SerializedName("no_of_bags_marketed")
    private String no_of_bags_marketed;

    @ColumnInfo(name = "no_of_bags_transported")
    @SerializedName("no_of_bags_transported")
    private String no_of_bags_transported;

    @ColumnInfo(name = "expected_bags")
    @SerializedName("expected_bags")
    private String expected_bags;

    @ColumnInfo(name = "harvest_complete")
    @SerializedName("harvest_complete")
    private String harvest_complete;

    @ColumnInfo(name = "payment_status")
    @SerializedName("payment_status")
    private String payment_status;

    @ColumnInfo(name = "harvest_advance_paid")
    @SerializedName("harvest_advance_paid")
    private String harvest_advance_paid;

    @ColumnInfo(name = "transporter_name")
    @SerializedName("transporter_name")
    private String transporter_name;

    @ColumnInfo(name = "transporter_phone_number")
    @SerializedName("transporter_phone_number")
    private String transporter_phone_number;

    @ColumnInfo(name = "bgt_name")
    @SerializedName("bgt_name")
    private String bgt_name;

    @NonNull
    @ColumnInfo(name = "bgt_staff_id")
    @SerializedName("bgt_staff_id")
    private String bgt_staff_id;

    public CollectionCenterEntity(@NonNull String unique_member_id, @NonNull String ik_number, String no_of_bags_marketed, String no_of_bags_transported, String expected_bags, String harvest_complete, String payment_status, String harvest_advance_paid, String transporter_name, String transporter_phone_number, String bgt_name, @NonNull String bgt_staff_id) {
        this.unique_member_id = unique_member_id;
        this.ik_number = ik_number;
        this.no_of_bags_marketed = no_of_bags_marketed;
        this.no_of_bags_transported = no_of_bags_transported;
        this.expected_bags = expected_bags;
        this.harvest_complete = harvest_complete;
        this.payment_status = payment_status;
        this.harvest_advance_paid = harvest_advance_paid;
        this.transporter_name = transporter_name;
        this.transporter_phone_number = transporter_phone_number;
        this.bgt_name = bgt_name;
        this.bgt_staff_id = bgt_staff_id;
    }

    public CollectionCenterEntity() {
    }

    @NonNull
    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(@NonNull String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    @NonNull
    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(@NonNull String ik_number) {
        this.ik_number = ik_number;
    }

    public String getNo_of_bags_marketed() {
        return no_of_bags_marketed;
    }

    public void setNo_of_bags_marketed(String no_of_bags_marketed) {
        this.no_of_bags_marketed = no_of_bags_marketed;
    }

    public String getNo_of_bags_transported() {
        return no_of_bags_transported;
    }

    public void setNo_of_bags_transported(String no_of_bags_transported) {
        this.no_of_bags_transported = no_of_bags_transported;
    }

    public String getExpected_bags() {
        return expected_bags;
    }

    public void setExpected_bags(String expected_bags) {
        this.expected_bags = expected_bags;
    }

    public String getHarvest_complete() {
        return harvest_complete;
    }

    public void setHarvest_complete(String harvest_complete) {
        this.harvest_complete = harvest_complete;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getHarvest_advance_paid() {
        return harvest_advance_paid;
    }

    public void setHarvest_advance_paid(String harvest_advance_paid) {
        this.harvest_advance_paid = harvest_advance_paid;
    }

    public String getTransporter_name() {
        return transporter_name;
    }

    public void setTransporter_name(String transporter_name) {
        this.transporter_name = transporter_name;
    }

    public String getTransporter_phone_number() {
        return transporter_phone_number;
    }

    public void setTransporter_phone_number(String transporter_phone_number) {
        this.transporter_phone_number = transporter_phone_number;
    }

    public String getBgt_name() {
        return bgt_name;
    }

    public void setBgt_name(String bgt_name) {
        this.bgt_name = bgt_name;
    }

    public String getBgt_staff_id() {
        return bgt_staff_id;
    }

    public void setBgt_staff_id(String bgt_staff_id) {
        this.bgt_staff_id = bgt_staff_id;
    }
}
