package com.codeboy.qianghongbao.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeboy.qianghongbao.R;
import com.codeboy.qianghongbao.model.Detail;

import java.util.List;

/**
 * Created by snsoft on 31/3/2017.
 */

public class DetailAdapter extends BaseAdapter {
    private class ViewHolder {
        public TextView id;
        public TextView create;
        public TextView before;
        public TextView amount;
        public TextView after;
        public TextView note;
        public TextView mynote;
        public TextView othernote;
        public TextView money;
        public LinearLayout back;
    }

    private List<Detail> list;
    private Context context;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public DetailAdapter(List<Detail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.detailitem, null);
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.create = (TextView) convertView.findViewById(R.id.create);
            holder.before = (TextView) convertView.findViewById(R.id.before);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.after = (TextView) convertView.findViewById(R.id.after);
            holder.note = (TextView) convertView.findViewById(R.id.note);
            holder.mynote = (TextView) convertView.findViewById(R.id.mynote);
            holder.othernote = (TextView) convertView.findViewById(R.id.othernote);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.back = (LinearLayout) convertView.findViewById(R.id.back);
            convertView.setTag(holder);

        }
        holder = (ViewHolder) convertView.getTag();
        holder.id.setText(list.get(position).getId() + "");
        holder.create.setText(list.get(position).getPaytime());
        holder.before.setText(list.get(position).getBeforemoney());
        holder.amount.setText(list.get(position).getAmout());
        holder.after.setText(list.get(position).getAftermoney());
        holder.note.setText(list.get(position).getNote());
        holder.mynote.setText(list.get(position).getMynote());
        holder.othernote.setText(list.get(position).getOthernote());
        holder.money.setText(list.get(position).getNowmoney());
        if (list.get(position).getType() == 0)
            holder.back.setBackgroundColor(Color.parseColor("#FFFFFF"));
        else
            holder.back.setBackgroundColor(Color.parseColor("#FF0000"));
        return convertView;
    }
}

