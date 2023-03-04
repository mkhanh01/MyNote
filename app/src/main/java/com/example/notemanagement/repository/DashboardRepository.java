package com.example.notemanagement.repository;

import com.example.notemanagement.api.APIClient;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Dashboard;
import com.example.notemanagement.service.DashboardService;
import com.example.notemanagement.service.DetailService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    private DashboardService dashboardService;

    public DashboardRepository(){
        dashboardService = getDashboardService();
    }

    public static DashboardService getDashboardService(){
        return APIClient.getClient().create(DashboardService.class);
    }

    public RefreshLiveData<List<Dashboard>> loadAllDashboard(String tab){
        final RefreshLiveData<List<Dashboard>> liveData = new RefreshLiveData<>((callback) -> {
            dashboardService.getDashboard(tab, DataLocalManager.getEmail()).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null){
                        List<Dashboard> dashboards = new ArrayList<>();

                        for (List<String> dashboard: response.body().getData()) {
                            dashboards.add(new Dashboard(dashboard.get(0), dashboard.get(1)));
                        }
                        callback.onDataLoaded(dashboards);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        });

        return liveData;
    }


}
