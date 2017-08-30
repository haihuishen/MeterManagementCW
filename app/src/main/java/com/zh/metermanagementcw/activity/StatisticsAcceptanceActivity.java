package com.zh.metermanagementcw.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.AcceptanceAdapter;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.TransformerBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/3.
 */

public class StatisticsAcceptanceActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 总户数 -- 文本 -- tv_allCount*/
    private TextView mTvAllCount;
    /** 总户数 -- 查看详情 -- 文本 -- tv_all*/
    private TextView mTvAll;

    /** 日冻结复核\n已完成 -- 文本 -- tv_dayFinishCount*/
    private TextView mTvDayFinishCount;
    /** 日冻结复核\n已完成 -- 查看详情 -- 文本 -- tv_dayFinish*/
    private TextView mTvDayFinish;
    /** 日冻结复核\n未完成 -- 文本 -- tv_dayUnfinishedCount*/
    private TextView mTvDayUnfinishedCount;
    /** 日冻结复核\n未完成 -- 查看详情 -- 文本 -- tv_dayUnfinished*/
    private TextView mTvDayUnfinished;

    /** 月冻结复核\n已完成 -- 文本 -- tv_monthFinishCount*/
    private TextView mTvMonthFinishCount;
    /** 月冻结复核\n已完成 -- 查看详情 -- 文本 -- tv_monthFinish*/
    private TextView mTvMonthFinish;
    /** 月冻结复核\n未完成 -- 文本 -- tv_monthUnfinishedCount*/
    private TextView mTvMonthUnfinishedCount;
    /** 月冻结复核\n未完成 -- 查看详情 -- 文本 -- tv_monthUnfinished*/
    private TextView mTvMonthUnfinished;


    /** 日冻结复核\n异常 -- 文本 -- tv_dayAbnormalCount*/
    private TextView mTvDayAbnormalCount;
    /** 日冻结复核\n异常 -- 查看详情 -- 文本 -- tv_dayAbnormal*/
    private TextView mTvDayAbnormal;
    /** 月冻结复核\n异常 -- 文本 -- tv_monthAbnormalCount*/
    private TextView mTvMonthAbnormalCount;
    /** 月冻结复核\n异常 -- 查看详情 -- 文本 -- tv_monthAbnormal*/
    private TextView mTvMonthAbnormal;



    @Override
    public int getContentLayout() {
        return R.layout.activity_statistics_acceptance;
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

        mTvTitle.setText("统计");
    }

    @Override
    public void initView() {

        mTvAllCount = (TextView) findViewById(R.id.tv_allCount);
        mTvAll = (TextView) findViewById(R.id.tv_all);

        mTvDayFinishCount = (TextView) findViewById(R.id.tv_dayFinishCount);
        mTvDayFinish = (TextView) findViewById(R.id.tv_dayFinish);
        mTvDayUnfinishedCount = (TextView) findViewById(R.id.tv_dayUnfinishedCount);
        mTvDayUnfinished = (TextView) findViewById(R.id.tv_dayUnfinished);

        mTvMonthFinishCount = (TextView) findViewById(R.id.tv_monthFinishCount);
        mTvMonthFinish = (TextView) findViewById(R.id.tv_monthFinish);
        mTvMonthUnfinishedCount = (TextView) findViewById(R.id.tv_monthUnfinishedCount);
        mTvMonthUnfinished = (TextView) findViewById(R.id.tv_monthUnfinished);

        mTvDayAbnormalCount = (TextView) findViewById(R.id.tv_dayAbnormalCount);
        mTvDayAbnormal = (TextView) findViewById(R.id.tv_dayAbnormal);
        mTvMonthAbnormalCount = (TextView) findViewById(R.id.tv_monthAbnormalCount);
        mTvMonthAbnormal = (TextView) findViewById(R.id.tv_monthAbnormal);

    }

    @Override
    public void initListener() {

        mTvAll.setOnClickListener(this);

        mTvDayFinish.setOnClickListener(this);
        mTvDayUnfinished.setOnClickListener(this);

        mTvMonthFinish.setOnClickListener(this);
        mTvMonthUnfinished.setOnClickListener(this);

        mTvDayAbnormal.setOnClickListener(this);
        mTvMonthAbnormal.setOnClickListener(this);
    }

    @Override
    public void initData() {


        showLoadingDialog("","正在加载数据...");
        // 从数据库中加载数据
        taskPresenter1.readDbToBeanForAcceptance(readObserver);

    }



    @Override
    public void onClick(View v) {

        Intent intent;
            switch (v.getId()) {
                case R.id.btn_back_left:

                    finish();
                    break;

                case R.id.btn_menu_right:
                    break;


                case R.id.tv_all:                               // 总户数
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.all.toString());
                    startActivity(intent);
                    break;

                case R.id.tv_dayFinish:                         // 日冻结复核--已完成
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.dayFinish.toString());
                    startActivity(intent);
                    break;

                case R.id.tv_dayUnfinished:                     // 日冻结复核--未完成
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.dayUnfinished.toString());
                    startActivity(intent);
                    break;

                case R.id.tv_monthFinish:                      // 月冻结复核--已完成
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.monthFinish.toString());
                    startActivity(intent);
                    break;

                case R.id.tv_monthUnfinished:                  // 月冻结复核--未完成
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.monthUnfinished.toString());
                    startActivity(intent);
                    break;

                case R.id.tv_dayAbnormal:                   // 日冻结复核--异常
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.dayAbnormal.toString());
                    startActivity(intent);
                    break;

                case R.id.tv_monthAbnormal:                  // 月冻结复核--异常
                    intent = new Intent(StatisticsAcceptanceActivity.this, StatisticsAcceptanceDetailsActivity.class);
                    intent.putExtra("type", AcceptanceAdapter.Type.monthAbnormal.toString());
                    startActivity(intent);
                    break;

            }

    }






    /**
     * 将数据从数据库读取到内存中
     * rxjava -- 主线程
     */
    Observer readObserver = new Observer<List<AcceptanceBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<AcceptanceBean> beanList) {
            LogUtils.i("beanList.size()" + beanList.size());
            MyApplication.setAcceptanceBeanList(beanList);

            int allCount = 0;

            int dayFinishCount = 0;
            int dayUnFinishedCount = 0;

            int monthFinishCount = 0;
            int monthUnFinishedCount = 0;

            int dayAbnormalCount = 0;
            int monthAbnormalCount = 0;

            for(AcceptanceBean bean : beanList){
                allCount++;

                if(bean.isFinishByDay()) {
                    dayFinishCount++;
                    if(!bean.getConclusionByDay().equals("正常"))
                        dayAbnormalCount++;
                } else {
                    dayUnFinishedCount++;
                }

                if(bean.isFinishByMonth()){
                    monthFinishCount++;
                    if(!bean.getConclusionByMonth().equals("正常"))
                        monthAbnormalCount++;
                }else {
                    monthUnFinishedCount++;
                }

            }

            mTvAllCount.setText(allCount + "户");
            mTvDayFinishCount.setText(dayFinishCount + "户");
            mTvDayUnfinishedCount.setText(dayUnFinishedCount + "户");

            mTvMonthFinishCount.setText(monthFinishCount + "户");
            mTvMonthUnfinishedCount.setText(monthUnFinishedCount + "户");

            mTvDayAbnormalCount.setText(dayAbnormalCount + "户");
            mTvMonthAbnormalCount.setText(monthAbnormalCount + "户");

            if(allCount == 0) {
                mTvAll.setEnabled(false);
            }
            if(dayFinishCount == 0){
                mTvDayFinish.setEnabled(false);
            }
            if(dayUnFinishedCount == 0){
                mTvDayUnfinished.setEnabled(false);
            }
            if(monthFinishCount == 0){
                mTvMonthFinish.setEnabled(false);
            }
            if(monthUnFinishedCount == 0){
                mTvMonthUnfinished.setEnabled(false);
            }

            if(dayAbnormalCount == 0){
                mTvDayAbnormal.setEnabled(false);
            }
            if(monthAbnormalCount == 0){
                mTvMonthAbnormal.setEnabled(false);
            }

        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {
            closeDialog();
        }
    };

}
