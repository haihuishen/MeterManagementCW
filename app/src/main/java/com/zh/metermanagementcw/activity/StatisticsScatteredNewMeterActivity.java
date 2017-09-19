package com.zh.metermanagementcw.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/3.
 */

public class StatisticsScatteredNewMeterActivity extends BaseActivity implements View.OnClickListener {

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



    @Override
    public int getContentLayout() {
        return R.layout.activity_statistics_scattered_new_meter;
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

    }

    @Override
    public void initListener() {
        mTvAll.setOnClickListener(this);
    }

    @Override
    public void initData() {


        showLoadingDialog("","正在加载数据...");
        // 从数据库中加载数据
        taskPresenter1.readDbToBeanForScatteredNewMeter(readForScatteredNewMeterObserver);

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
                    intent = new Intent(getContext(), StatisticsScatteredNewMeterDetailsActivity.class);
                    startActivity(intent);
                    break;

            }

    }






    /**
     * 将数据从数据库读取到内存中
     * rxjava -- 主线程
     */
    Observer readForScatteredNewMeterObserver = new Observer<List<ScatteredNewMeterBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<ScatteredNewMeterBean> beanList) {
            LogUtils.i("beanList.size()" + beanList.size());

            MyApplication.setScatteredNewMeterBeanList(beanList);

            int allCount = 0;

            for(ScatteredNewMeterBean bean : beanList){
                allCount++;
            }

            mTvAllCount.setText(allCount + "户");

            if(allCount == 0) {
                mTvAll.setEnabled(false);
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
