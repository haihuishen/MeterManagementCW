package com.zh.metermanagementcw.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.library.Info;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.bean.CollectorNumberBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.ArrayList;

/**
 * 零散新装
 */
public class ScatteredNewMeterAdapter extends BaseAdapter {

    private ArrayList<ScatteredNewMeterBean> mBeanArrayList = new ArrayList<>();
    private Context mContext;

    PrePhotoListener mPrePhotoListener;

    private ArrayList<CollectorNumberBean> mCollectorNumberBean = new ArrayList<>();

    public ScatteredNewMeterAdapter(Context context, ArrayList<ScatteredNewMeterBean> beanList,
                                    PrePhotoListener prePhotoListener){

        mContext = context;
        mBeanArrayList = beanList;
        mPrePhotoListener = prePhotoListener;
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

        final ViewHold viewHold;
        if(convertView == null){                // 拿缓存
            // 将 layout 填充成"View"
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.item_lv_scattered_new_meter,parent,false);      // listview中每一项的布局

            viewHold = new ViewHold(convertView);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvUserNumber.setText(mBeanArrayList.get(position).getUserNumber());
        viewHold.tvUserName.setText(mBeanArrayList.get(position).getUserName());
        viewHold.tvUserAddr.setText(mBeanArrayList.get(position).getUserAddr());
        viewHold.tvUserPhone.setText(mBeanArrayList.get(position).getUserPhone());

        viewHold.tvNewAssetNumbersScan.setText(mBeanArrayList.get(position).getNewAssetNumbers());
        viewHold.tvNewAddr.setText(mBeanArrayList.get(position).getNewAddr());
        viewHold.tvNewElectricity.setText(mBeanArrayList.get(position).getNewElectricity());
        viewHold.tvMeterFootNumbers.setText(mBeanArrayList.get(position).getMeterFootNumbers());
        viewHold.tvMeterBodyNumbers1.setText(mBeanArrayList.get(position).getMeterBodyNumbers1());
        viewHold.tvMeterBodyNumbers2.setText(mBeanArrayList.get(position).getMeterBodyNumbers2());

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);         // 水平

        //LogUtils.i("mBeanArrayList.get(position):" +  mBeanArrayList.get(position).toString());

        String path = "";
        if (!TextUtils.isEmpty(mBeanArrayList.get(position).getPicPath())) {
            path = mBeanArrayList.get(position).getPicPath();
        }
        LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);         // 水平
        viewHold.rvPic.setLayoutManager(manager1);

        PicAdapter picAdapter = new PicAdapter(mContext, new ArrayList<String>(), new PicAdapter.PicListener() {
            @Override
            public void onDelete(int index, String path) {

            }

            @Override
            public void onPreView(int index, String path, Info info) {

                mPrePhotoListener.onPreView(index, path, info);

            }
        });

        viewHold.rvPic.setAdapter(picAdapter);
        picAdapter.setDeleteIcon(true);
        picAdapter.setPathList(path);

        return convertView;
    }


    class ViewHold{

        /** 用户编号 */
        public TextView tvUserNumber;
        /** 用户名称 */
        public TextView tvUserName;
        /** 用户地址 */
        public TextView tvUserAddr;
        /** 用户电话 */
        public TextView tvUserPhone;


        /** 新表 -- 包裹的布局 */
        public LinearLayout lLayoutNewMeter;
        /** 新表资产编号 */
        public TextView tvNewAssetNumbersScan;
        /** 新表表地址 */
        public TextView tvNewAddr;
        /** 新表止码 */
        public TextView tvNewElectricity;

        /** 电表表脚封扣 -- 文本 -- tv_meterFootNumbers*/
        public TextView tvMeterFootNumbers;
        /** 表箱封扣1 -- 文本 -- tv_meterBodyNumbers1*/
        public TextView tvMeterBodyNumbers1;
        /** 表箱封扣2 -- 文本 -- tv_meterBodyNumbers2*/
        public TextView tvMeterBodyNumbers2;


        /** 图片列表 -- 电表的 */
        public RecyclerView rvPic;



        public ViewHold(View view) {

            tvUserNumber = (TextView) view.findViewById(R.id.tv_userNumber);
            tvUserName = (TextView) view.findViewById(R.id.tv_userName);
            tvUserAddr = (TextView) view.findViewById(R.id.tv_userAddr);
            tvUserPhone = (TextView) view.findViewById(R.id.tv_userPhone);

            tvNewAssetNumbersScan = (TextView) view.findViewById(R.id.tv_newAssetNumbersScan);
            tvNewAddr = (TextView) view.findViewById(R.id.tv_newAddr);
            tvNewElectricity = (TextView) view.findViewById(R.id.tv_newElectricity);
            tvMeterFootNumbers = (TextView) view.findViewById(R.id.tv_meterFootNumbers);
            tvMeterBodyNumbers1 = (TextView) view.findViewById(R.id.tv_meterBodyNumbers1);
            tvMeterBodyNumbers2 = (TextView) view.findViewById(R.id.tv_meterBodyNumbers2);

            rvPic = (RecyclerView) view.findViewById(R.id.rv_pic);
        }


    }

    /**
     * 电表图片预览
     */
    public interface PrePhotoListener{

        /**
         * 电表图片预览
         *
         * @param index
         * @param path
         * @param info
         */
        void onPreView(int index, String path, Info info);
    }
}
