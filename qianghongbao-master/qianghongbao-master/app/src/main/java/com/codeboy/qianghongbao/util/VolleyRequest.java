package com.codeboy.qianghongbao.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by colincao on 4/6/2017.
 */

public class VolleyRequest extends StringRequest {
    public Context context;


    public Map<String, String> headers = new HashMap<String, String>();

    public VolleyRequest(int method, String url,
                           Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    //有的服务器要key，所有添加了一个key方法
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return super.getBody();
    }
}
