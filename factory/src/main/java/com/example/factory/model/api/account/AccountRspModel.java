package com.example.factory.model.api.account;

import com.example.factory.model.db.User;

/**
 * Created by mxz on 18-3-17.
 */

public class AccountRspModel {
    private User user;
    private String account;
    private String token;
    private boolean isBind;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind='" + isBind + '\'' +
                '}';
    }

}
