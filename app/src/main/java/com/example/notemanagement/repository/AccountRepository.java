package com.example.notemanagement.repository;


import com.example.notemanagement.api.APIClient;
import com.example.notemanagement.model.Account;
import com.example.notemanagement.model.BaseResponse;
import com.example.notemanagement.model.Constants;
import com.example.notemanagement.service.AccountService;

import retrofit2.Call;

public class AccountRepository {

    private AccountService mAccountService;

    public AccountRepository(){
        mAccountService = getAccountServices();
    }

    public static AccountService getAccountServices(){
        return APIClient.getClient().create(AccountService.class);
    }

    public Call<BaseResponse> login(Account account){
        return getAccountServices().login(account.getEmail(), account.getPassword());
    }

    public Call<BaseResponse> signup(Account account){
        return getAccountServices().signup(account.getEmail(), account.getPassword()
                , account.getFirstName(), account.getLastName());
    }

    public Call<BaseResponse> editProfile(String email, Account account){
        return getAccountServices().editProfile(Constants.TAB_PROFILE, email, account.getEmail()
                , account.getFirstName(), account.getLastName());
    }

    public Call<BaseResponse> changePassword(Account account, String password){
        return getAccountServices().changePassword(Constants.TAB_PROFILE, account.getEmail()
                , account.getPassword(), password);
    }
}