package com.babbangona.mspalybookupgrade.HarvestSummary.retrofit;

import com.babbangona.mspalybookupgrade.HarvestSummary.data.entities.CollectionCenterEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RettofitInterface {

    @GET("downloadHarvestSummary")
    Call<List<CollectionCenterEntity>> downloadHarvestSummary(@Query("staff_id") String staff_id, @Query("last_synced_time") String last_synced_time);
}
