package com.codeboy.qianghongbao.model;

/**
 * Created by colincao on 4/17/2017.
 */

public class Basemodel {
    private Data data;
    private String service;
    private String functionName;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Basemodel(Data data, String service, String functionName) {
        this.data = data;
        this.service = service;
        this.functionName = functionName;
    }

}
