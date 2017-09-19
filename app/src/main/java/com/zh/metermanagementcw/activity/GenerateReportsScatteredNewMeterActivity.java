package com.zh.metermanagementcw.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/6.
 */

public class GenerateReportsScatteredNewMeterActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    Button mBtnGenerateReports;

    List<ScatteredNewMeterBean> mScatteredNewMeterBeanList = new ArrayList<>();

    String mExcelPath = "";

    @Override
    public int getContentLayout() {
        return R.layout.activity_generate_reports;
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

        mTvTitle.setText("生成报表");
    }

    @Override
    public void initView() {
        mBtnGenerateReports = (Button) findViewById(R.id.btn_generateReports);
    }

    @Override
    public void initListener() {
        mBtnGenerateReports.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;

            case R.id.btn_generateReports:      // 生成报表

                showLoadingDialog("", "正在生成报表");
                // 从数据库中加载数据
                taskPresenter1.readDbToBeanForScatteredNewMeter(readForScatteredNewMeterObserver);
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
            LogUtils.i("readForScatteredNewMeterObserver -- beanList.size()" + beanList.size());
            MyApplication.setScatteredNewMeterBeanList(beanList);
            mScatteredNewMeterBeanList = beanList;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {
            mExcelPath = Constant.ScatteredNewMeter_ExportExcel_Day_PATH + "零散新装.xls";

            taskPresenter1.generateReportsScatteredNewMeter(
                    generateReportsScatteredNewMeterObserver, getContext(),
                    mScatteredNewMeterBeanList, mExcelPath);
        }
    };

    /**
     * 生成excel
     *
     * rxjava -- 主线程
     */
    Observer generateReportsScatteredNewMeterObserver = new Observer<Boolean>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Boolean b) {
            showToast(b ? "生成文件成功" : "生成文件失败");

            FilesUtils.broadCreateFile(getContext(), mExcelPath);
        }


        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("generateReportsScatteredNewMeterObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }

        @Override
        public void onComplete() {
            closeDialog();
        }
    };


}
