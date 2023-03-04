package com.example.notemanagement.service;

import com.example.notemanagement.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccountService {
    @GET("login")
    Call<BaseResponse> login(@Query("email") String email, @Query("pass") String pass);

    @GET("signup")
    Call<BaseResponse> signup(@Query("email") String email, @Query("pass") String pass,
                              @Query("firstname") String firstname,@Query("lastname") String lastname);

    @GET("update")
    Call<BaseResponse> editProfile(@Query("tab") String tab, @Query("email") String email
            , @Query("nemail") String Nemail, @Query("firstname") String firstname
            ,@Query("lastname") String lastname);

    @GET("update")
    Call<BaseResponse> changePassword(@Query("tab") String tab, @Query("email") String email
            , @Query("pass") String pass, @Query("npass") String npass);


}
