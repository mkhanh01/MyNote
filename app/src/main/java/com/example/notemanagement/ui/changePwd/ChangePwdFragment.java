package com.example.notemanagement.ui.changePwd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.notemanagement.AccountViewModel;
import com.example.notemanagement.R;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.FragmentChangePwdBinding;
import com.example.notemanagement.model.Account;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePwdFragment extends Fragment implements View.OnClickListener{

    private FragmentChangePwdBinding binding;
    private AccountViewModel accountViewModel;
    public static ChangePwdFragment newInstance() {

        return new ChangePwdFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePwdBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);

        binding.fragmentChangePwdBtnUpdate.setOnClickListener(this);

        return view;
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(binding.fragmentChangePwdEtCurrentPwd.getText().toString().trim())){
            binding.fragmentChangePwdEtCurrentPwd.setError(getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.fragmentChangePwdEtNewPwd.getText().toString().trim())){
            binding.fragmentChangePwdEtNewPwd.setError(getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.fragmentChangePwdEtConfirmNewPwd.getText().toString().trim())){
            binding.fragmentChangePwdEtConfirmNewPwd.setError(getString(R.string.require));
            return false;
        }

        if(!TextUtils.equals(binding.fragmentChangePwdEtNewPwd.getText().toString().trim(),
                binding.fragmentChangePwdEtConfirmNewPwd.getText().toString().trim())){
            binding.fragmentChangePwdEtConfirmNewPwd.setError(getString(R.string.password_error));
            return false;
        }

        return true;
    }

    private void changePassword(){
        if(!checkInput())
            return;

        final Account account = new Account(DataLocalManager.getEmail(),
                binding.fragmentChangePwdEtCurrentPwd.getText().toString().trim());

        accountViewModel.changePassword(account,
                        binding.fragmentChangePwdEtConfirmNewPwd.getText().toString().trim())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        BaseResponse baseResponse = response.body();

                        if(baseResponse != null){
                            if(baseResponse.getStatus() == Constants.ERROR){
                                Toast.makeText(getContext(), R.string.change_password_error,Toast.LENGTH_LONG).show();
                                binding.fragmentChangePwdEtCurrentPwd.setError(getString(R.string.password_error));
                            } else {
                                account.setPassword(binding.fragmentChangePwdEtCurrentPwd
                                        .getText().toString().trim());

                                Toast.makeText(getContext(), R.string.change_password_successfully,
                                        Toast.LENGTH_LONG).show();
                                DataLocalManager.setPassword(account.getPassword());
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
            case R.id.fragment_changePwd_btn_update:
                changePassword();
                break;

        }
    }
}