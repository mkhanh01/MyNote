package com.example.notemanagement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.notemanagement.model.Account;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.repository.AccountRepository;

import retrofit2.Call;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository();
    }

    public Call<BaseResponse> login(Account account){
        return accountRepository.login(account);
    }

    public Call<BaseResponse> signup(Account account){
        return accountRepository.signup(account);
    }

    public Call<BaseResponse> editProfile(String email, Account account){
        return accountRepository.editProfile(email, account);
    }

    public Call<BaseResponse> changePassword(Account account, String password){
        return accountRepository.changePassword(account, password);
    }
}
