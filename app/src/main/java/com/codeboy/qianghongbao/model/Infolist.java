package com.codeboy.qianghongbao.model;

import java.util.List;

/**
 * Created by snsoft on 27/4/2017.
 */

public class Infolist {
    private String clientName;
    private String account;
    private String accountType;
    private String totalCount;
    private List<InfoItem> infoList;


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

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<InfoItem> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<InfoItem> infoList) {
        this.infoList = infoList;
    }
}
