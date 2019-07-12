package com.codeboy.qianghongbao.model;

import java.util.List;

/**
 * Created by colincao on 4/7/2017.
 */

public class Listdeposit {
    private String wechatAccount;
    private List<Depositrecord> depositRecords;

    public Listdeposit(String wechatAccount, List<Depositrecord> deposit) {
        this.wechatAccount = wechatAccount;
        this.depositRecords = deposit;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }

    public List<Depositrecord> getDeposit() {
        return depositRecords;
    }

    public void setDeposit(List<Depositrecord> deposit) {
        this.depositRecords = deposit;
    }
}
