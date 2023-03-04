package com.example.notemanagement.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagement.R;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.DialogAddCategoryBinding;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.ui.category.CategoryViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDialog extends DialogFragment implements View.OnClickListener {
    private DialogAddCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private final String Edit = "Edit";
    private final String Add = "Add";

    public static CategoryDialog newInstance(){
        return new CategoryDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState){
        binding = DialogAddCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();
        setCancelable(false);

        return view;
    }

    public void init(){
        if (DataLocalManager.getCheckEdit()){
            binding.dialogCategoryBtnAdd.setText(Edit);
            binding.dialogCategoryEtCategory.setText(DataLocalManager.getCategoryName());
        }else {
            binding.dialogCategoryBtnAdd.setText(Add);
        }

        DataLocalManager.setCheckEdit(false);

        binding.dialogCategoryBtnAdd.setOnClickListener(this);
        binding.dialogCategoryBtnClose.setOnClickListener(this);

        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
    }

    public boolean checkInput(){
        if (TextUtils.isEmpty(binding.dialogCategoryEtCategory.getText().toString().trim())){
            binding.dialogCategoryEtCategory.setError("Require!!");
            return false;
        }

        return true;
    }

    private void addCategory(){
        if (!checkInput())
            return;

        categoryViewModel.addCategory(binding.dialogCategoryEtCategory.getText()
                        .toString().trim())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                        if (response.body() != null){
                            if (response.body().getStatus() == Constants.SUCCESSFULLY){
                                Toast.makeText(getContext(), getString(R.string.add_success),
                                        Toast.LENGTH_LONG).show();
                                categoryViewModel.refreshData();
                                dismiss();
                            } else {
                                binding.dialogCategoryEtCategory.setError(getString(R.string.invalid_name));
                                Toast.makeText(getContext(), getString(R.string.add_fail), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                    }
                });
    }

    public void editCategory(){
        if (!checkInput())
            return;

        categoryViewModel.updateCategory(DataLocalManager.getCategoryName(),
                binding.dialogCategoryEtCategory.getText().toString())
                .enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();

                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.SUCCESSFULLY){
                        Toast.makeText(getContext(), getString(R.string.edit_success),
                                Toast.LENGTH_SHORT).show();
                        categoryViewModel.refreshData();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_category_btn_add:
                if (TextUtils.equals(binding.dialogCategoryBtnAdd.getText().toString(), Add))
                    addCategory();
                else
                    editCategory();
                break;
            case R.id.dialog_category_btn_close:
                dismiss();
                break;
        }
    }
}
