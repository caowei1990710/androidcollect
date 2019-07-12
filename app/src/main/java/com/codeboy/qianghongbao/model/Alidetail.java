package com.codeboy.qianghongbao.model;

/**
 * Created by colincao on 4/13/2017.
 */

public class Alidetail {
    private String id;
    private String transferTime;
    private String transferAmount;
    private String senderName;
    private String depositNumber;
    private String senderNickname;
    private String senderAccount;
    private String senderComment;
    private String balance;
    private String amount;
    private String beforeBalance;
    private String afterBalance;
    private String plafrom;
    private String note;
    private String wechatName;
    private String nickName;
    private String tranTime;
    private String createUser;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTranTime() {
        return tranTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumber) {
        this.depositNumber = depositNumber;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPlafrom() {
        return plafrom;
    }

    public void setPlafrom(String plafrom) {
        this.plafrom = plafrom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getSenderComment() {
        return senderComment;
    }

    public void setSenderComment(String senderComment) {
        this.senderComment = senderComment;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(String beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public String getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(String afterBalance) {
        this.afterBalance = afterBalance;
    }

    public Alidetail() {
    }

    @Override
    public String toString() {
        return "Alidetail{" +
                "id='" + id + '\'' +
                ", transferTime='" + transferTime + '\'' +
                ", transferAmount='" + transferAmount + '\'' +
                ", senderName='" + senderName + '\'' +
                ", depositNumber='" + depositNumber + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", senderAccount='" + senderAccount + '\'' +
                ", senderComment='" + senderComment + '\'' +
                ", balance='" + balance + '\'' +
                ", amount='" + amount + '\'' +
                ", beforeBalance='" + beforeBalance + '\'' +
                ", afterBalance='" + afterBalance + '\'' +
                ", plafrom='" + plafrom + '\'' +
                ", note='" + note + '\'' +
                ", wechatName='" + wechatName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", tranTime='" + tranTime + '\'' +
                ", createUser='" + createUser + '\'' +
                '}';
    }

    public Alidetail(String id, String transferTime, String senderName, String senderNickname, String senderAccount, String senderComment, String balance, String beforeBalance, String afterBalance, String plafrom, String note) {
        this.id = id;
        this.transferTime = transferTime;
        this.senderName = senderName;
        this.senderNickname = senderNickname;
        this.senderAccount = senderAccount;
        this.senderComment = senderComment;
        this.balance = balance;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.plafrom = plafrom;
        this.note = note;
    }
}
