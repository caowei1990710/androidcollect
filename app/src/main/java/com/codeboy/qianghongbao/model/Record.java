package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 26/4/2017.
 */

public class Record {
    private String clientName;
    private String querySize;
    private String accountType;
    private String account;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getQuerySize() {
        return querySize;
    }

    public void setQuerySize(String querySize) {
        this.querySize = querySize;
    }

}
