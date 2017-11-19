package com.raspi.amien.projectraspi.data.rest;


import com.raspi.amien.projectraspi.data.retrofit.GetGuest;
import com.raspi.amien.projectraspi.data.retrofit.GetTools;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by amien on 01/05/17.
 */

public interface ApiInterface {

    @GET("index.php/Tool_api")
    Call<GetTools> getUser();

    @FormUrlEncoded
    @POST("index.php/Tool_api")
    Call<GetTools> user_insert(@Field("no") String no,
                               @Field("status") String status);

    @GET("index.php/Guest_api")
    Call<GetGuest> getGuest();

    @FormUrlEncoded
    @GET("index.php/Guest_api")
    Call<GetGuest> guest_input(@Field("id") String id,
                               @Field("status") String status);

}
