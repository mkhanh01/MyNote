package com.example.notemanagement.service;

import com.example.notemanagement.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardService {

    @GET("get")
    Call<BaseResponse> getDashboard(@Query("tab") String tab, @Query("email") String email);

}
