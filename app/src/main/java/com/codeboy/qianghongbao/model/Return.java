package com.codeboy.qianghongbao.model;

/**
 * Created by colincao on 4/7/2017.
 */

public class Return {
    private int returnCode;
    private String returnMessage;
    private int status;
    private String msg;
    private Object data;
    private int code;
    private int totalnumber;

    public int getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(int totalnumber) {
        this.totalnumber = totalnumber;
    }

    public Return() {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    public Object getObject() {
        return data;
    }

    public void setObject(Object object) {
        this.data = object;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
