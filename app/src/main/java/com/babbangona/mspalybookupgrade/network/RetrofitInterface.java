package com.babbangona.mspalybookupgrade.network;

import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;
import com.babbangona.mspalybookupgrade.network.object.MsPlaybookInputDownload;
import com.babbangona.mspalybookupgrade.network.object.StaffListDownload;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    /**
     * This is the interface class that decides
     * */

    @GET("downloadActivityList")
    Call<ActivityListDownload> getActivityListDownload(@Query("last_synced_time") String last_synced_time);

    @GET("downloadStaffList")
    Call<StaffListDownload> getStaffListDownload(@Query("last_synced_time") String last_synced_time);

    @GET("downloadMsPlaybookInputData")
    Call<MsPlaybookInputDownload> getMsPlaybookInputDataDownload(@Query("staff_id") String staff_id,
                                                                 @Query("portfolio_list") String portfolio_list,
                                                                 @Query("last_synced_time") String last_synced_time);

    @FormUrlEncoded
    @POST("insert")
    Call<List<String>> sync1(@Field("field1") String tgl_data);

    @FormUrlEncoded
    @POST("yourPHPName.php")
    Call<String> sync2();
}
