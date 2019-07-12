package com.codeboy.qianghongbao.model;

/**
 * Created by colincao on 4/7/2017.
 */

public class Return {
    private int returnCode;
    private String returnMessage;

    public Return() {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
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
