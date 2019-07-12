package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 25/4/2017.
 */

public class Bank {
    private String holder;
    private String number;
    private String opd;
    private int status;

    public Bank(String holder, String number, String opd, int status) {
        this.holder = holder;
        this.number = number;
        this.opd = opd;
        this.status = status;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOpd() {
        return opd;
    }

    public void setOpd(String opd) {
        this.opd = opd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
