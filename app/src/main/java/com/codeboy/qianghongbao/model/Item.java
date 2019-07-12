package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 20/4/2017.
 */

public class Item {
    public String bankCard;
    public String credit;
    public String balance;
    public String createtime;
    public String depositAddress;
    public String flag;
    public String payer = "test";
    public String amount;
    public String status;
    public String remark;

    public String getStatus() {
        return status;
    }

    public String getDepositAddress() {
        return depositAddress;
    }

    public void setDepositAddress(String depositAddress) {
        this.depositAddress = depositAddress;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepositTime() {
        return createtime;
    }

    public void setDepositTime(String createtime) {
        this.createtime = createtime;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
