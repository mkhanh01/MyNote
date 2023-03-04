package com.example.notemanagement.repository;

import android.util.Log;

import com.example.notemanagement.api.APIClient;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Detail;
import com.example.notemanagement.service.DetailService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRepository {

    private DetailService mDetailService;

    public DetailRepository(){
        mDetailService = getDetailService();
    }

    public static DetailService getDetailService(){
        return APIClient.getClient().create(DetailService.class);
    }

    public RefreshLiveData <List<Detail>> loadAllDetails(String tab){
        final RefreshLiveData<List<Detail>> liveData = new RefreshLiveData<>((callback) ->{
            mDetailService.getDetail(tab, DataLocalManager.getEmail()).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    BaseResponse baseResponse = response.body();

                    if (baseResponse != null){
                        List<Detail> categories = new ArrayList<>();

                        for (List<String> category: baseResponse.getData()) {
                            categories.add(new Detail(category.get(0), category.get(1), category.get(2)));
                        }
                        callback.onDataLoaded(categories);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        });

        return liveData;
    }

    public Call<BaseResponse> addDetail(String tab, String nameDetail){
        return mDetailService.addDetail(tab, DataLocalManager.getEmail(), nameDetail);
    }

    public Call<BaseResponse> updateDetail(String tab, String nameDetail, String newNameDetail){
        return mDetailService.updateDetail(tab, DataLocalManager.getEmail(), nameDetail, newNameDetail);
    }

    public Call<BaseResponse> deleteDetail(String tab, String nameDetail){
        return mDetailService.deleteDetail(tab, DataLocalManager.getEmail(), nameDetail);
    }

}
