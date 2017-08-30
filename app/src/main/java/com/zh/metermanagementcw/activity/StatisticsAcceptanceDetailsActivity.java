package com.zh.metermanagementcw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.AcceptanceAdapter;
import com.zh.metermanagementcw.adapter.AssetsNumberMismatchesAdapter;
import com.zh.metermanagementcw.adapter.ConcentratorAdapter;
import com.zh.metermanagementcw.adapter.FinishedAdapter;
import com.zh.metermanagementcw.adapter.TransformerAdapter;
import com.zh.metermanagementcw.adapter.UnFinishedAdapter;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.CollectorNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.TransformerBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

/**
 * Created by Administrator on 2017/8/3.
 */

public class StatisticsAcceptanceDetailsActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    ListView mListView;

    List<AcceptanceBean> mAcceptanceBeanList = new ArrayList<>();

    AcceptanceAdapter mAcceptanceAdapter;


    @Override
    public int getContentLayout() {
        return R.layout.activity_statistics_acceptance_details;
    }

    @Override
    public void initTitleListener(TextView tvTitle, Button btnBack, Button btnMenu) {
        mTvTitle = tvTitle;
        mBtnBack = btnBack;
        mBtnMenu = btnMenu;

        mBtnBack.setOnClickListener(this);
        mBtnMenu.setOnClickListener(this);
    }

    @Override
    public void initTitleData(TextView tvTitle, Button btnBack, Button btnMenu) {
        mTvTitle.setVisibility(View.VISIBLE);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnMenu.setVisibility(View.GONE);

        mTvTitle.setText("详情");
    }


    @Override
    public void initView() {
        mListView = (ListView) findViewById(R.id.lv_acceptance);
    }

    @Override
    public void initListener() {
        
    }

    @Override
    public void initData() {

        List<AcceptanceBean> beanList = new ArrayList<>();
        
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");


        if(type.equals(AcceptanceAdapter.Type.all.toString())){
            mTvTitle.setText("总户数");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList = beanList;
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.all);
            
        }else if(type.equals(AcceptanceAdapter.Type.dayFinish.toString())){
            mTvTitle.setText("日冻结复核已完成");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList.clear();
            for(AcceptanceBean bean : beanList){
                if(bean.isFinishByDay()) {
                    mAcceptanceBeanList.add(bean);
                }
            }
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.dayFinish);

        }else if(type.equals(AcceptanceAdapter.Type.dayUnfinished.toString())){
            mTvTitle.setText("日冻结复核未完成");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList.clear();
            for(AcceptanceBean bean : beanList){
                if(!bean.isFinishByDay()) {
                    mAcceptanceBeanList.add(bean);
                }
            }
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.dayUnfinished);

        }else if(type.equals(AcceptanceAdapter.Type.dayAbnormal.toString())){
            mTvTitle.setText("日冻结复核异常");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList.clear();
            for(AcceptanceBean bean : beanList){
                if(bean.isFinishByDay()) {
                    if(!bean.getConclusionByDay().equals("正常"))
                        mAcceptanceBeanList.add(bean);
                }
            }
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.dayAbnormal);

        }else if(type.equals(AcceptanceAdapter.Type.monthFinish.toString())){
            mTvTitle.setText("月冻结复核已完成");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList.clear();
            for(AcceptanceBean bean : beanList){
                if(bean.isFinishByMonth()) {
                    mAcceptanceBeanList.add(bean);
                }
            }
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.monthFinish);

        }else if(type.equals(AcceptanceAdapter.Type.monthUnfinished.toString())){
            mTvTitle.setText("月冻结复核未完成");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList.clear();
            for(AcceptanceBean bean : beanList){
                if(!bean.isFinishByMonth()) {
                    mAcceptanceBeanList.add(bean);
                }
            }
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.monthUnfinished);

        }else if(type.equals(AcceptanceAdapter.Type.monthAbnormal.toString())){
            mTvTitle.setText("月冻结复核异常");
            beanList = MyApplication.getAcceptanceBeanList();
            mAcceptanceBeanList.clear();
            for(AcceptanceBean bean : beanList){
                if(bean.isFinishByMonth()) {
                    if(!bean.getConclusionByMonth().equals("正常"))
                        mAcceptanceBeanList.add(bean);
                }
            }
            mAcceptanceAdapter = new AcceptanceAdapter(this, mAcceptanceBeanList, AcceptanceAdapter.Type.monthAbnormal);

        }
        
        mListView.setAdapter(mAcceptanceAdapter);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;

        }
    }


}
