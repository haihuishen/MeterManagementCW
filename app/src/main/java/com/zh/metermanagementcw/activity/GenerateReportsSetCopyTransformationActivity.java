package com.zh.metermanagementcw.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AreaBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.NoWorkOrderPathBean;
import com.zh.metermanagementcw.bean.TransformerBean;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.LogUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/6.
 */

public class GenerateReportsSetCopyTransformationActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    Button mBtnGenerateReports;

    List<MeterBean1> mMeterBeenList;
    /** 查询出,有表无户的 */
    List<AssetNumberBean> mAssetNumberBeenList;
    /** 获取当前台区的--集中器 */
    List<ConcentratorBean> mConcentratorBeenList;
    /** 获取当前台区的--变压器 */
    List<TransformerBean> mTransformerBeanList;


    /** .../../XX供电所XX台区(抄表区段)户表改造台账.xls */
    String excelPath1 = "";
    /** .../../xx供电所xx台区(抄表区段)集中器户表档案(生成-计量自动化系统).xls */
    String excelPath2 = "";
    /** .../../xx供电所xx台区(抄表区段)户表档案(生成-营销系统).xls */
    String excelPath3 = "";

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

                taskPresenter1.readDbToBean(readObserver, MyApplication.getCurrentMeteringSection());
                // 查询出,有表无户的
                taskPresenter1.statisticsData(statisticsObserver);
                // 获取当前台区的--集中器
                taskPresenter1.getConcentrator(concentratorObserver, MyApplication.getCurrentMeteringSection());
                // 获取当前台区的--变压器
                taskPresenter1.getTransformer(transformerObserver, MyApplication.getCurrentMeteringSection());


                NoWorkOrderPathBean orderPathbean = MyApplication.getNoWorkOrderPath();
                AreaBean areaBean = MyApplication.getAreaBean();

                /** .../../XX供电所XX台区（抄表区段）户表改造台账.xls */
                excelPath1 = orderPathbean.getAreaExportPath()
                        + areaBean.getPowerSupplyBureau()
                        + areaBean.getCourts()
                        + "(" + areaBean.getTheMeteringSection() + ")"
                        + "户表改造台账.xls";
                /** .../../xx供电所xx台区(抄表区段)集中器户表档案(生成-计量自动化系统).xls */
                excelPath2 = orderPathbean.getAreaExportPath()
                        + areaBean.getPowerSupplyBureau()
                        + areaBean.getCourts()
                        + "(" + areaBean.getTheMeteringSection() + ")"
                        + "集中器户表档案(生成-计量自动化系统).xls";
                /** .../../xx供电所xx台区(抄表区段)户表档案(生成-营销系统).xls */
                excelPath3 = orderPathbean.getAreaExportPath()
                        + areaBean.getPowerSupplyBureau()
                        + areaBean.getCourts()
                        + "(" + areaBean.getTheMeteringSection() + ")"
                        + "户表档案(生成-营销系统).xls";


                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        taskPresenter1.generateReportsSetCopyTransformation(generateReportsObserver, getContext(),
                                mMeterBeenList,
                                mAssetNumberBeenList,
                                mConcentratorBeenList,
                                mTransformerBeanList,
                                excelPath1, excelPath2, excelPath3);
                    };
                }, 3000);

                break;
        }
    }



    /**
     * rxjava -- 主线程
     */
    Observer statisticsObserver = new Observer<List<AssetNumberBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<AssetNumberBean> assetNumberBeen) {
            MyApplication.setAssetNumberBeanList(assetNumberBeen);
            mAssetNumberBeenList = assetNumberBeen;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {

        }
    };


    /**
     * 将数据从数据库读取到内存中
     * rxjava -- 主线程
     */
    Observer readObserver = new Observer<List<MeterBean1>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<MeterBean1> meterBeen) {
            MyApplication.setMeterBean1List(meterBeen);
            mMeterBeenList = meterBeen;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {

        }
    };

    /**
     * 获取当前台区的--集中器
     *
     * rxjava -- 主线程
     */
    Observer concentratorObserver = new Observer<List<ConcentratorBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<ConcentratorBean> concentratorBeen) {
            MyApplication.setConcentratorBeanList(concentratorBeen);
            mConcentratorBeenList = concentratorBeen;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {

        }
    };


    /**
     * 获取当前台区的--变压器
     *
     * rxjava -- 主线程
     */
    Observer transformerObserver = new Observer<List<TransformerBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<TransformerBean> transformerBeen) {
            MyApplication.setTransformerBeanList(transformerBeen);
            mTransformerBeanList = transformerBeen;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {

        }
    };


    /**
     * 生成excel
     *
     * rxjava -- 主线程
     */
    Observer generateReportsObserver = new Observer<Boolean>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Boolean b) {
            showToast(b?"生成文件成功" : "生成文件失败");
            FilesUtils.broadCreateFile(getContext(), excelPath1);
            FilesUtils.broadCreateFile(getContext(), excelPath2);
            FilesUtils.broadCreateFile(getContext(), excelPath3);
        }

        @Override
        public void onError(@NonNull Throwable e) {

            LogUtils.i("generateReportsObserver:e.getMessage()" + e.getMessage());
            closeDialog();
        }

        @Override
        public void onComplete() {
            closeDialog();
        }
    };

}
