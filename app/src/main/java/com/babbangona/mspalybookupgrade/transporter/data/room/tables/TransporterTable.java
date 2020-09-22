package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transporter_table")
public class TransporterTable {

    @PrimaryKey
    @NonNull
    private String phone_number;
    private String first_name;
    private String last_name;
    private String vehicle_type;
    private String payment_option;
    private String bg_card;
    private Integer invalid_card_flag;
    private String account_number;
    private String account_name;
    private Integer account_mismatch_flag;
    private String bank_name;
    private String template;
    private Integer facial_capture_flag;
    private String staff_id;
    private String reg_date;
    private String date_updated;
    private Integer sync_flag;

    public TransporterTable(@NonNull String phone_number, String first_name, String last_name, String vehicle_type, String payment_option, String bg_card, Integer invalid_card_flag, String account_number, String account_name, Integer account_mismatch_flag, String bank_name, String template, Integer facial_capture_flag, String staff_id, String reg_date, String date_updated, Integer sync_flag) {
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.vehicle_type = vehicle_type;
        this.payment_option = payment_option;
        this.bg_card = bg_card;
        this.invalid_card_flag = invalid_card_flag;
        this.account_number = account_number;
        this.account_name = account_name;
        this.account_mismatch_flag = account_mismatch_flag;
        this.bank_name = bank_name;
        this.template = template;
        this.facial_capture_flag = facial_capture_flag;
        this.staff_id = staff_id;
        this.reg_date = reg_date;
        this.date_updated = date_updated;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getPhone_number() {
        return phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public String getBg_card() {
        return bg_card;
    }

    public Integer getInvalid_card_flag() {
        return invalid_card_flag;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public Integer getAccount_mismatch_flag() {
        return account_mismatch_flag;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getTemplate() {
        return template;
    }

    public Integer getFacial_capture_flag() {
        return facial_capture_flag;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public Integer getSync_flag() {
        return sync_flag;
    }

    /**
     * Tuple for sync_up response
     */

    public static class Response {
        public String phone_number;
        public Integer sync_flag;
    }
}
