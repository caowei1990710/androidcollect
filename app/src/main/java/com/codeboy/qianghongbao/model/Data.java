package com.codeboy.qianghongbao.model;

import java.util.ArrayList;

/**
 * Created by colincao on 4/17/2017.
 */

public class Data{
    public ArrayList<Item> items;
    public String status;
    public String token;
    public Data(ArrayList<Item> item) {
        this.items = item;
    }

    public ArrayList<Item> getItem() {
        return items;
    }

    public void setItem(ArrayList<Item> item) {
        this.items = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
