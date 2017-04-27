package com.baidu.fuguoyong.easyshop.mian.user.login.Person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.fuguoyong.easyshop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PersonAdapter extends BaseAdapter {
    private List<ItemShow> data = new ArrayList<>();
    private ViewHolder viewHolder;

    public PersonAdapter(List<ItemShow> list) {
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_person_info, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
         viewHolder.tvItemName.setText(data.get(position).getItem_title());
        viewHolder.tvPerson.setText(data.get(position).getItem_content());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_person)
        TextView tvPerson;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
