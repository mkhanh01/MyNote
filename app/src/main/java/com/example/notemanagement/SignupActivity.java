package com.example.notemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.ActivitySignupBinding;
import com.example.notemanagement.model.Account;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivitySignupBinding binding;
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding.activitySignupBtnSignup.setOnClickListener(this);
        binding.activitySignupTvLogin.setOnClickListener(this);

    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkInput(){
        if(TextUtils.isEmpty(binding.activitySignupEtFirstName.getText().toString().trim())){
            binding.activitySignupEtFirstName.setError(getResources().getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.activitySignupEtLastName.getText().toString().trim())){
            binding.activitySignupEtLastName.setError(getResources().getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.activitySignupEtEmail.getText().toString().trim())) {
            binding.activitySignupEtEmail.setError(getResources().getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.activitySignupEtPwd.getText().toString().trim())) {
            binding.activitySignupEtPwd.setError(getResources().getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.activitySignupEtConfirmPwd.getText().toString())) {
            if (!binding.activitySignupEtConfirmPwd.getText().toString().equals(binding.activitySignupEtPwd.getText().toString())) {
                binding.activitySignupEtConfirmPwd.setError(getResources().getString(R.string.compare_password_error));
                return false;
            }
            binding.activitySignupEtConfirmPwd.setError(getResources().getString(R.string.require));
            return false;
        }

        if (!isEmailValid(binding.activitySignupEtEmail.getText().toString())) {
            binding.activitySignupEtEmail.setError(getResources().getString(R.string.email_error));
            return false;
        }

        return true;
    }

    private  void signup(){
        if (!checkInput())
            return;

        final Account account = new Account(
                binding.activitySignupEtFirstName.getText().toString().trim(),
                binding.activitySignupEtLastName.getText().toString().trim(),
                binding.activitySignupEtEmail.getText().toString().trim(),
                binding.activitySignupEtConfirmPwd.getText().toString().trim());

        accountViewModel.signup(account).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.getStatus() == Constants.ERROR) {
                        if (baseResponse.getError() == Constants.SIGNUP_EMAIL_EXISTS) {
                            Toast.makeText(SignupActivity.this,
                                    getString(R.string.email_exists), Toast.LENGTH_LONG).show();

                            binding.activitySignupEtEmail.setError(getString(R.string.email_exists));
                        }
                    } else {
                        Toast.makeText(SignupActivity.this,
                                getString(R.string.signup_successfully), Toast.LENGTH_LONG).show();

                        DataLocalManager.setFirstName(account.getFirstName());
                        DataLocalManager.setLastname(account.getLastName());
                        DataLocalManager.setEmail(account.getEmail());
                        DataLocalManager.setPassword(account.getPassword());
                        DataLocalManager.setCheckRemember(true);

                        callLoginActivity();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    private void callLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_signup_tv_login:
                callLoginActivity();
                break;
            case R.id.activity_signup_btn_signup:
                signup();
                break;
        }

    }
}