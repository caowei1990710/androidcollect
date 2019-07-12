package com.codeboy.qianghongbao.model;

/**
 * Created by colincao on 4/7/2017.
 */

public class Depositrecord {
    private String id;
    private String balance;
    private String transferTime;
    private String transferAmount;
    private String senderComment;
    private String beneficiaryComment;
    private String transferComment;
    private String beforeBalance;
    private String afterBalance;

    public Depositrecord(String id, String balance, String transferAmount,String transferTime, String senderComment, String beneficiaryComment) {
        this.id = id;
        this.balance = balance;
        this.transferAmount = transferAmount;
        this.transferTime = transferTime;
        this.senderComment = senderComment;
        this.beneficiaryComment = beneficiaryComment;
    }

    public String getTransferComment() {
        return transferComment;
    }

    public void setTransferComment(String transferComment) {
        this.transferComment = transferComment;
    }

    public String getBeforeAmount() {
        return beforeBalance;
    }

    public void setBeforeAmount(String beforeAmount) {
        this.beforeBalance = beforeAmount;
    }

    public String getAfterAmount() {
        return afterBalance;
    }

    public void setAfterAmount(String afterAmount) {
        this.afterBalance = afterAmount;
    }

    public Depositrecord() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getSenderComment() {
        return senderComment;
    }

    public void setSenderComment(String senderComment) {
        this.senderComment = senderComment;
    }

    public String getBeneficiaryComment() {
        return beneficiaryComment;
    }

    public void setBeneficiaryComment(String beneficiaryComment) {
        this.beneficiaryComment = beneficiaryComment;
    }
}
