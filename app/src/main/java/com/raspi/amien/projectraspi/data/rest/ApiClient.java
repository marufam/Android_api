package com.raspi.amien.projectraspi.data.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amien on 01/05/17.
 */

public class ApiClient {
    public static final String BASE_URL = "http://192.168.43.126/raspi_api/";

    private static Retrofit retrofit = null;
    public static Retrofit GetTools() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
//    public static ApiInterface getApiKaryawan(){
//        return GetTools().create(ApiInterface.class);
//    }

    private static Retrofit retrofit1 = null;
    public static Retrofit GetGuests() {
        if (retrofit1==null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }

    private static Retrofit retrofit2 = null;
    public static Retrofit GetGuests2() {
        if (retrofit2==null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
//    public static ApiInterface getApiGuest(){
//        return GetTools().create(ApiInterface.class);
//    }


}
