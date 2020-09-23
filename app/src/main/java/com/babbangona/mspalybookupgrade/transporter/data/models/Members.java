package com.babbangona.mspalybookupgrade.transporter.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Members implements Parcelable {


    public static final Creator<Members> CREATOR = new Creator<Members>() {
        @Override
        public Members createFromParcel(Parcel in) {
            return new Members(in);
        }

        @Override
        public Members[] newArray(int size) {
            return new Members[size];
        }
    };
    private String uniqueMemberId;
    private String fullName;
    private String ikNumber;
    private String memberId;
    private String villageName;
    private Float fieldSize;


    public Members(String uniqueMemberId, String fullName, String ikNumber, String memberId, String villageName, Float fieldSize) {
        this.uniqueMemberId = uniqueMemberId;
        this.fullName = fullName;
        this.ikNumber = ikNumber;
        this.memberId = memberId;
        this.villageName = villageName;
        this.fieldSize = fieldSize;
    }

    protected Members(Parcel in) {
        uniqueMemberId = in.readString();
        fullName = in.readString();
        ikNumber = in.readString();
        memberId = in.readString();
        villageName = in.readString();

        if (in.readByte() == 0) {
            fieldSize = null;
        } else {
            fieldSize = in.readFloat();
        }
    }

    public String getUniqueMemberId() {
        return uniqueMemberId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIkNumber() {
        return ikNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getVillageName() {
        return villageName;
    }

    public Float getFieldSize() {
        return fieldSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(uniqueMemberId);
        dest.writeString(fullName);
        dest.writeString(ikNumber);
        dest.writeString(memberId);
        dest.writeString(villageName);

        if (fieldSize == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(fieldSize);
        }
    }
}
