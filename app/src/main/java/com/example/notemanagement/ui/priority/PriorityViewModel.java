package com.example.notemanagement.ui.priority;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Detail;
import com.example.notemanagement.repository.DetailRepository;

import java.util.List;

import retrofit2.Call;

public class PriorityViewModel extends AndroidViewModel {
    private DetailRepository detailRepository;
    private RefreshLiveData<List<Detail>> mPriorityList;

    public PriorityViewModel(@NonNull Application application) {
        super(application);
        init();
    }
    public void init(){
        detailRepository = new DetailRepository();
        mPriorityList = detailRepository.loadAllDetails(Constants.TAB_PRIORITY);
    }

    public void refreshData(){
        mPriorityList.refresh();
    }

    public LiveData<List<Detail>> getPriorityList(){
        return mPriorityList;
    }

    public Call<BaseResponse> addPriority(String namePriority){
        return detailRepository.addDetail(Constants.TAB_PRIORITY,namePriority);
    }

    public Call<BaseResponse> updatePriority(String namePriority, String newNamePriority){
        return detailRepository.updateDetail(Constants.TAB_PRIORITY, namePriority, newNamePriority);
    }

    public Call<BaseResponse> deletePriority(String namePriority){
        return detailRepository.deleteDetail(Constants.TAB_PRIORITY, namePriority);
    }
}