package com.zh.metermanagementcw.activity;

import android.graphics.Bitmap;
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
import com.zh.metermanagementcw.adapter.AcceptanceAdapter;
import com.zh.metermanagementcw.adapter.ScatteredNewMeterAdapter;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
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
 * 零散新装
 *
 */
public class SearchScatteredNewMeterActivity extends BaseActivity implements View.OnClickListener {

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
    /** 资产编码 -- 编辑框 */
    ClearEditText mCEtAssetsNumber;

    Button mBtnAssetsNumber;

    /** 收起和展开--查询条件 */
    ToggleButton mTbUpAndDowm;

    LinearLayout LlayoutSearch;


    /** 查询 -- 按钮 */
    Button mBtnSearch;

    //--------------------------------------------------
    ScatteredNewMeterAdapter mScatteredNewMeterAdapter;
    ListView mListView;

    ArrayList<ScatteredNewMeterBean> mScatteredNewMeterBeanList = new ArrayList<>();


    //--------------------------------------------------
    /** 成为 二维扫描 */
    Barcode2DWithSoft mBarcode2DWithSoft;
    ScanBack mScanBack;
    /** 当前二维扫描的按钮 */
    private int mCurrentScanBtnId = 1;

    public BeepManager mBeepManager;

    //--------------------------图片-----------------------------
    public Bitmap mBitmap;

    public View mLlayoutParent;
    public View mIvBg;
    /** 放大后存放图片的控件*/
    public PhotoView mPvBgImg;
    public Info mInfo;

    public AlphaAnimation in;
    public AlphaAnimation out;




    @Override
    public int getContentLayout() {
        return R.layout.activity_search_scatter_new_meter;
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
        mCEtAssetsNumber = (ClearEditText) findViewById(R.id.et_assetsNumber);

        mBtnAssetsNumber = (Button) findViewById(R.id.btn_assetsNumber);

        mTbUpAndDowm = (ToggleButton) findViewById(R.id.tb_upAndDown);
        LlayoutSearch = (LinearLayout) findViewById(R.id.llayout_search);

        mBtnSearch = (Button) findViewById(R.id.btn_search);

        //--------------------------------------------------
        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.iv_bg);
        mPvBgImg = (PhotoView) findViewById(R.id.pv_bg);

        mListView = (ListView) findViewById(R.id.lv_info);
    }

    @Override
    public void initListener() {
        mBtnSearch.setOnClickListener(this);
        mBtnAssetsNumber.setOnClickListener(this);

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


        //-------------------------------------------------------------------
        mScatteredNewMeterBeanList = new ArrayList<>();
        mScatteredNewMeterAdapter = new ScatteredNewMeterAdapter(getContext(), mScatteredNewMeterBeanList,
                new ScatteredNewMeterAdapter.PrePhotoListener() {
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

        mListView.setAdapter(mScatteredNewMeterAdapter);

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
    }


    /**
     * 查询
     */
    private void search() {

        HashMap<String, String> conditionMap = new HashMap<>();

        String userName = mCEtUserName.getText().toString().trim();
        String userNumber = mCEtUserNumber.getText().toString().trim();
        String assetsNumber = mCEtAssetsNumber.getText().toString().trim();


        boolean isEmpty = true;
        if(StringUtils.isNotEmpty(userName)){
            conditionMap.put("userName", userName);
            isEmpty = false;
        }
        if(StringUtils.isNotEmpty(userNumber)){
            conditionMap.put("userNumber", userNumber);
            isEmpty = false;
        }
        if(StringUtils.isNotEmpty(assetsNumber)){
            conditionMap.put("assetNumbers", assetsNumber);
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

            taskPresenter1.searchAcceptance(searchScatteredNewMeterObserver, conditionMap);
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

            }else{
                LogUtils.i("扫描成功");
                String barCode = new String(bytes, 0, length);

                mBeepManager.playSuccessful();
                final String scanData = barCode.trim();
                if(mCurrentScanBtnId == R.id.btn_assetsNumber){              // 旧表资产编号(二维扫描)
                    mCEtAssetsNumber.setText(scanData);

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
    Observer searchScatteredNewMeterObserver = new Observer<List<ScatteredNewMeterBean>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<ScatteredNewMeterBean> beanList) {
            if(beanList != null && beanList.size() > 0) {
                mTbUpAndDowm.setChecked(false);
                mScatteredNewMeterBeanList.clear();
                mScatteredNewMeterBeanList.addAll(beanList);

                mScatteredNewMeterAdapter.notifyDataSetChanged();
            }else {
                showToast("无此用户");
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("searchScatteredNewMeterObserver -- e.getMessage()" + e.getMessage());
        }

        @Override
        public void onComplete(){

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

            case R.id.btn_assetsNumber:
                mCurrentScanBtnId = R.id.btn_assetsNumber;
                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.pv_bg:                                                // 点击"放大后的预览图片的控件"，缩小、隐藏那个预览布局
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

}
