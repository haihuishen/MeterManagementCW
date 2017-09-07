package com.zh.metermanagementcw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.bean.ConcentratorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/30.
 */
public class ConcentratorAdapter extends BaseAdapter {

    private List<ConcentratorBean> mBeanArrayList = new ArrayList<>();
    private Context mContext;

    public ConcentratorAdapter(Context context, List<ConcentratorBean> concentratorBeanList){

        mContext = context;
        mBeanArrayList = concentratorBeanList;
    }

    @Override
    public int getCount() {
        return mBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;
        if(convertView == null){                // 拿缓存
            // 将 layout 填充成"View"
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.item_lv_concentrator, parent,false);      // listview中每一项的布局

            viewHold = new ViewHold(convertView);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvAssetNumbers.setText(mBeanArrayList.get(position).getAssetNumbers());
        viewHold.tvLatitude.setText(mBeanArrayList.get(position).getLatitude());
        viewHold.tvLongitude.setText(mBeanArrayList.get(position).getLongitude());
        viewHold.tvAddr.setText(mBeanArrayList.get(position).getAddr());

        viewHold.tvMeterFootNumbers.setText(mBeanArrayList.get(position).getMeterFootNumbers());
        viewHold.tvMeterBodyNumbers1.setText(mBeanArrayList.get(position).getMeterBodyNumbers1());
        viewHold.tvMeterBodyNumbers2.setText(mBeanArrayList.get(position).getMeterBodyNumbers2());

        return convertView;
    }


    class ViewHold{

        /** 资产编号(集中器) */
        public TextView tvAssetNumbers;
        /** 纬度 */
        public TextView tvLatitude;
        /** 经度 */
        public TextView tvLongitude;
        /** 地址 */
        public TextView tvAddr;

        /** 电表表脚封扣 -- 文本 -- tv_meterFootNumbers*/
        public TextView tvMeterFootNumbers;
        /** 表箱封扣1 -- 文本 -- tv_meterBodyNumbers1*/
        public TextView tvMeterBodyNumbers1;
        /** 表箱封扣2 -- 文本 -- tv_meterBodyNumbers2*/
        public TextView tvMeterBodyNumbers2;


        public ViewHold(View view) {

            tvAssetNumbers = (TextView) view.findViewById(R.id.tv_assetNumbers);
            tvLatitude = (TextView) view.findViewById(R.id.tv_latitude);
            tvLongitude = (TextView) view.findViewById(R.id.tv_longitude);
            tvAddr = (TextView) view.findViewById(R.id.tv_addr);

            tvMeterFootNumbers = (TextView) view.findViewById(R.id.tv_meterFootNumbers);
            tvMeterBodyNumbers1 = (TextView) view.findViewById(R.id.tv_meterBodyNumbers1);
            tvMeterBodyNumbers2 = (TextView) view.findViewById(R.id.tv_meterBodyNumbers2);

        }


    }
}
