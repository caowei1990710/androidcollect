package com.codeboy.qianghongbao.model;

import java.util.Date;

/**
 * Created by snsoft on 29/5/2018.
 */

public class AlipayItem {
    private String depositNumber;
    private Date tranTime;
    private Date creatTime;
    private Double amount;
    private String nickName;
    private String transferTime;
    private String note;
    private String wechatName;
    private String payAccount;
    private String platfrom;
    private String createUser;
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumber) {
        this.depositNumber = depositNumber;
    }

    public Date getTranTime() {
        return tranTime;
    }

    public void setTranTime(Date tranTime) {
        this.tranTime = tranTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AlipayItem() {
    }

    @Override
    public String toString() {
        return "AlipayItem{" +
                "depositNumber='" + depositNumber + '\'' +
                ", tranTime=" + tranTime +
                ", creatTime=" + creatTime +
                ", amount=" + amount +
                ", nickName='" + nickName + '\'' +
                ", note='" + note + '\'' +
                ", payAccount='" + payAccount + '\'' +
                ", wechatName='" + wechatName + '\'' +
                ", realName='" + realName + '\'' +
                ", transferTime='" + transferTime + '\'' +
                '}';
    }
}
