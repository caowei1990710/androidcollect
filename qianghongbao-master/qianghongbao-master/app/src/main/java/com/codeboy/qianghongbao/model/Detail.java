package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 31/3/2017.
 */

public class Detail {
    private String createtime;
    private String beforemoney;
    private String amout;
    private String aftermoney;
    private String paytime;
    private String note;
    private String changtype;
    private String mynote;
    private String othernote;
    private String nowmoney;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Detail() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Detail(String createtime, String beforemoney, String amout, String aftermoney, String paytime, String note, String changtype, String mynote, String othernote, String nowmoney, int type, int id) {
        this.createtime = createtime;
        this.beforemoney = beforemoney;
        this.amout = amout;
        this.aftermoney = aftermoney;
        this.paytime = paytime;
        this.note = note;
        this.changtype = changtype;
        this.mynote = mynote;
        this.othernote = othernote;
        this.nowmoney = nowmoney;
        this.type = type;
        this.id = id;
    }

    public String getChangtype() {
        return changtype;
    }

    public void setChangtype(String changtype) {
        this.changtype = changtype;
    }

    public String getMynote() {
        return mynote;
    }

    public void setMynote(String mynote) {
        this.mynote = mynote;
    }

    public String getOthernote() {
        return othernote;
    }

    public void setOthernote(String othernote) {
        this.othernote = othernote;
    }

    public String getNowmoney() {
        return nowmoney;
    }

    public void setNowmoney(String nowmoeny) {
        this.nowmoney = nowmoeny;
    }


    public String getCreatetime() {
        return createtime;
    }

    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getBeforemoney() {
        return beforemoney;
    }

    public void setBeforemoney(String beforemoney) {
        this.beforemoney = beforemoney;
    }

    public String getAmout() {
        return amout;
    }

    public void setAmout(String amout) {
        this.amout = amout;
    }

    public String getAftermoney() {
        return aftermoney;
    }

    public void setAftermoney(String aftermoney) {
        this.aftermoney = aftermoney;
    }
}
