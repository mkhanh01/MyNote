package com.example.notemanagement.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagement.R;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.DialogAddPriorityBinding;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.ui.category.CategoryViewModel;
import com.example.notemanagement.ui.priority.PriorityViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityDialog extends DialogFragment implements View.OnClickListener {
    private DialogAddPriorityBinding binding;
    private PriorityViewModel priorityViewModel;
    private final String Edit = "Edit";
    private final String Add = "Add";

    public static PriorityDialog newInstance(){
        return new PriorityDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState){
        binding = DialogAddPriorityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();
        setCancelable(false);

        return view;
    }

    public void init(){
        if (DataLocalManager.getCheckEdit()){
            binding.dialogPriorityBtnAdd.setText(Edit);
            binding.dialogPriorityEtPriority.setText(DataLocalManager.getPriorityName());
        }

        DataLocalManager.setCheckEdit(false);

        binding.dialogPriorityBtnAdd.setOnClickListener(this);
        binding.dialogPriorityBtnClose.setOnClickListener(this);

        priorityViewModel = new ViewModelProvider(getActivity()).get(PriorityViewModel.class);
    }

    public boolean checkInput(){

        if (TextUtils.isEmpty(binding.dialogPriorityEtPriority.getText().toString().trim())){
            binding.dialogPriorityEtPriority.setError("Require!!");
            return false;
        }

        return true;
    }

    private void addPriority(){
        if(!checkInput())
            return;

        priorityViewModel.addPriority(binding.dialogPriorityEtPriority.getText()
                .toString()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.SUCCESSFULLY){
                        priorityViewModel.refreshData();
                        Toast.makeText(getContext(), getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        binding.dialogPriorityEtPriority.setError(getString(R.string.invalid_name));
                        Toast.makeText(getContext(), getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    public void editPriority(){
        if(!checkInput())
            return;

        priorityViewModel.updatePriority(DataLocalManager.getPriorityName(),
                binding.dialogPriorityEtPriority.getText()
                .toString()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.SUCCESSFULLY){

                        Toast.makeText(getContext(), getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
                        priorityViewModel.refreshData();
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
            case R.id.dialog_priority_btn_add:
                if (TextUtils.equals(binding.dialogPriorityBtnAdd.getText().toString(), Add))
                    addPriority();
                else
                    editPriority();
                break;
            case R.id.dialog_priority_btn_close:
                dismiss();
                break;
        }
    }
}
