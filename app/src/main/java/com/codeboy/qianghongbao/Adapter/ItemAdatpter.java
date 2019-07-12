package com.codeboy.qianghongbao.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codeboy.qianghongbao.R;
import com.codeboy.qianghongbao.model.AliDetailitem;
import com.codeboy.qianghongbao.model.AlipayItem;

import java.util.List;

/**
 * Created by snsoft on 12/5/2018.
 */

public class ItemAdatpter extends ArrayAdapter {
    private final int resourceId;
    private List<AlipayItem> resululist;

    public ItemAdatpter(Context context, int resource, List<AlipayItem> list) {
        super(context, resource, list);
        resourceId = resource;
        resululist = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlipayItem item = resululist.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView account = (TextView) view.findViewById(R.id.account);
        TextView nickName = (TextView) view.findViewById(R.id.nickName);
        TextView amount = (TextView) view.findViewById(R.id.amount);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView remark = (TextView) view.findViewById(R.id.remark);
        account.setText(item.getWechatName());
        amount.setText(item.getAmount()+"");
        time.setText(item.getTransferTime());
        remark.setText(item.getNote());
        nickName.setText(item.getNickName());
        return view;
    }
}
