package com.example.notemanagement.service;

import com.example.notemanagement.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DetailService {
    @GET("get")
    Call<BaseResponse> getDetail(@Query("tab") String tab, @Query("email") String email);

    @GET("add")
    Call<BaseResponse> addDetail(@Query("tab") String tab, @Query("email") String email,
                                 @Query("name") String name);

    @GET("update")
    Call<BaseResponse> updateDetail(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name, @Query("nname") String nname);

    @GET("del")
    Call<BaseResponse> deleteDetail(@Query("tab") String tab, @Query("email") String email,
                                    @Query("name") String name);


}
