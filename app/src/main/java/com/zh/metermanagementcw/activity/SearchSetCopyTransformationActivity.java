package com.zh.metermanagementcw.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.shen.sweetdialog.SweetAlertDialog;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.FinishedAdapter;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.CollectorNumberBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.BeepManager;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.StringUtils;
import com.zh.metermanagementcw.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SearchSetCopyTransformationActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;


    /** 用户名称 -- 编辑框 */
    ClearEditText mCEtUserName;
    /** 用户编码 -- 编辑框 */
    ClearEditText mCEtUserNumber;
    /** 旧资产编码 -- 编辑框 */
    ClearEditText mCEtOldAssetsNumber;
    /** 新资产编码 -- 编辑框 */
    ClearEditText mCEtNewAssetsNumber;

    Button mBtnOldAssetsNumber;
    Button mBtnNewAssetsNumber;

    /** 收起和展开--查询条件 */
    ToggleButton mTbUpAndDowm;

    LinearLayout LlayoutSearch;


    /** 查询 -- 按钮 */
    Button mBtnSearch;

    /** 查询出的信息 -- 文本 */
    TextView mTvInfo;

    //--------------------------------------------------
    FinishedAdapter mFinishedAdapter;
    ListView mListView;

    ArrayList<MeterBean1> mMeterBean1List = new ArrayList<>();

    public Bitmap mBitmap;

    public View mLlayoutParent;
    public View mIvBg;
    /** 放大后存放图片的控件*/
    public PhotoView mPvBgImg;
    public Info mInfo;

    public AlphaAnimation in;
    public AlphaAnimation out;

    //--------------------------------------------------
    /** 成为 二维扫描 */
    Barcode2DWithSoft mBarcode2DWithSoft;
    ScanBack mScanBack;
    /** 当前二维扫描的按钮 */
    private int mCurrentScanBtnId = 1;

    public BeepManager mBeepManager;

    @Override
    public int getContentLayout() {
        return R.layout.activity_search_set_copy_transformation;
    }

    @Override
    public void initTitleListener(TextView tvTitle, Button btnBack, Button btnMenu) {
        mTvTitle = tvTitle;
        mBtnBack = btnBack;
        mBtnMenu = btnMenu;

        mBtnBack.setOnClickListener(this);
        // mBtnMenu.setOnClickListener(this);
    }

    @Override
    public void initTitleData(TextView tvTitle, Button btnBack, Button btnMenu) {
        mTvTitle.setVisibility(View.VISIBLE);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnMenu.setVisibility(View.GONE);

        mTvTitle.setText("查询");
    }


    @Override
    public void initView() {

        mCEtUserName = (ClearEditText) findViewById(R.id.et_userName);
        mCEtUserNumber = (ClearEditText) findViewById(R.id.et_userNumber);
        mCEtOldAssetsNumber = (ClearEditText) findViewById(R.id.et_oldAssetsNumber);
        mCEtNewAssetsNumber = (ClearEditText) findViewById(R.id.et_newAssetsNumber);

        mBtnOldAssetsNumber = (Button) findViewById(R.id.btn_oldAssetsNumber);
        mBtnNewAssetsNumber = (Button) findViewById(R.id.btn_newAssetsNumber);

        mTbUpAndDowm = (ToggleButton) findViewById(R.id.tb_upAndDown);
        LlayoutSearch = (LinearLayout) findViewById(R.id.llayout_search);

        mBtnSearch = (Button) findViewById(R.id.btn_search);

        mTvInfo = (TextView) findViewById(R.id.tv_info);


        //--------------------------------------------------
        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.iv_bg);
        mPvBgImg = (PhotoView) findViewById(R.id.pv_bg);

        mListView = (ListView) findViewById(R.id.lv_info);
    }

    @Override
    public void initListener() {
        mBtnSearch.setOnClickListener(this);
        mBtnOldAssetsNumber.setOnClickListener(this);
        mBtnNewAssetsNumber.setOnClickListener(this);
        mPvBgImg.setOnClickListener(this);

        mTbUpAndDowm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                mTbUpAndDowm.setChecked(isChecked);
                //使用三目运算符来响应按钮变换的事件
                mTbUpAndDowm.setBackgroundResource(isChecked ? R.mipmap.dowm : R.mipmap.up);

                if(isChecked)
                    LlayoutSearch.setVisibility(View.VISIBLE);
                else
                    LlayoutSearch.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void initData() {
        mScanBack = new ScanBack();

        mTbUpAndDowm.setChecked(true);

        mBeepManager = new BeepManager(getContext(),true,false);

        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);

        mFinishedAdapter = new FinishedAdapter(getContext(), mMeterBean1List, new ArrayList<CollectorNumberBean>(),
                new FinishedAdapter.FinishPhotoListener() {
                    @Override
                    public void onPreView(int index, String path, Info info) {
                        mInfo = info;

                        mBitmap = ImageFactory.getBitmap(path);
                        mPvBgImg.setImageBitmap(mBitmap);
                        mIvBg.startAnimation(in);             // 执行动画
                        mIvBg.setVisibility(View.VISIBLE);
                        mLlayoutParent.setVisibility(View.VISIBLE);
                        mPvBgImg.animaFrom(mInfo);
                        setTitleIsShow(View.GONE);
                    }
                });
        mListView.setAdapter(mFinishedAdapter);

        //------------------------------------------------------------------

        // 预览图片的动画
        in = new AlphaAnimation(0, 1);
        out = new AlphaAnimation(1, 0);

        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mIvBg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mBitmap = ImageFactory.getBitmap(Constant.CACHE_IMAGE_PATH + "no_preview_picture.png");

        mPvBgImg.setImageBitmap(mBitmap);
        mPvBgImg.enable();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBarcode2DWithSoft != null)
            mBarcode2DWithSoft.stopScan();  // 停止
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        LogUtils.i("onDestroy");
        if(mBarcode2DWithSoft!=null){
            mBarcode2DWithSoft.stopScan();
            mBarcode2DWithSoft.close();
        }
        super.onDestroy();
        //android.os.Process.killProcess(java.lang.Process.myPid());
    }


    /**
     * 查询
     */
    private void search() {

        HashMap<String, String> conditionMap = new HashMap<>();

        String userName = mCEtUserName.getText().toString().trim();
        String userNumber = mCEtUserNumber.getText().toString().trim();
        String oldAssetsNumber = mCEtOldAssetsNumber.getText().toString().trim();
        String newAssetsNumber = mCEtNewAssetsNumber.getText().toString().trim();

        boolean isEmpty = true;
        if(StringUtils.isNotEmpty(userName)){
            conditionMap.put("userName", userName);
            isEmpty = false;
        }
        if(StringUtils.isNotEmpty(userNumber)){
            conditionMap.put("userNumber", userNumber);
            isEmpty = false;
        }
        if(StringUtils.isNotEmpty(oldAssetsNumber)){
            conditionMap.put("oldAssetNumbers", oldAssetsNumber);
            isEmpty = false;
        }
        if(StringUtils.isNotEmpty(newAssetsNumber)){
            conditionMap.put("newAssetNumbersScan", newAssetsNumber);
            isEmpty = false;
        }

        if(isEmpty){
            SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("提示")
                    .setContentText("查询条件不能为空")
                    .setConfirmText("确认")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismiss();
                        }
                    });

            dialog.show();
        }else {
            taskPresenter1.searchMeterInfo(searchMeterInfoObserver,
                    MyApplication.getCurrentMeteringSection(),
                    conditionMap);
        }
    }


    /**
     * 成为扫描返回
     */
    private final class ScanBack implements  Barcode2DWithSoft.ScanCallback{
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
                LogUtils.i("扫描失败");
                mBeepManager.playError();
                //showToast("扫描失败");
//                if (length == -1) {
//                    tvData.setText("Scan cancel");
//                } else if (length == 0) {
//                    tvData.setText("Scan TimeOut");
//                } else {
//                    Log.i(TAG,"Scan fail");
//                    //Toast.makeText(MainActivity.this,"Scan fail",Toast.LENGTH_SHORT).show();
//                }
            }else{
                LogUtils.i("扫描成功");
                String barCode = new String(bytes, 0, length);

                mBeepManager.playSuccessful();
                final String scanData = barCode.trim();
                if(mCurrentScanBtnId == R.id.btn_oldAssetsNumber){              // 旧表资产编号(二维扫描)
                    mCEtOldAssetsNumber.setText(scanData);

                }else if(mCurrentScanBtnId == R.id.btn_newAssetsNumber){    // 新表资产编号(二维扫描)
                    mCEtNewAssetsNumber.setText(scanData);

                }
            }
            //scaning=false;
        }
    }

    /**
     * 查询表信息
     *
     * rxjava -- 主线程
     */
    Observer initBarcode2DSObserver = new Observer<Barcode2DWithSoft>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Barcode2DWithSoft barcode2DWithSoft) {
            if(barcode2DWithSoft == null)
                LogUtils.i("二维初始化失败" );
            else {
                LogUtils.i("二维初始化成功" );
                mBarcode2DWithSoft = barcode2DWithSoft;
            }
        }



        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("initBarcode2DSObserver -- e.getMessage()" + e.getMessage());
        }

        @Override
        public void onComplete(){


        }
    };


    /**
     * 查询表信息
     *
     * rxjava -- 主线程
     */
    Observer searchMeterInfoObserver = new Observer<List<MeterBean1>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<MeterBean1> meterBeen) {

            mTvTitle.setText("查询结果");

            if(meterBeen != null && meterBeen.size() > 0) {
                mTbUpAndDowm.setChecked(false);
//            for(MeterBean1 bean : meterBeen){
//                LogUtils.i("searchMeterInfoObserver :" + bean.toString());
//            }
                mMeterBean1List.clear();
                mMeterBean1List.addAll(meterBeen);
                mFinishedAdapter.notifyDataSetChanged();
            }else {
                showToast("无此用户");
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("searchMeterInfoObserver -- e.getMessage()" + e.getMessage());
        }

        @Override
        public void onComplete(){

            taskPresenter1.getCollectorList(getCollctorListObserver, MyApplication.getCurrentMeteringSection());
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
            ArrayList<CollectorNumberBean> collectorNumberBeanList = (ArrayList<CollectorNumberBean>) collectorNumberBeen;

            if(mFinishedAdapter!=null){
                mFinishedAdapter.setCollectorNumberBeanList(collectorNumberBeanList);
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
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btn_back_left:
                finish();
                break;

            case R.id.btn_menu_right:

                break;

            case R.id.btn_search:
                search();
                break;

            case R.id.btn_oldAssetsNumber:
                mCurrentScanBtnId = R.id.btn_oldAssetsNumber;
                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.btn_newAssetsNumber:
                mCurrentScanBtnId = R.id.btn_newAssetsNumber;
                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.pv_bg:                                          // 点击"放大后的预览图片的控件"，缩小、隐藏那个预览布局
                mIvBg.startAnimation(out);
                setTitleIsShow(View.VISIBLE);
                mPvBgImg.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mLlayoutParent.setVisibility(View.GONE);
                    }
                });
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {                 // 如果点击的是"返回按钮"

            if(mLlayoutParent.getVisibility() == View.VISIBLE && mIvBg.getVisibility() == View.VISIBLE){   // 缩小、隐藏那个预览布局
                mIvBg.startAnimation(out);
                setTitleIsShow(View.VISIBLE);
                mPvBgImg.animaTo(mInfo, new Runnable() {
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
