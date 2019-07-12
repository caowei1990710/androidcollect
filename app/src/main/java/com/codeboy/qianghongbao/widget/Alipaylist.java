package com.codeboy.qianghongbao.widget;

import com.codeboy.qianghongbao.model.Alidetail;

import java.util.List;

/**
 * Created by colincao on 4/18/2017.
 */

public class Alipaylist {
    private String alipayAccount;
    private List<Alidetail> depositRecords;

    public Alipaylist() {
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public List<Alidetail> getDepositRecords() {
        return depositRecords;
    }

    public void setDepositRecords(List<Alidetail> depositRecords) {
        this.depositRecords = depositRecords;
    }
}
