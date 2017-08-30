package com.zh.metermanagementcw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.TransformerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/30.
 */
public class TransformerAdapter extends BaseAdapter {

    private List<TransformerBean> mBeanArrayList = new ArrayList<>();
    private Context mContext;

    public TransformerAdapter(Context context, List<TransformerBean> transformerBeanList){

        mContext = context;
        mBeanArrayList = transformerBeanList;
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
            convertView = inflater.inflate(R.layout.item_lv_transformer, parent,false);      // listview中每一项的布局

            viewHold = new ViewHold(convertView);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvLatitude.setText(mBeanArrayList.get(position).getLatitude());
        viewHold.tvLongitude.setText(mBeanArrayList.get(position).getLongitude());
        viewHold.tvAddr.setText(mBeanArrayList.get(position).getAddr());

        return convertView;
    }


    class ViewHold{

        /** 纬度 */
        public TextView tvLatitude;
        /** 经度 */
        public TextView tvLongitude;
        /** 地址 */
        public TextView tvAddr;


        public ViewHold(View view) {

            tvLatitude = (TextView) view.findViewById(R.id.tv_latitude);
            tvLongitude = (TextView) view.findViewById(R.id.tv_longitude);
            tvAddr = (TextView) view.findViewById(R.id.tv_addr);

        }


    }
}
