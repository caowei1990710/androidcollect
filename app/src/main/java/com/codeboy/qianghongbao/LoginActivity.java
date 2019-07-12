package com.codeboy.qianghongbao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.codeboy.qianghongbao.R;
import com.codeboy.qianghongbao.model.Bank;
import com.codeboy.qianghongbao.model.Detail;
import com.codeboy.qianghongbao.model.Return;
import com.codeboy.qianghongbao.util.BitmapUtils;
import com.codeboy.qianghongbao.util.JsonUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText devicename;
    public static String devicestring, platString;
    private Button sure;
    public BaseAdapter adapter;
    public AppCompatSpinner paltfrom;
    public AppCompatSpinner depart;
    private CheckBox newcheck;
    private ArrayList<String> platlist;
    private ArrayList<String> departlist;
    private ArrayList<String> namelist;
    private String newSelect;
    private String newdepart;
    private int platfdef = 0;
    private int departfdef = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        devicestring = BitmapUtils.getKey(LoginActivity.this, "bankcard");
        platString = BitmapUtils.getKey(LoginActivity.this, "platfrom");
        newdepart = BitmapUtils.getKey(LoginActivity.this, "depart");
        findbyId();
//        if (BitmapUtils.getKey(LoginActivity.this, "depart") != null) {
//            depart.setChecked("1".equals(BitmapUtils.getKey(LoginActivity.this, "depart")));
//            if ("1".equals(BitmapUtils.getKey(LoginActivity.this, "depart")))
//                newdepart = "yes";
//            else
//                newdepart = "no";
//        }
        if (BitmapUtils.getKey(LoginActivity.this, "check") != null) {
            newcheck.setChecked("1".equals(BitmapUtils.getKey(LoginActivity.this, "check")));
            if ("1".equals(BitmapUtils.getKey(LoginActivity.this, "check")))
                newSelect = "yes";
            else
                newSelect = "no";
        }
//        if (!devicestring.equals("")) {
//            Intent intent = new Intent(LoginActivity.this, alipyActivity.class);
//            startActivity(intent);
////            sendData();
//        }
    }

    private void findbyId() {
        devicename = (EditText) findViewById(R.id.devicename);
        paltfrom = (AppCompatSpinner) findViewById(R.id.paltfrom);
        depart = (AppCompatSpinner) findViewById(R.id.depart);
        newcheck = (CheckBox) findViewById(R.id.newcheck);
        newcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BitmapUtils.setKey(LoginActivity.this, "check", isChecked ? "1" : "0");
//                isChecked?(newSelect = "yes"):(newSelect = "no");
                if (isChecked)
                    newSelect = "yes";
                else
                    newSelect = "no";
            }
        });
        platlist = new ArrayList<String>();
//        platlist.add("RUIBO");
//        platlist.add("BBETASIA");
        platlist.add("XBET");
//        platlist.add("HAOMEN");
        platlist.add("MSGREEN");
//        platlist.add("BAIBO");
        platlist.add("JINSHENG");
        platlist.add("BOLEBA");
        namelist = new ArrayList<String>();
//        namelist.add("金佰利");
//        namelist.add("金世豪");
        namelist.add("XBET");
//        namelist.add("宝运莱");
        namelist.add("PMS");
//        namelist.add("新德利");
        namelist.add("嘉博");
        namelist.add("天和");
        departlist = new ArrayList<String>();
        departlist.add("ipeak");
        departlist.add("anxeuz");
        departlist.add("hashtech");
        MyAdapter myAdapter = new MyAdapter(namelist, LoginActivity.this);
        MyAdapter deAdapter = new MyAdapter(departlist, LoginActivity.this);
        paltfrom.setAdapter(myAdapter);
        depart.setAdapter(deAdapter);
        devicename.setText(devicestring);
        sure = (Button) findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtils.setKey(LoginActivity.this, "bankcard", devicename.getText().toString());
                devicestring = devicename.getText().toString();
                Intent intent = new Intent(LoginActivity.this, alipyActivity.class);
                intent.putExtra("newSelect", newSelect);
                startActivity(intent);
//                sendData();
                finish();
                BitmapUtils.setKey(LoginActivity.this, "platfrom", platlist.get(paltfrom.getSelectedItemPosition()));
                BitmapUtils.setKey(LoginActivity.this, "depart", departlist.get(depart.getSelectedItemPosition()));
            }
        });
        for (int i = 0; i < platlist.size(); i++) {
            if (platString.equals(platlist.get(i))) {
                platfdef = i;
            }
        }
        paltfrom.setSelection(platfdef);
        for (int i = 0; i < departlist.size(); i++) {
            if (departlist.get(i).equals(newdepart))
                departfdef = i;
        }
        depart.setSelection(departfdef);
        depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LoginActivity.this, departlist.get(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        paltfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LoginActivity.this, platlist.get(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        paltfrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//        BitmapUtils.setKey(LoginActivity.this,"devicename");
    }

    class MyAdapter extends BaseAdapter {
        private ArrayList<String> list;
        private Context context;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public MyAdapter(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView iv = new TextView(context);//针对外面传递过来的Context变量，
            iv.setText(list.get(position));
//            Log.i("magc", String.valueOf(imgs[position]));
            iv.setLayoutParams(new LinearLayoutCompat.LayoutParams(200, 80));//设置Gallery中每一个图片的大小为80*80。
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            return iv;
        }
    }
//    public void sendData() {
//        String url = "http://www.autopay8.me/api/inquireDepositCard";
//        Log.e("url:", url);
////        devicestring = "ceshi*9";
////        Listdeposit listdeposit = databaseHelper.querylist(alipyActivity.this);
//        JSONObject params = new JSONObject();
//        try {
////            String wechat = listdeposit.getWechatAccount();
////                    params.put("String", JsonUtil.objectToString(wechat.substring(wechat.indexOf("：")+1, wechat.length())));
////                    params.put("depositRecords", JsonUtil.objectToString(listdeposit.getDeposit()));
//            params.put("item", JsonUtil.objectToString(devicestring));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.e("String", JsonUtil.objectToString(devicestring));
//        final String param = devicestring + "*99"; // new Gson().toJson(devicestring);
////        final String param = JsonUtil.objectToString(devicestring+"*99"); // new Gson().toJson(devicestring);
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            public void onResponse(String response) {
//                Intent intent = new Intent(LoginActivity.this, alipyActivity.class);
//                startActivity(intent);
//                Log.e("response", response);
//                Bank retrun = (Bank) JsonUtil.stringToObject(response, Bank.class);
////                        retrun =
//                Toast.makeText(LoginActivity.this, retrun.toString(), Toast.LENGTH_LONG).show();
//                if (retrun.getStatus() == 200) {
//                    BitmapUtils.setKey(LoginActivity.this, "bankcard", retrun.getNumber());
//
////                    bankcard = retrun.getNumber();
//                    Toast.makeText(LoginActivity.this, retrun.getNumber(), Toast.LENGTH_LONG).show();
//                    finish();
////                    retrun.getNumber();
////                    List<Detail> list = databaseHelper.querydata(1, "_id desc");
////                    BitmapUtils.setKey(LoginActivity.this, "_id", list.get(0).getId() + "");
//                }
//                BitmapUtils.setKey(LoginActivity.this, "bankcard",devicename.getText().toString());
//            }
//
//        }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                Log.e("error:", error + "");
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return param == null ? null : param.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", param, "utf-8");
//                    return null;
//                }
//            }
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseText = "";
//                try {
//                    responseText = new String(response.data, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                        /*
//                        // String responseString = "";
//                        if (response != null) {
//                            responseString = String.valueOf(response.statusCode);
//                            // can get more details such as response.headers
//                        }
//                        */
//                return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
//            }
//        };
//
//        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
//        QHBApplication.getHttpQueues().add(request);
//    }
}
