package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 10/5/2018.
 */

public class AliDetailitem {
    private String wechatName;
    private String nickName;
    private String state;
    private String amount;
    private String transTime;
    private String note;
    private String type;
    private String dayamount;
    private String daylimit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDaylimit() {
        return daylimit;
    }

    public void setDaylimit(String daylimit) {
        this.daylimit = daylimit;
    }

    public String getDayamount() {
        return dayamount;
    }

    public void setDayamount(String dayamount) {
        this.dayamount = dayamount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public AliDetailitem() {
    }
}
