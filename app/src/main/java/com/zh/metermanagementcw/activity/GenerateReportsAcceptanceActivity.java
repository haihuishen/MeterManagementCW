package com.zh.metermanagementcw.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AreaBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.NoWorkOrderPathBean;
import com.zh.metermanagementcw.bean.TransformerBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/6.
 */

public class GenerateReportsAcceptanceActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    Button mBtnGenerateReports;

    List<AcceptanceBean> mAcceptanceBeanList = new ArrayList<>();

    String excelPathByDay = "";
    String excelPathByMonth = "";

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

                taskPresenter1.readDbToBeanForAcceptance(readObserver);
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
            mAcceptanceBeanList = beanList;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("readObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }

        @Override
        public void onComplete() {

            excelPathByDay = Constant.Acceptance_ExportExcel_Day_PATH + "日冻结验收数据.xls";
            excelPathByMonth = Constant.Acceptance_ExportExcel_Month_PATH + "月冻结验收数据.xls";

            taskPresenter1.generateReportsAcceptance(generateReportsAcceptanceObserver,
                    getContext(),
                    mAcceptanceBeanList,
                    excelPathByDay,
                    excelPathByMonth);
            closeDialog();
        }
    };


    /**
     * 生成excel
     *
     * rxjava -- 主线程
     */
    Observer generateReportsAcceptanceObserver = new Observer<Boolean>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Boolean b) {
            showToast(b ? "生成文件成功" : "生成文件失败");

            FilesUtils.broadCreateFile(getContext(), excelPathByDay);
            FilesUtils.broadCreateFile(getContext(), excelPathByMonth);
        }


        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("generateReportsAcceptanceObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }

        @Override
        public void onComplete() {
            closeDialog();
        }
    };


}
