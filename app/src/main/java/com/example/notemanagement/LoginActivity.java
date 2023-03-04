package com.example.notemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.notemanagement.data.DataLocalManager;
import com.example.notemanagement.databinding.ActivityLoginBinding;
import com.example.notemanagement.model.Account;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityLoginBinding binding;
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding.activityLoginChkRemember.setOnClickListener(this);
        binding.activityLoginTvNewAccount.setOnClickListener(this);
        binding.activityLoginBtnLogin.setOnClickListener(this);

        if(DataLocalManager.getCheckRemember()){
            binding.activityLoginEtEmail.setText(DataLocalManager.getEmail());
            binding.activityLoginEtPwd.setText(DataLocalManager.getPassword());
            binding.activityLoginChkRemember.setChecked(true);
        }
    }

    public boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean checkInput(){
        if(TextUtils.isEmpty(binding.activityLoginEtEmail.getText().toString().trim())){
            binding.activityLoginEtEmail.setError(getResources().getString(R.string.require));
            return false;
        }

        if(TextUtils.isEmpty(binding.activityLoginEtPwd.getText().toString().trim())){
            binding.activityLoginEtPwd.setError(getResources().getString(R.string.require));
            return false;
        }

        if(!isEmailValid(binding.activityLoginEtEmail.getText().toString().trim())){
            binding.activityLoginEtEmail.setError(getResources().getString(R.string.email_error));
            return false;
        }

        return true;
    }

    private void login(){
        if (!checkInput())
            return;

        final Account account = new Account(binding.activityLoginEtEmail.getText().toString().trim(),
                binding.activityLoginEtPwd.getText().toString().trim());

        accountViewModel.login(account).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();

                if(baseResponse != null){
                    if (baseResponse.getStatus() == Constants.ERROR){
                        if (baseResponse.getError() == Constants.LOGIN_PASSWORD_ERROR){
                            binding.activityLoginEtPwd.setError(getString(R.string.password_error));
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.email_error)
                                    , Toast.LENGTH_SHORT).show();

                            binding.activityLoginEtEmail.setError(getResources()
                                    .getString(R.string.require));
                            binding.activityLoginEtPwd.setError(getResources()
                                    .getString(R.string.require));
                        }
                    } else {
                        account.setFirstName(baseResponse.getInfo().getFirstName());
                        account.setLastName(baseResponse.getInfo().getLastName());

                        Toast.makeText(LoginActivity.this, getResources()
                                .getString(R.string.login_successfully), Toast.LENGTH_LONG).show();

                        DataLocalManager.setEmail(account.getEmail());
                        DataLocalManager.setPassword(account.getPassword());
                        DataLocalManager.setFirstName(account.getFirstName());
                        DataLocalManager.setLastname(account.getLastName());
                        DataLocalManager.setCheckRemember(isCheckedRemember());

                        callHomeActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    private boolean isCheckedRemember(){
        if(binding.activityLoginChkRemember.isChecked())
            return true;

        return false;
    }

    private void callHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();
    }
    private void callSignupActivity(){
        Intent intent = new Intent(this, SignupActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_login_btn_login:
                login();
                break;
            case R.id.activity_login_tv_newAccount:
                callSignupActivity();
                break;
        }

    }
}