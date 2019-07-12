package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 26/4/2017.
 */

public class Syncmoney {
    private String clientName;
    private String account;
    private String accountType;
    private String realTimeBalance;
    private String clientTime;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRealTimeBalance() {
        return realTimeBalance;
    }

    public void setRealTimeBalance(String realTimeBalance) {
        this.realTimeBalance = realTimeBalance;
    }

    public String getClientTime() {
        return clientTime;
    }

    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }
}
