package com.example.notemanagement.ui.editProfile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notemanagement.AccountViewModel;
import com.example.notemanagement.HomeActivity;
import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.FragmentEditProfileBinding;

import com.example.notemanagement.R;
import com.example.notemanagement.model.Account;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment implements  View.OnClickListener{
    FragmentEditProfileBinding binding;
    AccountViewModel accountViewModel;

    public static EditProfileFragment newInstance() {

        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {

        binding = FragmentEditProfileBinding.inflate(inflater, container,false);
        View view = binding.getRoot();

        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);

        binding.fragmentEditProfileEtFirstName.setText(DataLocalManager.getFirstname());
        binding.fragmentEditProfileEtLastName.setText(DataLocalManager.getLastname());
        binding.fragmentEditProfileEtEmail.setText(DataLocalManager.getEmail());

        binding.fragmentEditProfileBtnUpdate.setOnClickListener(this);
        binding.fragmentEditProfileTvHome.setOnClickListener(this);

        return view;

    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(binding.fragmentEditProfileEtFirstName.getText().toString().trim())){
            binding.fragmentEditProfileEtFirstName.setError(getResources().getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.fragmentEditProfileEtLastName.getText().toString().trim())){
            binding.fragmentEditProfileEtLastName.setError(getResources().getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.fragmentEditProfileEtEmail.getText().toString().trim())){
            binding.fragmentEditProfileEtEmail.setError(getResources().getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.fragmentEditProfileEtNewEmail.getText().toString().trim())){
            binding.fragmentEditProfileEtNewEmail.setError(getResources().getString(R.string.new_email_error));
            return false;
        }

        if(!isEmailValid(binding.fragmentEditProfileEtNewEmail.getText().toString())){
            binding.fragmentEditProfileEtNewEmail.setError(getResources().getString(R.string.email_error));
            return false;
        }

        if(!TextUtils.equals(binding.fragmentEditProfileEtEmail.getText().toString().trim(),
                DataLocalManager.getEmail())){
            binding.fragmentEditProfileEtNewEmail.setError(getResources().getString(R.string.email_error));
            return false;
        }

        return true;
    }

    private void editProfile(){
        if(!checkInput())
            return;

        final Account account = new Account(
                binding.fragmentEditProfileEtFirstName.getText().toString().trim(),
                binding.fragmentEditProfileEtLastName.getText().toString().trim(),
                binding.fragmentEditProfileEtNewEmail.getText().toString().trim());

        accountViewModel.editProfile(binding.fragmentEditProfileEtEmail.getText().toString().trim(),
                account).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if(baseResponse != null) {
                    if(baseResponse.getStatus() == Constants.SUCCESSFULLY){
                        Toast.makeText(getContext(), R.string.edit_profile_successfully, Toast.LENGTH_SHORT).show();

                        DataLocalManager.setFirstName(account.getFirstName());
                        DataLocalManager.setLastname(account.getLastName());
                        DataLocalManager.setEmail(account.getEmail());

                        callHomeActivity();
                    } else {
                        Toast.makeText(getContext(), R.string.edit_profile_error, Toast.LENGTH_SHORT).show();

                        binding.fragmentEditProfileEtEmail.setError(getString(R.string.email_error));
                        binding.fragmentEditProfileEtNewEmail.setError(getString(R.string.email_error));

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }

    private void callHomeActivity() {
        Intent intent = new Intent(getContext(), HomeActivity.class);

        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_editProfile_btn_update:
                editProfile();
                break;
            case R.id.fragment_editProfile_tv_home:
                callHomeActivity();
                break;
        }
    }
}