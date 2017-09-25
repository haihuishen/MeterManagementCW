package com.zh.metermanagementcw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/30.
 */
public class AcceptanceAdapter extends BaseAdapter {

    public static enum Type{
        all,

        dayFinish,
        dayUnfinished,
        monthFinish,
        monthUnfinished,

        dayAbnormal,
        monthAbnormal
    }

    private List<AcceptanceBean> mBeanArrayList = new ArrayList<>();
    private Context mContext;
    private Type mType;

    public AcceptanceAdapter(Context context, List<AcceptanceBean> acceptanceBeanList, Type type){

        mContext = context;
        mBeanArrayList = acceptanceBeanList;
        mType = type;
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
            convertView = inflater.inflate(R.layout.item_lv_acceptance,parent,false);      // listview中每一项的布局

            viewHold = new ViewHold(convertView, mType);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }




        
        viewHold.tvAssetNumbers.setText(mBeanArrayList.get(position).getAssetNumbers());
        viewHold.tvUserNumber.setText(mBeanArrayList.get(position).getUserNumber());
        viewHold.tvUserName.setText(mBeanArrayList.get(position).getUserName());
        viewHold.tvTerminalNo.setText(mBeanArrayList.get(position).getTerminalNo());
        viewHold.tvMeterAddr.setText(mBeanArrayList.get(position).getMeterAddr());
        

        viewHold.tvDaysFreezingTimeIn.setText(mBeanArrayList.get(position).getDaysFreezingTimeIn());
        viewHold.tvElectricityInByDay.setText(mBeanArrayList.get(position).getElectricityInByDay());
        viewHold.tvDaysFreezingTimeScan.setText(mBeanArrayList.get(position).getDaysFreezingTimeScan());
        viewHold.tvElectricityScanByDay.setText(mBeanArrayList.get(position).getElectricityScanByDay());
        viewHold.tvDifferencesByDay.setText(mBeanArrayList.get(position).getDifferencesByDay());
        viewHold.tvConclusionByDay.setText(mBeanArrayList.get(position).getConclusionByDay());

        viewHold.tvMonthFreezingTimeIn.setText(mBeanArrayList.get(position).getMonthFreezingTimeIn());
        viewHold.tvElectricityInByMonth.setText(mBeanArrayList.get(position).getElectricityInByMonth());
        viewHold.tvMonthFreezingTimeScan.setText(mBeanArrayList.get(position).getMonthFreezingTimeScan());
        viewHold.tvElectricityScanByMonth.setText(mBeanArrayList.get(position).getElectricityScanByMonth());
        viewHold.tvDifferencesByMonth.setText(mBeanArrayList.get(position).getDifferencesByMonth());
        viewHold.tvConclusionByMonth.setText(mBeanArrayList.get(position).getConclusionByMonth());

        return convertView;
    }


    class ViewHold{


        /** 资产编号 -- tv_assetNumbers*/
        public TextView tvAssetNumbers;
        /** 用户编号 -- tv_userNumber*/
        public TextView tvUserNumber;
        /** 用户名称 -- tv_userName*/
        public TextView tvUserName;
        /** 终端内编号 -- tv_terminalNo*/
        public TextView tvTerminalNo;
        /** 电表地址 -- tv_meterAddr*/
        public TextView tvMeterAddr;


        /** 日冻结(布局) -- llayout_day*/
        public LinearLayout lLayoutDay;
        /** 日冻结时间 -- tv_daysFreezingTimeIn*/
        public TextView tvDaysFreezingTimeIn;
        /** 日冻结读数 -- tv_electricityInByDay*/
        public TextView tvElectricityInByDay;
        /** 复核日冻结时间 -- tv_daysFreezingTimeScan*/
        public TextView tvDaysFreezingTimeScan;
        /** 复核日冻结读数 -- tv_electricityScanByDay*/
        public TextView tvElectricityScanByDay;
        /** 集抄与现场差异 -- tv_differencesByDay*/
        public TextView tvDifferencesByDay;
        /** 复核日冻结总结 -- tv_conclusionByDay*/
        public TextView tvConclusionByDay;

        /** 月冻结(布局) -- llayout_month*/
        public LinearLayout lLayoutMonth;
        /** 月冻结时间 -- tv_monthFreezingTimeIn*/
        public TextView tvMonthFreezingTimeIn;
        /** 月冻结读数 -- tv_electricityInByMonth*/
        public TextView tvElectricityInByMonth;
        /** 复核月冻结时间 -- tv_monthFreezingTimeScan*/
        public TextView tvMonthFreezingTimeScan;
        /** 复核月冻结读数 -- tv_electricityScanByMonth*/
        public TextView tvElectricityScanByMonth;
        /** 集抄与现场差异 -- tv_differencesByMonth*/
        public TextView tvDifferencesByMonth;
        /** 复核月冻结总结 -- tv_conclusionByMonth*/
        public TextView tvConclusionByMonth;



        public ViewHold(View view, Type type) {

            tvAssetNumbers = (TextView) view.findViewById(R.id.tv_assetNumbers);
            tvUserNumber = (TextView) view.findViewById(R.id.tv_userNumber);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvTerminalNo = (TextView) view.findViewById(R.id.tv_terminalNo);
            tvMeterAddr = (TextView) view.findViewById(R.id.tv_meterAddr);


            lLayoutDay = (LinearLayout) view.findViewById(R.id.llayout_day);
            tvDaysFreezingTimeIn = (TextView) view.findViewById(R.id.tv_daysFreezingTimeIn);
            tvElectricityInByDay = (TextView) view.findViewById(R.id.tv_electricityInByDay);
            tvDaysFreezingTimeScan = (TextView) view.findViewById(R.id.tv_daysFreezingTimeScan);
            tvElectricityScanByDay = (TextView) view.findViewById(R.id.tv_electricityScanByDay);
            tvDifferencesByDay = (TextView) view.findViewById(R.id.tv_differencesByDay);
            tvConclusionByDay = (TextView) view.findViewById(R.id.tv_conclusionByDay);

            lLayoutMonth = (LinearLayout) view.findViewById(R.id.llayout_month);
            tvMonthFreezingTimeIn = (TextView) view.findViewById(R.id.tv_monthFreezingTimeIn);
            tvElectricityInByMonth = (TextView) view.findViewById(R.id.tv_electricityInByMonth);
            tvMonthFreezingTimeScan = (TextView) view.findViewById(R.id.tv_monthFreezingTimeScan);
            tvElectricityScanByMonth = (TextView) view.findViewById(R.id.tv_electricityScanByMonth);
            tvDifferencesByMonth = (TextView) view.findViewById(R.id.tv_differencesByMonth);
            tvConclusionByMonth = (TextView) view.findViewById(R.id.tv_conclusionByMonth);

            if(mType == Type.all){
                lLayoutDay.setVisibility(View.VISIBLE);
                lLayoutMonth.setVisibility(View.VISIBLE);

            }else if(mType == Type.dayFinish ||
                    mType == Type.dayUnfinished ||
                    mType == Type.dayAbnormal){

                lLayoutDay.setVisibility(View.VISIBLE);
                lLayoutMonth.setVisibility(View.GONE);

            }else if(mType == Type.monthFinish ||
                    mType == Type.monthUnfinished ||
                    mType == Type.monthAbnormal){

                lLayoutDay.setVisibility(View.GONE);
                lLayoutMonth.setVisibility(View.VISIBLE);
            }
        }


    }
}
