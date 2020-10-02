package com.babbangona.mspalybookupgrade.transporter.data.retrofit;

import com.babbangona.mspalybookupgrade.transporter.data.models.ImageResponse;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CardsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.FavouritesTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TempTransporterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TpoLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitApiCalls {

    @GET("downloadTransporterTable")
    Call<List<TransporterTable>> syncDownTransporterTable(@Query("last_sync_time") String last_sync_time);

    @GET("downloadOperatingAreas")
    Call<List<OperatingAreasTable>> syncDownOperatingAreas(@Query("last_sync_time") String last_sync_time);

    @GET("downloadCollectionCenters")
    Call<List<CollectionCenterTable>> syncDownCC(@Query("last_sync_time") String last_sync_time);

    @GET("downloadTransporterCards")
    Call<List<CardsTable.Download>> syncDownCards(@Query("last_sync_time") String last_sync_time);

    @GET("downloadCoachLogs")
    Call<List<CoachLogsTable.Download>> syncDownCoachLogs(@Query("last_sync_time") String last_sync_time);

    @GET("downloadTpoLogs")
    Call<List<TpoLogsTable.Download>> syncDownTpoLogs(@Query("last_sync_time") String last_sync_time);

    @GET("downloadFavourites")
    Call<List<FavouritesTable.Download>> syncDownFavourites(@Query("last_sync_time") String last_sync_time, @Query("staff_id") String staff_id);

    @FormUrlEncoded
    @POST("uploadTransporterTable")
    Call<List<TransporterTable.Response>> syncUpTransporterTable(@Field("transporter_list") String list);

    @FormUrlEncoded
    @POST("uploadOperatingAreas")
    Call<List<OperatingAreasTable.Response>> syncUpOperatingAreasTable(@Field("operating_areas_list") String list);

    @FormUrlEncoded
    @POST("uploadCoachLogs")
    Call<List<CoachLogsTable.Response>> syncUpCoachLogsTable(@Field("coach_logs_list") String list);

    @FormUrlEncoded
    @POST("uploadTpoLogs")
    Call<List<TpoLogsTable.Response>> syncUpTpoLogsTable(@Field("tpo_logs_list") String list);

    @FormUrlEncoded
    @POST("uploadFavourites")
    Call<ResponseBody> syncUpFavourites(@Field("favourites_list") String list);

    @FormUrlEncoded
    @POST("uploadTempTransporterTable")
    Call<List<TempTransporterTable.Response>> syncUpTempTransporters(@Field("temp_transporters_list") String list);

    @Multipart
    @POST("uploadImages")
    Call<ImageResponse> uploadTransporterImages(@Part MultipartBody.Part file, @Part("file") RequestBody name);

}
