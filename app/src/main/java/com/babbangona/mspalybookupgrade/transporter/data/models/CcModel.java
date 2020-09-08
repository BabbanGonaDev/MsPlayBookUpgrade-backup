package com.babbangona.mspalybookupgrade.transporter.data.models;

/**
 * This model class is used to add "isSelected" attribute to the Collection Center data.
 * So as to be able to track when a collection center has been selected.
 */

public class CcModel {

    private String cc_id;
    private String state;
    private String lga;
    private String cc_name;
    private String date_updated;
    private Boolean is_selected;

    public CcModel(String cc_id, String state, String lga, String cc_name, String date_updated) {
        this.cc_id = cc_id;
        this.state = state;
        this.lga = lga;
        this.cc_name = cc_name;
        this.date_updated = date_updated;
        this.is_selected = false;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }
}
