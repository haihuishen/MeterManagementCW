package com.zh.metermanagementcw.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
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

public class StatisticsSetCopyTransformationActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 台区总数 -- 文本 */
    private TextView mTvAreaCount;
    /** 台区总数 -- 查看详情 -- 文本 */
    private TextView mTvArea;
    /** 已换表 -- 文本 */
    private TextView mTvFinishCount;
    /** 已换表 -- 查看详情 -- 文本 */
    private TextView mTvFinish;
    /** 未换表 -- 文本 */
    private TextView mTvUnfinishedCount;
    /** 未换表 -- 查看详情 -- 文本 */
    private TextView mTvUnfinished;
    /** 资产编码无匹配 -- 文本 */
    private TextView mTvAssetsNumberMismatchesCount;
    /** 资产编码无匹配 -- 查看详情 -- 文本 */
    private TextView mTvAssetsNumberMismatches;

    /** 换表 -- 文本 */
    private TextView mTvReplaceMeterCount;
    /** 换表 -- 查看详情 -- 文本 */
    private TextView mTvReplaceMeter;
    /** 加装采集器 -- 文本 */
    private TextView mTvNewCollectorCount;
    /** 加装采集器 -- 查看详情 -- 文本 */
    private TextView mTvNewCollector;


    /** 旧表地址\n资产编码\n无匹配 -- 文本 */
    private TextView mTvOldAddrAndassetsNumberMismatchesCount;
    /** 旧表地址\n资产编码\n无匹配 -- 查看详情 -- 文本 */
    private TextView mTvOldAddrAndassetsNumberMismatches;
    /** 新表地址\n资产编码\n无匹配 -- 文本 */
    private TextView mTvNewAddrAndassetsNumberMismatchesCount;
    /** 新表地址\n资产编码\n无匹配 -- 查看详情 -- 文本 */
    private TextView mTvNewAddrAndassetsNumberMismatches;


    /** 集中器 -- 查看详情 -- 文本 -- tv_concentratorCount*/
    private TextView mTvConcentratorCount;
    /** 集中器 -- 查看详情 -- 文本 -- tv_concentrator*/
    private TextView mTvConcentrator;

    /** 变压器 -- 查看详情 -- 文本 -- tv_transformerCount*/
    private TextView mTvTransformerCount;
    /** 变压器 -- 查看详情 -- 文本 -- tv_transformer*/
    private TextView mTvTransformer;

    @Override
    public int getContentLayout() {
        return R.layout.activity_statistics_set_copy_transformation;
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

        mTvAreaCount = (TextView) findViewById(R.id.tv_areaCount);
        mTvArea = (TextView) findViewById(R.id.tv_area);
        mTvFinishCount = (TextView) findViewById(R.id.tv_finishCount);
        mTvFinish = (TextView) findViewById(R.id.tv_finish);
        mTvUnfinishedCount = (TextView) findViewById(R.id.tv_unfinishedCount);
        mTvUnfinished = (TextView) findViewById(R.id.tv_unfinished);

        mTvReplaceMeterCount = (TextView) findViewById(R.id.tv_replaceMeterCount);
        mTvReplaceMeter = (TextView) findViewById(R.id.tv_replaceMeter);
        mTvNewCollectorCount = (TextView) findViewById(R.id.tv_newCollectorCount);
        mTvNewCollector = (TextView) findViewById(R.id.tv_newCollector);

        mTvAssetsNumberMismatchesCount = (TextView) findViewById(R.id.tv_assetsNumber_MismatchesCount);
        mTvAssetsNumberMismatches = (TextView) findViewById(R.id.tv_assetsNumber_Mismatches);
        mTvOldAddrAndassetsNumberMismatchesCount = (TextView) findViewById(R.id.tv_old_addrAndassetsNumber_MismatchesCount);
        mTvOldAddrAndassetsNumberMismatches = (TextView) findViewById(R.id.tv_old_addrAndassetsNumber_Mismatches);
        mTvNewAddrAndassetsNumberMismatchesCount = (TextView) findViewById(R.id.tv_new_addrAndassetsNumber_MismatchesCount);
        mTvNewAddrAndassetsNumberMismatches = (TextView) findViewById(R.id.tv_new_addrAndassetsNumber_Mismatches);

        mTvConcentratorCount = (TextView) findViewById(R.id.tv_concentratorCount);
        mTvConcentrator = (TextView) findViewById(R.id.tv_concentrator);

        mTvTransformerCount = (TextView) findViewById(R.id.tv_transformerCount);
        mTvTransformer = (TextView) findViewById(R.id.tv_transformer);
    }

    @Override
    public void initListener() {
        mTvArea.setOnClickListener(this);
        mTvFinish.setOnClickListener(this);
        mTvUnfinished.setOnClickListener(this);

        mTvReplaceMeter.setOnClickListener(this);
        mTvNewCollector.setOnClickListener(this);

        mTvAssetsNumberMismatches.setOnClickListener(this);
        mTvOldAddrAndassetsNumberMismatches.setOnClickListener(this);
        mTvNewAddrAndassetsNumberMismatches.setOnClickListener(this);

        mTvConcentrator.setOnClickListener(this);
        mTvTransformer.setOnClickListener(this);
    }

    @Override
    public void initData() {


        showLoadingDialog("","正在加载数据...");
        // 从数据库中加载数据
        taskPresenter1.readDbToBean(readObserver, MyApplication.getCurrentMeteringSection());

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

                case R.id.tv_area:              // 台区总数
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_all);
                    startActivity(intent);
                    break;

                case R.id.tv_finish:            // 完成
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_finish);
                    startActivity(intent);
                    break;

                case R.id.tv_unfinished:        // 未完成
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_unfinished);
                    startActivity(intent);
                    break;

                case R.id.tv_replaceMeter:      // 换表
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_replaceMeter);
                    startActivity(intent);
                    break;

                case R.id.tv_newCollector:      // 加采集器
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_newCollector);
                    startActivity(intent);
                    break;

                case R.id.tv_concentrator:                  // 集中器
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_concentrator);
                    startActivity(intent);
                    break;

                case R.id.tv_transformer:                  // 变压器
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_transformer);
                    startActivity(intent);
                    break;

                case R.id.tv_assetsNumber_Mismatches:       // 查询出,有表无户的
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_assetsNumber_Mismatches);
                    startActivity(intent);
                    break;


                case R.id.tv_old_addrAndassetsNumber_Mismatches:                  // 旧表地址和编码不匹配
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_old_addrAndassetsNumber_Mismatches);
                    startActivity(intent);
                    break;

                case R.id.tv_new_addrAndassetsNumber_Mismatches:                  // 新表地址和编码不匹配
                    intent = new Intent(StatisticsSetCopyTransformationActivity.this, StatisticsSetCopyTransformationDetailsActivity.class);
                    intent.putExtra("type", Constant.type_new_addrAndassetsNumber_Mismatches);
                    startActivity(intent);
                    break;

            }

    }






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


            int allCount = 0;
            int finishCount = 0;
            int unFinishedCount = 0;

            int replaceMeterCount = 0;          // 换表
            int newCollectorCount = 0;          // 加采集器

            int oldAddrAndassetsNumberMismatchesCount = 0;
            int newAddrAndassetsNumberMismatchesCount = 0;

            LogUtils.i("meterBeen.size()" + meterBeen.size());

            MyApplication.setMeterBean1List(meterBeen);

            for(MeterBean1 bean : meterBeen){
                allCount++;
                if(bean.isFinish()) {
                    finishCount++;



                    if(bean.getRelaceOrAnd().equals("1")){
                        newCollectorCount++;


                    }else if(bean.getRelaceOrAnd().equals("0")){
                        replaceMeterCount++;

                        if(bean.isOldAddrAndAsset())
                            oldAddrAndassetsNumberMismatchesCount++;
                        if(bean.isNewAddrAndAsset())
                            newAddrAndassetsNumberMismatchesCount++;
                    }

                } else
                    unFinishedCount++;

            }
            mTvAreaCount.setText(allCount + "户");
            mTvFinishCount.setText(finishCount + "户");
            mTvUnfinishedCount.setText(unFinishedCount + "户");

            mTvReplaceMeterCount.setText(replaceMeterCount + "户");
            mTvNewCollectorCount.setText(newCollectorCount + "户");

            mTvOldAddrAndassetsNumberMismatchesCount.setText(oldAddrAndassetsNumberMismatchesCount + "户");
            mTvNewAddrAndassetsNumberMismatchesCount.setText(newAddrAndassetsNumberMismatchesCount + "户");

            if(allCount == 0) {
                mTvArea.setEnabled(false);
            }
            if(finishCount == 0){
                mTvFinish.setEnabled(false);
            }
            if(unFinishedCount == 0){
                mTvUnfinished.setEnabled(false);
            }
            if(replaceMeterCount == 0){
                mTvReplaceMeter.setEnabled(false);
            }
            if(newCollectorCount == 0){
                mTvNewCollector.setEnabled(false);
            }

            if(oldAddrAndassetsNumberMismatchesCount == 0){
                mTvOldAddrAndassetsNumberMismatches.setEnabled(false);
            }
            if(newAddrAndassetsNumberMismatchesCount == 0){
                mTvNewAddrAndassetsNumberMismatches.setEnabled(false);
            }


            // 查询出,有表无户的
            taskPresenter1.statisticsData(statisticsObserver);
            // 获取当前台区的--集中器
            taskPresenter1.getConcentrator(concentratorObserver, MyApplication.getCurrentMeteringSection());
            // 获取当前台区的--变压器
            taskPresenter1.getTransformer(transformerObserver, MyApplication.getCurrentMeteringSection());
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
     * 查询出,有表无户的
     *
     * rxjava -- 主线程
     */
    Observer statisticsObserver = new Observer<List<AssetNumberBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<AssetNumberBean> assetNumberBeen) {
            MyApplication.setAssetNumberBeanList(assetNumberBeen);

            if(assetNumberBeen.size() == 0)
                mTvAssetsNumberMismatches.setEnabled(false);


            mTvAssetsNumberMismatchesCount.setText(assetNumberBeen.size() + "户");
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

            if(concentratorBeen.size() == 0)
                mTvConcentrator.setEnabled(false);

            mTvConcentratorCount.setText(concentratorBeen.size() + "台");
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

            if(transformerBeen.size() == 0)
                mTvTransformer.setEnabled(false);

            mTvTransformerCount.setText(transformerBeen.size() + "台");
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
