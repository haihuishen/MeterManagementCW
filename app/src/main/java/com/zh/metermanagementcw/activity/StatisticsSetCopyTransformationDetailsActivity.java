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
import com.zh.metermanagementcw.adapter.AssetsNumberMismatchesAdapter;
import com.zh.metermanagementcw.adapter.ConcentratorAdapter;
import com.zh.metermanagementcw.adapter.FinishedAdapter;
import com.zh.metermanagementcw.adapter.TransformerAdapter;
import com.zh.metermanagementcw.adapter.UnFinishedAdapter;
import com.zh.metermanagementcw.application.MyApplication;
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

public class StatisticsSetCopyTransformationDetailsActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;


    /******************************全屏图片***********************************/
    View mLlayoutParent;
    View mIvBg;
    PhotoView mPvBg;
    Info mInfo;

    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    /** 包裹预览图片的控件*/
    private LinearLayout mLayoutPv;
    /** 预览图片控件*/
    private PhotoView mPvCamaraPhoto;

    ListView mListView;
    ListView mOtherListView;

    private Bitmap mBitmap;

    ArrayList<CollectorNumberBean> mCollectorNumberBeanList = new ArrayList<CollectorNumberBean>();

    FinishedAdapter mFinishedAdapter;
    @Override
    public int getContentLayout() {
        return R.layout.activity_statistics_set_copy_transformation_details;
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
        mListView = (ListView) findViewById(R.id.lv_info);
        mOtherListView = (ListView) findViewById(R.id.lv_assetsNumber_Mismatches);

        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.bg);
        mPvBg = (PhotoView) findViewById(R.id.img);

    }

    @Override
    public void initListener() {

        // 全屏图片
        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvBg.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mPvBg.enable();
        mPvBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvBg.startAnimation(out);
                setTitleIsShow(View.VISIBLE);
                mPvBg.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mLlayoutParent.setVisibility(GONE);

                    }
                });
            }
        });

    }

    @Override
    public void initData() {

        taskPresenter1.readDbToBean(statisticsObserver, MyApplication.getCurrentMeteringSection());

        ArrayList<MeterBean1> beanList = new ArrayList<>();
        List<AssetNumberBean> assetNumberBeanList = new ArrayList<>();
        List<ConcentratorBean> concentratorBeanList = new ArrayList<>();
        List<TransformerBean> transformerBeanList = new ArrayList<>();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if(type.equals(Constant.type_all)){
            beanList = (ArrayList<MeterBean1>) MyApplication.getMeterBean1List();
            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            UnFinishedAdapter unFinishedAdapter = new UnFinishedAdapter(this, beanList);
            mListView.setAdapter(unFinishedAdapter);

        }else if(type.equals(Constant.type_finish)){
            for(MeterBean1 bean : MyApplication.getMeterBean1List()){
                if(bean.isFinish()){
                    beanList.add(bean);
                    LogUtils.i("bean--isFinish" + bean.toString());
                }
            }
            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            mFinishedAdapter = new FinishedAdapter(this, beanList, mCollectorNumberBeanList,
                    new FinishedAdapter.FinishPhotoListener() {
                        @Override
                        public void onPreView(int index, String path, Info info) {
                            mInfo = info;

                            mBitmap = ImageFactory.getBitmap(path);
                            mPvBg.setImageBitmap(mBitmap);
                            mIvBg.startAnimation(in);             // 执行动画
                            mIvBg.setVisibility(View.VISIBLE);
                            mLlayoutParent.setVisibility(View.VISIBLE);
                            mPvBg.animaFrom(mInfo);

                            setTitleIsShow(View.GONE);
                        }
                    });
            mListView.setAdapter(mFinishedAdapter);

        }else if(type.equals(Constant.type_unfinished)){
            for(MeterBean1 bean : MyApplication.getMeterBean1List()){
                if(!bean.isFinish()){
                    beanList.add(bean);
                }
            }
            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            UnFinishedAdapter unFinishedAdapter = new UnFinishedAdapter(this, beanList);
            mListView.setAdapter(unFinishedAdapter);

        }else if(type.equals(Constant.type_replaceMeter)){                           // 换表

            for(MeterBean1 bean : MyApplication.getMeterBean1List()){
                if(bean.isFinish()){
                    if(bean.getRelaceOrAnd().equals("0"))
                        beanList.add(bean);
                }
            }
            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            mFinishedAdapter = new FinishedAdapter(this, beanList, mCollectorNumberBeanList,
                    new FinishedAdapter.FinishPhotoListener() {
                        @Override
                        public void onPreView(int index, String path, Info info) {
                            mInfo = info;

                            mBitmap = ImageFactory.getBitmap(path);
                            mPvBg.setImageBitmap(mBitmap);
                            mIvBg.startAnimation(in);             // 执行动画
                            mIvBg.setVisibility(View.VISIBLE);
                            mLlayoutParent.setVisibility(View.VISIBLE);
                            mPvBg.animaFrom(mInfo);

                            setTitleIsShow(View.GONE);
                        }
                    });
            mListView.setAdapter(mFinishedAdapter);

        }else if(type.equals(Constant.type_newCollector)){                           // 加采集器

            for(MeterBean1 bean : MyApplication.getMeterBean1List()){
                if(bean.isFinish()){
                    if(bean.getRelaceOrAnd().equals("1"))
                        beanList.add(bean);
                }
            }
            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            mFinishedAdapter = new FinishedAdapter(this, beanList, mCollectorNumberBeanList,
                    new FinishedAdapter.FinishPhotoListener() {
                        @Override
                        public void onPreView(int index, String path, Info info) {
                            mInfo = info;

                            mBitmap = ImageFactory.getBitmap(path);
                            mPvBg.setImageBitmap(mBitmap);
                            mIvBg.startAnimation(in);             // 执行动画
                            mIvBg.setVisibility(View.VISIBLE);
                            mLlayoutParent.setVisibility(View.VISIBLE);
                            mPvBg.animaFrom(mInfo);

                            setTitleIsShow(View.GONE);
                        }
                    });
            mListView.setAdapter(mFinishedAdapter);

        }else if(type.equals(Constant.type_assetsNumber_Mismatches)){               // 有表,无户

            assetNumberBeanList = MyApplication.getAssetNumberBeanList();
            mListView.setVisibility(View.GONE);
            mOtherListView.setVisibility(View.VISIBLE);

            AssetsNumberMismatchesAdapter assetsNumberMismatchesAdapter =
                    new AssetsNumberMismatchesAdapter(getContext(), assetNumberBeanList);
            mOtherListView.setAdapter(assetsNumberMismatchesAdapter);

        }else if(type.equals(Constant.type_concentrator)){                          // 集中器
            concentratorBeanList = MyApplication.getConcentratorBeanList();
            mListView.setVisibility(View.GONE);
            mOtherListView.setVisibility(View.VISIBLE);

            ConcentratorAdapter concentratorAdapter =
                    new ConcentratorAdapter(getContext(), concentratorBeanList);
            mOtherListView.setAdapter(concentratorAdapter);

        }else if(type.equals(Constant.type_transformer)){                           // 变压器

            transformerBeanList = MyApplication.getTransformerBeanList();
            mListView.setVisibility(View.GONE);
            mOtherListView.setVisibility(View.VISIBLE);

            TransformerAdapter transformerAdapter =
                    new TransformerAdapter(getContext(), transformerBeanList);
            mOtherListView.setAdapter(transformerAdapter);

        }else if(type.equals(Constant.type_old_addrAndassetsNumber_Mismatches)){        // 旧表地址和编码不匹配

            for(MeterBean1 bean : MyApplication.getMeterBean1List()){
                if(bean.isFinish()){

                    if(bean.getRelaceOrAnd().equals("1")){

                    }else if(bean.getRelaceOrAnd().equals("0")){
                        if(bean.isOldAddrAndAsset())
                            beanList.add(bean);
                    }
                }
            }

            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            mFinishedAdapter = new FinishedAdapter(this, beanList, mCollectorNumberBeanList,
                    new FinishedAdapter.FinishPhotoListener() {
                        @Override
                        public void onPreView(int index, String path, Info info) {
                            mInfo = info;

                            mBitmap = ImageFactory.getBitmap(path);
                            mPvBg.setImageBitmap(mBitmap);
                            mIvBg.startAnimation(in);             // 执行动画
                            mIvBg.setVisibility(View.VISIBLE);
                            mLlayoutParent.setVisibility(View.VISIBLE);
                            mPvBg.animaFrom(mInfo);

                            setTitleIsShow(View.GONE);
                        }
                    });
            mListView.setAdapter(mFinishedAdapter);

        }else if(type.equals(Constant.type_new_addrAndassetsNumber_Mismatches)){        // 新表地址和编码不匹配
            for(MeterBean1 bean : MyApplication.getMeterBean1List()){
                if(bean.isFinish()){
                    if(bean.getRelaceOrAnd().equals("1")){

                    }else if(bean.getRelaceOrAnd().equals("0")){
                        if(bean.isNewAddrAndAsset())
                            beanList.add(bean);
                    }
                }
            }

            mListView.setVisibility(View.VISIBLE);
            mOtherListView.setVisibility(View.GONE);

            mFinishedAdapter = new FinishedAdapter(this, beanList, mCollectorNumberBeanList,
                    new FinishedAdapter.FinishPhotoListener() {
                        @Override
                        public void onPreView(int index, String path, Info info) {
                            mInfo = info;

                            mBitmap = ImageFactory.getBitmap(path);
                            mPvBg.setImageBitmap(mBitmap);
                            mIvBg.startAnimation(in);             // 执行动画
                            mIvBg.setVisibility(View.VISIBLE);
                            mLlayoutParent.setVisibility(View.VISIBLE);
                            mPvBg.animaFrom(mInfo);

                            setTitleIsShow(View.GONE);
                        }
                    });
            mListView.setAdapter(mFinishedAdapter);

        }

        mTvTitle.setText(type);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;


            case R.id.img:                                          // 点击"放大后的预览图片的控件"，缩小、隐藏那个预览布局
                mIvBg.startAnimation(out);
                setTitleIsShow(View.VISIBLE);
                mPvBg.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mLlayoutParent.setVisibility(View.GONE);

                    }
                });
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

            AssetsNumberMismatchesAdapter assetsNumberMismatchesAdapter =
                    new AssetsNumberMismatchesAdapter(StatisticsSetCopyTransformationDetailsActivity.this, assetNumberBeen);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            closeDialog();
        }

        @Override
        public void onComplete() {
            taskPresenter1.getCollectorList(getCollctorListObserver, MyApplication.getCurrentMeteringSection());
            //closeDialog();
        }
    };

    /**
     * 获取采集器列表
     *
     * rxjava -- 主线程
     */
    Observer getCollctorListObserver = new Observer<List<CollectorNumberBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<CollectorNumberBean> collectorNumberBeen) {
            mCollectorNumberBeanList = (ArrayList<CollectorNumberBean>) collectorNumberBeen;

            if(mFinishedAdapter!=null){
                mFinishedAdapter.setCollectorNumberBeanList(mCollectorNumberBeanList);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("getCollctorObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }


        @Override
        public void onComplete() {

            //closeDialog();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {                 // 如果点击的是"返回按钮"

            if(mLlayoutParent.getVisibility() == View.VISIBLE && mIvBg.getVisibility() == View.VISIBLE){   // 缩小、隐藏那个预览布局
                mIvBg.startAnimation(out);
                setTitleIsShow(View.VISIBLE);
                mPvBg.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mLlayoutParent.setVisibility(View.GONE);

                    }
                });
                return true;
            }

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
