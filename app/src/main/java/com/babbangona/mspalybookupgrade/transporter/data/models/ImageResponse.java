package com.babbangona.mspalybookupgrade.transporter.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResponse {

    @SerializedName("file_name")
    @Expose
    private String file_name;

    @SerializedName("status")
    @Expose
    private String status;

    public ImageResponse(String file_name, String status) {
        this.file_name = file_name;
        this.status = status;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getStatus() {
        return status;
    }
}
