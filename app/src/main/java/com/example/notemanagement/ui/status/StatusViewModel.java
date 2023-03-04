package com.example.notemanagement.ui.status;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Detail;
import com.example.notemanagement.repository.DetailRepository;

import java.util.List;

import retrofit2.Call;

public class StatusViewModel extends AndroidViewModel {

    private DetailRepository detailRepository;
    private RefreshLiveData<List<Detail>> mStatusList;

    public StatusViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init(){
        detailRepository = new DetailRepository();
        mStatusList = detailRepository.loadAllDetails(Constants.TAB_STATUS);
    }

    public void refreshData(){
        mStatusList.refresh();
    }

    public LiveData<List<Detail>> getStatusList(){
        return mStatusList;
    }

    public Call<BaseResponse> addStatus(String nameStatus){
        return detailRepository.addDetail(Constants.TAB_STATUS,nameStatus);
    }

    public Call<BaseResponse> updateStatus(String nameStatus, String newNameStatus){
        return detailRepository.updateDetail(Constants.TAB_STATUS, nameStatus, newNameStatus);
    }

    public Call<BaseResponse> deleteStatus(String nameStatus){
        return detailRepository.deleteDetail(Constants.TAB_STATUS, nameStatus);
    }
}