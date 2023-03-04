package com.example.notemanagement.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notemanagement.data.RefreshLiveData;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.model.Detail;
import com.example.notemanagement.repository.DetailRepository;

import java.util.List;

import retrofit2.Call;

public class CategoryViewModel extends AndroidViewModel {
    private DetailRepository detailRepository;
    private RefreshLiveData<List<Detail>> mCategoryList;

    public CategoryViewModel(@NonNull  Application application) {
        super(application);
        init();
    }

    public void init(){
        detailRepository = new DetailRepository();
        mCategoryList = detailRepository.loadAllDetails(Constants.TAB_CATEGORY);
    }

    public void refreshData(){
        mCategoryList.refresh();
    }

    public LiveData<List<Detail>> getCategoryList(){
        return mCategoryList;
    }

    public Call<BaseResponse> addCategory(String nameCategory){
        return detailRepository.addDetail(Constants.TAB_CATEGORY,nameCategory);
    }

    public Call<BaseResponse> updateCategory(String nameCategory, String newNameCategory){
        return detailRepository.updateDetail(Constants.TAB_CATEGORY, nameCategory, newNameCategory);
    }

    public Call<BaseResponse> deleteCategory(String nameCategory){
        return detailRepository.deleteDetail(Constants.TAB_CATEGORY, nameCategory);
    }
}