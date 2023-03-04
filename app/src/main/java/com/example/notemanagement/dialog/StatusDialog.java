package com.example.notemanagement.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagement.R;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.DialogAddPriorityBinding;
import com.example.notemanagement.databinding.DialogAddStatusBinding;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.ui.priority.PriorityViewModel;
import com.example.notemanagement.ui.status.StatusViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatusDialog extends DialogFragment implements View.OnClickListener{
    private DialogAddStatusBinding binding;
    private StatusViewModel statusViewModel;
    private final String Edit = "Edit";
    private final String Add = "Add";

    public static StatusDialog newInstance(){
        return new StatusDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState){
        binding = DialogAddStatusBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();
        setCancelable(false);

        return view;
    }

    public void init(){
        if (DataLocalManager.getCheckEdit()){
            binding.dialogStatusBtnAdd.setText(Edit);
            binding.dialogStatusEtStatus.setText(DataLocalManager.getStatusName());
        }

        DataLocalManager.setCheckEdit(false);

        binding.dialogStatusBtnAdd.setOnClickListener(this);
        binding.dialogStatusBtnClose.setOnClickListener(this);

        statusViewModel = new ViewModelProvider(getActivity()).get(StatusViewModel.class);
    }

    public boolean checkInput(){

        if (TextUtils.isEmpty(binding.dialogStatusEtStatus.getText().toString().trim())){
            binding.dialogStatusEtStatus.setError("Require!!");
            return false;
        }

        return true;
    }

    private void addStatus(){
        if(!checkInput())
            return;

        statusViewModel.addStatus(binding.dialogStatusEtStatus.getText()
                .toString()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.SUCCESSFULLY){
                        Toast.makeText(getContext(), getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                        statusViewModel.refreshData();
                        dismiss();
                    } else {
                        binding.dialogStatusEtStatus.setError(getString(R.string.invalid_name));
                        Toast.makeText(getContext(), getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    public void editStatus(){
        if(!checkInput())
            return;

        statusViewModel.updateStatus(DataLocalManager.getStatusName(),
                binding.dialogStatusEtStatus.getText()
                        .toString()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getStatus() == Constants.SUCCESSFULLY){
                        statusViewModel.refreshData();
                        Toast.makeText(getContext(), getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
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
            case R.id.dialog_status_btn_add:
                if (TextUtils.equals(binding.dialogStatusBtnAdd.getText().toString(), Add))
                    addStatus();
                else
                    editStatus();
                break;
            case R.id.dialog_status_btn_close:
                dismiss();
                break;
        }
    }
}
