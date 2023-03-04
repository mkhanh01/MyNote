package com.example.notemanagement.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Dashboard;
import com.example.notemanagement.repository.DashboardRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private DashboardRepository dashboardRepository;
    private RefreshLiveData<List<Dashboard>> dashboardData;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        dashboardRepository = new DashboardRepository();
        dashboardData = dashboardRepository.loadAllDashboard(Constants.TAB_DASHBOARD);
    }

    public void refreshData(){
        dashboardData.refresh();
    }

    public RefreshLiveData<List<Dashboard>> getDashboardLiveData() {
        return dashboardData;
    }

}