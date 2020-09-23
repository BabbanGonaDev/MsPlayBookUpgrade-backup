package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "tpo_logs_table", primaryKeys = {"member_id", "quantity", "transporter_id", "date_logged"})
public class TpoLogsTable {

    @NonNull
    private String member_id;
    @NonNull
    private Integer quantity;
    private String transported_by;
    @NonNull
    private String transporter_id;
    private String voucher_id;
    private Integer voucher_id_flag;
    private String cc_id;
    private Integer instant_payment_flag;
    @NonNull
    private String date_logged;
    private String sync_flag;

    public TpoLogsTable(@NonNull String member_id, @NonNull Integer quantity, String transported_by, @NonNull String transporter_id, String voucher_id, Integer voucher_id_flag, String cc_id, Integer instant_payment_flag, @NonNull String date_logged, String sync_flag) {
        this.member_id = member_id;
        this.quantity = quantity;
        this.transported_by = transported_by;
        this.transporter_id = transporter_id;
        this.voucher_id = voucher_id;
        this.voucher_id_flag = voucher_id_flag;
        this.cc_id = cc_id;
        this.instant_payment_flag = instant_payment_flag;
        this.date_logged = date_logged;
        this.sync_flag = sync_flag;
    }

    @NonNull
    public String getMember_id() {
        return member_id;
    }

    @NonNull
    public Integer getQuantity() {
        return quantity;
    }

    public String getTransported_by() {
        return transported_by;
    }

    @NonNull
    public String getTransporter_id() {
        return transporter_id;
    }

    public String getVoucher_id() {
        return voucher_id;
    }

    public Integer getVoucher_id_flag() {
        return voucher_id_flag;
    }

    public String getCc_id() {
        return cc_id;
    }

    public Integer getInstant_payment_flag() {
        return instant_payment_flag;
    }

    @NonNull
    public String getDate_logged() {
        return date_logged;
    }

    public String getSync_flag() {
        return sync_flag;
    }
}
