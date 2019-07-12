package com.codeboy.qianghongbao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by snsoft on 3/4/2017.
 */

public class PayItemAcitivity extends Activity{
    private Spinner spinner;
    private EditText amount;
    private Button sure;
    private String moneychange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payitem);
        findbyId();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void findbyId(){
        spinner = (Spinner) findViewById(R.id.spinner2);
        amount = (EditText) findViewById(R.id.money);
        sure = (Button) findViewById(R.id.sure);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.putExtra("bian", moneychange);
                setResult(4,i);
                finish();
            }
        });
    }
}
