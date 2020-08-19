package com.babbangona.mspalybookupgrade.network;

import com.babbangona.mspalybookupgrade.data.db.entities.PWSCategoryList;
import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;
import com.babbangona.mspalybookupgrade.network.object.AppVariablesDownload;
import com.babbangona.mspalybookupgrade.network.object.CategoryDownload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.HGListDownload;
import com.babbangona.mspalybookupgrade.network.object.HarvestLocationDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsUpload;
import com.babbangona.mspalybookupgrade.network.object.MsPlaybookInputDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.PCPWSActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.PCPWSActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.PWSActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.PWSActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.PWSCategoryListDownload;
import com.babbangona.mspalybookupgrade.network.object.RFActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.RFActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.RFListDownload;
import com.babbangona.mspalybookupgrade.network.object.ServerResponse;
import com.babbangona.mspalybookupgrade.network.object.StaffListDownload;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitInterface {

    /**
     * This is the interface class that decides
     * */

    @GET("downloadActivityList")
    Call<ActivityListDownload> getActivityListDownload(@Query("last_synced_time") String last_synced_time);

    @GET("downloadHGList")
    Call<HGListDownload> getHGListDownload(@Query("last_synced_time") String last_synced_time);

    @GET("downloadStaffList")
    Call<StaffListDownload> getStaffListDownload(@Query("last_synced_time") String last_synced_time);

    @GET("downloadMsPlaybookInputData")
    Call<MsPlaybookInputDownload> getMsPlaybookInputDataDownload(@Query("staff_id") String staff_id,
                                                                 @Query("portfolio_list") String portfolio_list,
                                                                 @Query("last_synced_time") String last_synced_time);

    @GET("downloadLogs")
    Call<LogsDownload> downloadLogs(@Query("staff_id") String staff_id,
                                    @Query("portfolio_list") String portfolio_list,
                                    @Query("last_synced_time") String last_synced_time);

    @GET("downloadHGActivityFlag")
    Call<HGActivitiesFlagDownload> downloadHGActivityFlag(@Query("staff_id") String staff_id,
                                                          @Query("portfolio_list") String portfolio_list,
                                                          @Query("last_synced_time") String last_synced_time);

    @GET("downloadNormalActivityFlag")
    Call<NormalActivitiesFlagDownload> downloadNormalActivityFlag(@Query("staff_id") String staff_id,
                                                                  @Query("portfolio_list") String portfolio_list,
                                                                  @Query("last_synced_time") String last_synced_time);

    @GET("downloadCategory")
    Call<CategoryDownload> downloadCategory(@Query("last_synced_time") String last_synced_time);

    @GET("downloadHarvestLocation")
    Call<HarvestLocationDownload> downloadHarvestLocation(@Query("last_synced_time") String last_synced_time);

    @GET("downloadMsPlaybookAppVariables")
    Call<AppVariablesDownload> downloadAppVariables(@Query("staff_id") String staff_id);

    @FormUrlEncoded
    @POST("uploadLogsRecord")
    Call<List<LogsUpload>> uploadLogsRecord(@Field("upload_list") String upload_list, @Field("staff_id") String staff_id);

    @FormUrlEncoded
    @POST("uploadHGActivitiesRecord")
    Call<List<HGActivitiesUpload>> uploadHGActivitiesRecord(@Field("upload_list") String upload_list, @Field("staff_id") String staff_id);

    @FormUrlEncoded
    @POST("uploadNormalActivitiesRecord")
    Call<List<NormalActivitiesUpload>> uploadNormalActivitiesRecord(@Field("upload_list") String upload_list, @Field("staff_id") String staff_id);

    @GET("downloadRFList")
    Call<RFListDownload> getRFListDownload(@Query("last_synced_time") String last_synced_time);

    @GET("downloadRFActivityFlag")
    Call<RFActivitiesFlagDownload> downloadRFActivityFlag(@Query("staff_id") String staff_id,
                                                          @Query("portfolio_list") String portfolio_list,
                                                          @Query("last_synced_time") String last_synced_time);

    @FormUrlEncoded
    @POST("uploadRFActivitiesRecord")
    Call<List<RFActivitiesUpload>> uploadRFActivitiesRecord(@Field("upload_list") String upload_list, @Field("staff_id") String staff_id);

    @Multipart
    @POST("syncMsPlaybookPicture")
    Call <ServerResponse> uploadMsPlaybookPicture(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @GET("downloadPWSCategoryList")
    Call<PWSCategoryListDownload> getPWSCategoryList(@Query("last_synced_time") String last_synced_time);

    @FormUrlEncoded
    @POST("uploadPWSActivitiesRecord")
    Call<List<PWSActivitiesUpload>> uploadPWSActivitiesRecord(@Field("upload_list") String upload_list, @Field("staff_id") String staff_id);

    @FormUrlEncoded
    @POST("uploadPCPWSActivitiesRecord")
    Call<List<PCPWSActivitiesUpload>> uploadPCPWSActivitiesRecord(@Field("upload_list") String upload_list, @Field("staff_id") String staff_id);

    @GET("downloadPWSActivityFlag")
    Call<PWSActivitiesFlagDownload> downloadPWSActivityFlag(@Query("staff_id") String staff_id,
                                                            @Query("portfolio_list") String portfolio_list,
                                                            @Query("last_synced_time") String last_synced_time);

    @GET("downloadPCPWSActivityFlag")
    Call<PCPWSActivitiesFlagDownload> downloadPCPWSActivityFlag(@Query("staff_id") String staff_id,
                                                              @Query("portfolio_list") String portfolio_list,
                                                              @Query("last_synced_time") String last_synced_time);

    @FormUrlEncoded
    @POST("yourPHPName.php")
    Call<String> sync2();
}
