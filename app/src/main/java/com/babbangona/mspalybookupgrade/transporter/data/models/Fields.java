package com.babbangona.mspalybookupgrade.transporter.data.models;

public class Fields {

    private String uniqueMemberId;
    private String uniqueFieldId;
    private Float fieldSize;

    public Fields(String uniqueMemberId, String uniqueFieldId, Float fieldSize) {
        this.uniqueMemberId = uniqueMemberId;
        this.uniqueFieldId = uniqueFieldId;
        this.fieldSize = fieldSize;
    }

    public String getUniqueMemberId() {
        return uniqueMemberId;
    }

    public String getUniqueFieldId() {
        return uniqueFieldId;
    }

    public Float getFieldSize() {
        return fieldSize;
    }
}
