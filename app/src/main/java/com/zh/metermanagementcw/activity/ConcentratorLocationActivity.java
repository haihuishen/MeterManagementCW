package com.zh.metermanagementcw.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.shen.sweetdialog.SweetAlertDialog;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.PicAdapter;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.ExcelUtil;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LocationUtils;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.StringUtils;
import com.zh.metermanagementcw.view.ClearEditText;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 *
 * 无工单 -- 集中器(百度获取定位)
 *
 */
public class ConcentratorLocationActivity extends BaseActivity implements View.OnClickListener {

    /** 拍照获取图片*/
    public static final int TAKE_PHOTO = 2000;
    public static final int RESULT_OK = 1;

    /** 拍照获取图片--电表表脚封扣*/
    public static final int TAKE_PHOTO_METER_FOOT = 2001;
    /** 拍照获取图片--表箱封扣1*/
    public static final int TAKE_PHOTO_METER_BODY1 = 2002;
    /** 拍照获取图片--表箱封扣2*/
    public static final int TAKE_PHOTO_METER_BODY2 = 2003;


    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 集中器资产编号 -- 编辑框 -- cet_concentratorAssetNumbers */
    ClearEditText mCEtConcentratorAssetNumbers;
    /** 集中器资产编号(扫描输入) -- 按钮 -- btn_concentratorAssetNumbers */
    Button mBtnConcentratorAssetNumbers;


    /** 正在定位,请稍后... -- 文本 -- tv_wait */
    TextView mTvWait;
    /** 经度 -- 文本 -- tv_longitude */
    TextView mTvLongitude;
    /** 纬度 -- 文本 -- tv_latitude */
    TextView mTvLatitude;
    /** 地址 -- 文本 -- tv_addr */
    TextView mTvAddr;

    /** 获取定位 -- 按钮 -- btn_getLocation */
    Button mBtnGetLocation;

    /** 保存 -- 按钮 -- btn_save */
    Button mBtnSave;

    //--------------------------扫描-------------------------
    /** 成为 二维扫描 */
    Barcode2DWithSoft mBarcode2DWithSoft;
    ScanBack mScanBack;
    /** 当前二维扫描的按钮 */
    private int mCurrentScanBtnId = 1;

    //--------------------------百度地图定位---------------------------

    /** 百度API的"客户端类" */
    private LocationClient mLocationClient;
    public BDAbstractLocationListener myListener = new MyLocationListener();
    long mCount = 0;


    //-------------------------集中器的封扣---------------------
    /** 电表表脚封扣 -- 编辑框 -- cet_meterFootNumbersScan*/
    private ClearEditText mCEtMeterFootNumbersScan;
    /** 电表表脚封扣(扫描) -- 按钮 -- btn_meterFootNumbersScan*/
    private Button mBtnMeterFootNumbersScan;

    /** 电表表脚封扣 -- 布局 -- rlayout_meterFoot */
    private RelativeLayout mRLayoutMeterFoot;
    /** 电表表脚封扣(拍照后得到的照片) -- 图片 -- pv_meterFootPic*/
    private PhotoView mPvCameraMeterFoot;
    /** 电表表脚封扣(拍照后得到的照片的删除按钮) -- 按钮 -- iv_meterFootPicDelete*/
    private ImageView mIvMeterFootPicDelete;

    /** 电表表脚封扣(拍照) -- 图片按钮 -- ib_cameraMeterFoot*/
    private ImageButton mIBtnCameraMeterFoot;



    /** 表箱封扣1 -- 编辑框 -- cet_meterBodyNumbersScan1*/
    private ClearEditText mCEtMeterBodyNumbersScan1;
    /** 表箱封扣1(扫描) -- 按钮 -- btn_meterBodyNumbersScan1*/
    private Button mBtnMeterBodyNumbersScan1;

    /** 表箱封扣1 -- 布局 -- rlayout_meterBody1 */
    private RelativeLayout mRLayoutMeterBody1;
    /** 表箱封扣1(拍照后得到的照片) -- 图片 -- pv_meterBodyPic1*/
    private PhotoView mPvCameraMeterBody1;
    /** 表箱封扣1(拍照后得到的照片的删除按钮) -- 按钮 -- iv_meterBodyPicDelete1*/
    private ImageView mIvMeterBodyPicDelete1;

    /** 表箱封扣1(拍照) -- 图片按钮 -- ib_cameraMeterBody1*/
    private ImageButton mIBtnCameraMeterBody1;



    /** 表箱封扣2 -- 编辑框 -- cet_meterBodyNumbersScan2*/
    private ClearEditText mCEtMeterBodyNumbersScan2;
    /** 表箱封扣2(扫描) -- 按钮 -- btn_meterBodyNumbersScan2*/
    private Button mBtnMeterBodyNumbersScan2;

    /** 表箱封扣2 -- 布局 -- rlayout_meterBody2 */
    private RelativeLayout mRLayoutMeterBody2;
    /** 表箱封扣2(拍照后得到的照片) -- 图片 -- pv_meterBodyPic2*/
    private PhotoView mPvCameraMeterBody2;
    /** 表箱封扣2(拍照后得到的照片的删除按钮) -- 按钮 -- iv_meterBodyPicDelete2*/
    private ImageView mIvMeterBodyPicDelete2;

    /** 表箱封扣2(拍照) -- 图片按钮 -- ib_cameraMeterBody2*/
    private ImageButton mIBtnCameraMeterBody2;

    //--------------------------------------------------------

    /** 摄影 -- 按钮 */
    private Button mBtnCamera;
    /** 照片列表 -- 再生控件 */
    private RecyclerView mRvPic;

    private Bitmap mBitmap;

    View mLlayoutParent;
    View mIvBg;
    /** 放大后存放图片的控件*/
    PhotoView mPvBgImg;
    Info mInfo;

    AlphaAnimation in;
    AlphaAnimation out;

    String mCurrentPicName = "";


    /** 获取当前台区的--集中器 */
    List<ConcentratorBean> mConcentratorBeenList;

    ConcentratorBean mConcentratorBean = new ConcentratorBean();

    //---------------------------------图片-----------------------
    List<String> mPicPaths = new ArrayList<>();
    PicAdapter mPicAdapter;
    int mPhotoIndex = 0;

    @Override
    public int getContentLayout() {
        return R.layout.activity_get_location_concentrator;
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

        mTvTitle.setText("集中器位置信息");
    }

    @Override
    public void initView() {

        mCEtConcentratorAssetNumbers = (ClearEditText) findViewById(R.id.cet_concentratorAssetNumbers);
        mBtnConcentratorAssetNumbers = (Button) findViewById(R.id.btn_concentratorAssetNumbers);

        mTvWait = (TextView) findViewById(R.id.tv_wait);

        mTvLongitude = (TextView) findViewById(R.id.tv_longitude);
        mTvLatitude = (TextView) findViewById(R.id.tv_latitude);

        mTvAddr = (TextView) findViewById(R.id.tv_addr);

        mBtnGetLocation = (Button) findViewById(R.id.btn_getLocation);
        mBtnSave = (Button) findViewById(R.id.btn_save);


        mCEtMeterFootNumbersScan = (ClearEditText) findViewById(R.id.cet_meterFootNumbersScan);
        mBtnMeterFootNumbersScan = (Button) findViewById(R.id.btn_meterFootNumbersScan);
        mRLayoutMeterFoot = (RelativeLayout) findViewById(R.id.rlayout_meterFoot);
        mPvCameraMeterFoot = (PhotoView) findViewById(R.id.pv_meterFootPic);
        mIvMeterFootPicDelete = (ImageView) findViewById(R.id.iv_meterFootPicDelete);
        mIBtnCameraMeterFoot = (ImageButton) findViewById(R.id.ib_cameraMeterFoot);

        mCEtMeterBodyNumbersScan1 = (ClearEditText) findViewById(R.id.cet_meterBodyNumbersScan1);
        mBtnMeterBodyNumbersScan1 = (Button) findViewById(R.id.btn_meterBodyNumbersScan1);
        mRLayoutMeterBody1 = (RelativeLayout) findViewById(R.id.rlayout_meterBody1);
        mPvCameraMeterBody1 = (PhotoView) findViewById(R.id.pv_meterBodyPic1);
        mIvMeterBodyPicDelete1 = (ImageView) findViewById(R.id.iv_meterBodyPicDelete1);
        mIBtnCameraMeterBody1 = (ImageButton) findViewById(R.id.ib_cameraMeterBody1);

        mCEtMeterBodyNumbersScan2 = (ClearEditText) findViewById(R.id.cet_meterBodyNumbersScan2);
        mBtnMeterBodyNumbersScan2 = (Button) findViewById(R.id.btn_meterBodyNumbersScan2);
        mRLayoutMeterBody2 = (RelativeLayout) findViewById(R.id.rlayout_meterBody2);
        mPvCameraMeterBody2 = (PhotoView) findViewById(R.id.pv_meterBodyPic2);
        mIvMeterBodyPicDelete2 = (ImageView) findViewById(R.id.iv_meterBodyPicDelete2);
        mIBtnCameraMeterBody2 = (ImageButton) findViewById(R.id.ib_cameraMeterBody2);


        mBtnCamera = (Button) findViewById(R.id.btn_camera);

        mRvPic = (RecyclerView) findViewById(R.id.rv_pic);

        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.bg);
        mPvBgImg = (PhotoView) findViewById(R.id.img);
    }

    @Override
    public void initListener() {

        mBtnConcentratorAssetNumbers.setOnClickListener(this);
        mBtnGetLocation.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);


        mBtnMeterFootNumbersScan.setOnClickListener(this);
        mPvCameraMeterFoot.setOnClickListener(this);
        mIvMeterFootPicDelete.setOnClickListener(this);
        mIBtnCameraMeterFoot.setOnClickListener(this);

        mBtnMeterBodyNumbersScan1.setOnClickListener(this);
        mPvCameraMeterBody1.setOnClickListener(this);
        mIvMeterBodyPicDelete1.setOnClickListener(this);
        mIBtnCameraMeterBody1.setOnClickListener(this);

        mBtnMeterBodyNumbersScan2.setOnClickListener(this);
        mPvCameraMeterBody2.setOnClickListener(this);
        mIvMeterBodyPicDelete2.setOnClickListener(this);
        mIBtnCameraMeterBody2.setOnClickListener(this);

        mBtnCamera.setOnClickListener(this);
        mPvBgImg.setOnClickListener(this);
    }

    @Override
    public void initData() {

        mBtnConcentratorAssetNumbers.setEnabled(false);

        mBtnMeterFootNumbersScan.setEnabled(false);
        mBtnMeterBodyNumbersScan1.setEnabled(false);
        mBtnMeterBodyNumbersScan2.setEnabled(false);

        mScanBack = new ScanBack();
        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);
        // 获取当前台区的--集中器
        taskPresenter1.getConcentrator(concentratorObserver, MyApplication.getCurrentMeteringSection());
        initBaiduLocate();


        //------------------------------------------集中器 图片列表-----------------------------------------------------
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);         // 水平
        mRvPic.setLayoutManager(manager);

        mPicAdapter = new PicAdapter(this, mPicPaths, new PicAdapter.PicListener() {
            @Override
            public void onDelete(int index, final String path) {                      // 删除资源

                SweetAlertDialog dialog = new SweetAlertDialog(ConcentratorLocationActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mConcentratorBean.setPicPath(StringUtils.deleteSubStr(mConcentratorBean.getPicPath(), path));
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(path));

                                mPicAdapter.setPathList(mConcentratorBean.getPicPath());
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });


                dialog.show();

            }

            @Override
            public void onPreView(int index, String path, Info info){                      // 预览图片

                mInfo = info;                   // 拿到pv_camaraPhoto的信息(如：位置)，用于动画

                mBitmap = ImageFactory.getBitmap(path);
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                //ToastUtil.show("点击了预览图片");
                setTitleIsShow(View.GONE);

            }
        });

        mRvPic.setAdapter(mPicAdapter);

        //---------------------------------封扣---------------------------------------
        mCEtMeterFootNumbersScan.setText("");
        mRLayoutMeterFoot.setVisibility(View.GONE);
        mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

        mCEtMeterBodyNumbersScan1.setText("");
        mRLayoutMeterBody1.setVisibility(View.GONE);
        mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

        mCEtMeterBodyNumbersScan2.setText("");
        mRLayoutMeterBody2.setVisibility(View.GONE);
        mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);

        //------------------------- 拍照 ----------------------------------

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBarcode2DWithSoft != null)
            mBarcode2DWithSoft.stopScan();  // 停止
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBarcode2DWithSoft != null){
            mBarcode2DWithSoft.stopScan();
            mBarcode2DWithSoft.close();
        }


        if(mLocationClient != null && myListener != null){
            mLocationClient.unRegisterLocationListener(myListener);
            if(mLocationClient.isStarted())
                mLocationClient.stop();
        }
    }

    /**
     * 初始化 -- 百度API的"客户端"   <p>
     * 开始"客户端"
     */
    public void initBaiduLocate() {

        if (mLocationClient != null) {                           // 百度API的"客户端"  对象
            if(mLocationClient.isStarted())
                mLocationClient.stop();                              // 停止"客户端"
        }

        // 获取一个"默认方式"初始化 -- 百度定位客户端
        mLocationClient = LocationUtils.getDefaultInitedLocationClient(this);
        mLocationClient.registerLocationListener(myListener);          // 注册一个定位监听
        mLocationClient.start();                                 // 开始"客户端"
    }


    /**
     * 百度定位监听
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            mCount++;
            //mTvWait.setText("第" + mCount + "次:" );
            mTvWait.setText("");

            DecimalFormat f = new DecimalFormat("0.000000");
            //String templocationStr = f.format(location.getLongitude()) + "," +  f.format(location.getLatitude());

            mTvLongitude.setText(f.format(location.getLongitude()) + "");
            mTvLatitude.setText(f.format(location.getLatitude()) + "");

            mTvAddr.setText(location.getAddrStr());

//            //获取定位结果
//            location.getTime();             //获取定位时间
//            location.getLocationID();       //获取定位唯一ID，v7.2版本新增，用于排查定位问题
//            location.getLocType();          //获取定位类型
//            location.getLatitude();         //获取纬度信息
//            location.getLongitude();        //获取经度信息
//            location.getRadius();           //获取定位精准度
//            location.getAddrStr();          //获取地址信息
//            location.getCountry();          //获取国家信息
//            location.getCountryCode();      //获取国家码
//            location.getCity();             //获取城市信息
//            location.getCityCode();         //获取城市码
//            location.getDistrict();         //获取区县信息
//            location.getStreet();           //获取街道信息
//            location.getStreetNumber();     //获取街道码
//            location.getLocationDescribe(); //获取当前位置描述信息
//            location.getPoiList();          //获取当前位置周边POI信息
//
//            location.getBuildingID();       //室内精准定位下，获取楼宇ID
//            location.getBuildingName();     //室内精准定位下，获取楼宇名称
//            location.getFloor();            //室内精准定位下，获取当前位置所处的楼层信息

            if (location.getLocType() == BDLocation.TypeGpsLocation){
                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();                //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();      //获取当前卫星数
                location.getAltitude();             //获取海拔高度信息，单位米
                location.getDirection();            //获取方向信息，单位度

                LogUtils.i("前为GPS定位结果，可获取以下信息\n" +
                        " -- 获取当前速度，单位：公里每小时:" + location.getSpeed() +
                        " -- 获取当前卫星数:" + location.getSatelliteNumber() +
                        " -- 获取海拔高度信息，单位米:" + location.getAltitude() +
                        " -- 获取方向信息，单位度:" + location.getDirection()
                );

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                //当前为网络定位结果，可获取以下信息
                location.getOperators();            //获取运营商信息
                LogUtils.i("当前为网络定位结果，可获取以下信息 -- 获取运营商信息:" + location.getOperators());

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                //当前为网络定位结果
                LogUtils.i("当前为网络定位结果");

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
                LogUtils.i("当前网络定位失败");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //当前网络不通
                LogUtils.i("当前网络不通");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码

                LogUtils.i("当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限");
            }


        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {
                //建议打开GPS
                LogUtils.i("建议打开GPS");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {
                //建议打开wifi，不必连接，这样有助于提高网络定位精度！
                LogUtils.i("建议打开wifi，不必连接，这样有助于提高网络定位精度！");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {
                //定位权限受限，建议提示用户授予APP定位权限！
                LogUtils.i("定位权限受限，建议提示用户授予APP定位权限！");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {
                //网络异常造成定位失败，建议用户确认网络状态是否异常！
                LogUtils.i("网络异常造成定位失败，建议用户确认网络状态是否异常！");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {
                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！
                LogUtils.i("手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {
                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！
                LogUtils.i("无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {
                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！
                LogUtils.i("无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {
                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com
                LogUtils.i("百度定位服务端定位失败");

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {
                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！
                LogUtils.i("无法获取有效定位依据，但无法确定具体原因");
            }
        }
    }


    private void save(){
        String assetNumbers = mCEtConcentratorAssetNumbers.getText().toString().trim();
        String longitude = mTvLongitude.getText().toString().trim();
        String latitude = mTvLatitude.getText().toString().trim();
        String addr = mTvAddr.getText().toString().trim();

        //0.000000
        if(TextUtils.isEmpty(assetNumbers)){
            showToast("请输入资产编号");
            return;
        }
//        if(TextUtils.isEmpty(longitude) || longitude.equals("0.000000")){
//            showToast("无经纬度");
//            return;
//        }
//        if(TextUtils.isEmpty(latitude) || longitude.equals("0.000000")){
//            showToast("无经纬度");
//            return;
//        }

        //LogUtils.i("mConcentratorBean.toString():" + mConcentratorBean.toString());

        ContentValues values = new ContentValues();
        values.put(Constant.CONCENTRATOR.assetNumbers.toString(), assetNumbers);
        values.put(Constant.CONCENTRATOR.longitude.toString(), longitude);
        values.put(Constant.CONCENTRATOR.latitude.toString(), latitude);
        values.put(Constant.CONCENTRATOR.addr.toString(), addr);
        values.put(Constant.CONCENTRATOR.theMeteringSection.toString(), MyApplication.getCurrentMeteringSection());

        //----2017/09/04
        values.put(Constant.CONCENTRATOR.meterFootNumbers.toString(), mConcentratorBean.getMeterFootNumbers());
        values.put(Constant.CONCENTRATOR.meterFootPicPath.toString(), mConcentratorBean.getMeterFootPicPath());
        values.put(Constant.CONCENTRATOR.meterBodyNumbers1.toString(), mConcentratorBean.getMeterBodyNumbers1());
        values.put(Constant.CONCENTRATOR.meterBodyPicPath1.toString(), mConcentratorBean.getMeterBodyPicPath1());
        values.put(Constant.CONCENTRATOR.meterBodyNumbers2.toString(), mConcentratorBean.getMeterBodyNumbers2());
        values.put(Constant.CONCENTRATOR.meterBodyPicPath2.toString(), mConcentratorBean.getMeterBodyPicPath2());
        values.put(Constant.CONCENTRATOR.picPath.toString(), mConcentratorBean.getPicPath());

        taskPresenter1.addConcentrator(addConcentratorObserver, values, assetNumbers);

    }

    @Override
    public void onClick(View v) {
        String assetNumbers = mCEtConcentratorAssetNumbers.getText().toString().trim();

        switch (v.getId()) {


            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;

            case R.id.btn_concentratorAssetNumbers:                        // 扫描"集中器资产编号"
                mCEtConcentratorAssetNumbers.setText("");
                //mTvLongitude.setText("");
                //mTvLatitude.setText("");


                //---------------------------------封扣---------------------------------------
                mCEtMeterFootNumbersScan.setText("");
                mRLayoutMeterFoot.setVisibility(View.GONE);
                mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

                mCEtMeterBodyNumbersScan1.setText("");
                mRLayoutMeterBody1.setVisibility(View.GONE);
                mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

                mCEtMeterBodyNumbersScan2.setText("");
                mRLayoutMeterBody2.setVisibility(View.GONE);
                mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);

                mPicAdapter.clearPathList();
                //----------------------------------------------------
                mCurrentScanBtnId = R.id.btn_concentratorAssetNumbers;

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.btn_getLocation:

                break;

            case R.id.btn_save:
                save();
                break;


            case R.id.btn_meterFootNumbersScan:        // 电表表脚封扣(扫描) -- 按钮
                if(StringUtils.isEmpty(assetNumbers)){
                    showToast("请输入--资产编号");
                    return;
                }
                mCurrentScanBtnId = R.id.btn_meterFootNumbersScan;
                mCEtMeterFootNumbersScan.setText("");

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.pv_meterFootPic:                  // 电表表脚封扣(拍照后得到的照片) -- 图片
                mInfo = mPvCameraMeterFoot.getInfo();                   // 拿到pv_camaraPhoto的信息(如：位置)，用于动画
                mBitmap = ImageFactory.getBitmap(mConcentratorBean.getMeterFootPicPath());
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                setTitleIsShow(View.GONE);
                break;
            case R.id.iv_meterFootPicDelete:            // 电表表脚封扣(拍照后得到的照片的删除按钮) -- 按钮

                SweetAlertDialog dialog = new SweetAlertDialog(ConcentratorLocationActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String path = mConcentratorBean.getMeterFootPicPath();
                                mConcentratorBean.setMeterFootPicPath("");
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(path));

                                mRLayoutMeterFoot.setVisibility(View.GONE);
                                mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });


                dialog.show();

                break;
            case R.id.ib_cameraMeterFoot:               // 电表表脚封扣(拍照) -- 图片按钮
                if(StringUtils.isEmpty(assetNumbers)){
                    showToast("请输入--资产编号");
                    return;
                }

                mCurrentPicName = MyApplication.getNoWorkOrderPath().getConcentratorPhotoPath() +
                        assetNumbers + "_电表表脚封扣.jpg";

                if(mBarcode2DWithSoft!=null){
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.close();
                    mBarcode2DWithSoft = null;
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg");  // 携带图片存放路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PHOTO_METER_FOOT);

                break;


            case R.id.btn_meterBodyNumbersScan1:        // 表箱封扣1(扫描) -- 按钮
                if(StringUtils.isEmpty(assetNumbers)){
                    showToast("请输入--资产编号");
                    return;
                }

                mCurrentScanBtnId = R.id.btn_meterBodyNumbersScan1;
                mCEtMeterBodyNumbersScan1.setText("");

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;
            case R.id.pv_meterBodyPic1:                  // 表箱封扣1(拍照后得到的照片) -- 图片
                mInfo = mPvCameraMeterBody1.getInfo();                   // 拿到pv_camaraPhoto的信息(如：位置)，用于动画
                mBitmap = ImageFactory.getBitmap(mConcentratorBean.getMeterBodyPicPath1());
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                setTitleIsShow(View.GONE);
                break;

            case R.id.iv_meterBodyPicDelete1:            // 表箱封扣1(拍照后得到的照片的删除按钮) -- 按钮
                dialog = new SweetAlertDialog(ConcentratorLocationActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String path = mConcentratorBean.getMeterBodyPicPath1();
                                mConcentratorBean.setMeterBodyPicPath1("");
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(path));

                                mRLayoutMeterBody1.setVisibility(View.GONE);
                                mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });


                dialog.show();
                break;
            case R.id.ib_cameraMeterBody1:               // 表箱封扣1(拍照) -- 图片按钮
                if(StringUtils.isEmpty(assetNumbers)){
                    showToast("请输入--资产编号");
                    return;
                }
                mCurrentPicName = MyApplication.getNoWorkOrderPath().getConcentratorPhotoPath() +
                    assetNumbers + "_表箱封扣1.jpg";

                if(mBarcode2DWithSoft!=null){
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.close();
                    mBarcode2DWithSoft = null;
                }

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg");  // 携带图片存放路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PHOTO_METER_BODY1);
                break;

            case R.id.btn_meterBodyNumbersScan2:        // 表箱封扣2(扫描) -- 按钮
                if(StringUtils.isEmpty(assetNumbers)){
                    showToast("请输入--资产编号");
                    return;
                }
                mCurrentScanBtnId = R.id.btn_meterBodyNumbersScan2;
                mCEtMeterBodyNumbersScan2.setText("");

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.pv_meterBodyPic2:                  // 表箱封扣2(拍照后得到的照片) -- 图片
                mInfo = mPvCameraMeterBody2.getInfo();                   // 拿到pv_camaraPhoto的信息(如：位置)，用于动画
                mBitmap = ImageFactory.getBitmap(mConcentratorBean.getMeterBodyPicPath2());
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                setTitleIsShow(View.GONE);
                break;

            case R.id.iv_meterBodyPicDelete2:            // 表箱封扣2(拍照后得到的照片的删除按钮) -- 按钮
                dialog = new SweetAlertDialog(ConcentratorLocationActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String path = mConcentratorBean.getMeterBodyPicPath2();
                                mConcentratorBean.setMeterBodyPicPath2("");
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(path));

                                mRLayoutMeterBody2.setVisibility(View.GONE);
                                mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);

                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });


                dialog.show();
                break;
            case R.id.ib_cameraMeterBody2:               // 表箱封扣2(拍照) -- 图片按钮
                if(StringUtils.isEmpty(assetNumbers)){
                    showToast("请输入--资产编号");
                    return;
                }
                mCurrentPicName = MyApplication.getNoWorkOrderPath().getConcentratorPhotoPath() +
                        assetNumbers + "_表箱封扣2.jpg";

                if(mBarcode2DWithSoft!=null){
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.close();
                    mBarcode2DWithSoft = null;
                }

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg");  // 携带图片存放路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PHOTO_METER_BODY2);
                break;



            case R.id.btn_camera:

                if(!TextUtils.isEmpty(assetNumbers)) {

                    int size = 0;
                    if(StringUtils.isEmpty(mConcentratorBean.getPicPath())){
                        size = 0;
                    }else{
                        size = mConcentratorBean.getPicPath().split(",").length;
                    }
                    if(size > 3){
                        showToast("照片已超过4张");
                    }else {
                        mCurrentPicName = MyApplication.getNoWorkOrderPath().getConcentratorPhotoPath()
                                + assetNumbers + "_" + mPhotoIndex + ".jpg";

                        if(mBarcode2DWithSoft!=null){
                            mBarcode2DWithSoft.stopScan();
                            mBarcode2DWithSoft.close();
                            mBarcode2DWithSoft = null;
                        }

                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file = new File(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg");  // 携带图片存放路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                }else{
                    showToast("请输入--旧表资产编号");
                }


                break;

            case R.id.img:                                          // 点击"放大后的预览图片的控件"，缩小、隐藏那个预览布局
                mIvBg.startAnimation(out);
                setTitleIsShow(View.VISIBLE);
                mPvBgImg.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mLlayoutParent.setVisibility(View.GONE);
                    }
                });
                break;

            default:
                break;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBtnConcentratorAssetNumbers.setEnabled(false);

        mBtnMeterFootNumbersScan.setEnabled(false);
        mBtnMeterBodyNumbersScan1.setEnabled(false);
        mBtnMeterBodyNumbersScan2.setEnabled(false);

        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);

        if(requestCode == TAKE_PHOTO){                       // 拍照获取图片
            if (resultCode == Activity.RESULT_OK) {

                try {
                    ImageFactory.ratioAndGenThumb(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg",
                            mCurrentPicName, 1000, 1000, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LogUtils.i("shen1");

                if (mPicAdapter != null && mConcentratorBean != null) {
                    mPicAdapter.addPath(mCurrentPicName);
                    //LogUtils.i("前：" + mConcentratorBean.getPicPath());

                    if (StringUtils.isEmpty(mConcentratorBean.getPicPath())) {
                        mConcentratorBean.setPicPath(mCurrentPicName);
                    } else {
                        mConcentratorBean.setPicPath(mConcentratorBean.getPicPath() + "," + mCurrentPicName);
                    }
                    //LogUtils.i("后：" + mConcentratorBean.getPicPath());
                    ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(mCurrentPicName));
                    mPhotoIndex++;

                }
            }

        }else if(requestCode == TAKE_PHOTO_METER_FOOT){                       // 拍照获取图片--电表表脚封扣
            if (resultCode == Activity.RESULT_OK) {

                try {
                    ImageFactory.ratioAndGenThumb(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg",
                            mCurrentPicName, 1000, 1000, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mConcentratorBean != null) {

                    mRLayoutMeterFoot.setVisibility(View.VISIBLE);
                    mIBtnCameraMeterFoot.setVisibility(View.GONE);

                    mConcentratorBean.setMeterFootPicPath(mCurrentPicName);
                    mPvCameraMeterFoot.setImageBitmap(ImageFactory.getBitmap(mConcentratorBean.getMeterFootPicPath()));
                    ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(mCurrentPicName));
                }
            }

        }else if(requestCode == TAKE_PHOTO_METER_BODY1){                       // 拍照获取图片--表箱封扣1
            if (resultCode == Activity.RESULT_OK) {

                try {
                    ImageFactory.ratioAndGenThumb(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg",
                            mCurrentPicName, 1000, 1000, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mConcentratorBean != null) {

                    mRLayoutMeterBody1.setVisibility(View.VISIBLE);
                    mIBtnCameraMeterBody1.setVisibility(View.GONE);

                    mConcentratorBean.setMeterBodyPicPath1(mCurrentPicName);
                    mPvCameraMeterBody1.setImageBitmap(ImageFactory.getBitmap(mConcentratorBean.getMeterBodyPicPath1()));

                    ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(mCurrentPicName));
                }
            }

        }else if(requestCode == TAKE_PHOTO_METER_BODY2){                       // 拍照获取图片--表箱封扣2
            if (resultCode == Activity.RESULT_OK) {


                try {
                    ImageFactory.ratioAndGenThumb(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg",
                            mCurrentPicName, 1000, 1000, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mConcentratorBean != null) {

                    mRLayoutMeterBody2.setVisibility(View.VISIBLE);
                    mIBtnCameraMeterBody2.setVisibility(View.GONE);

                    mConcentratorBean.setMeterBodyPicPath2(mCurrentPicName);
                    mPvCameraMeterBody2.setImageBitmap(ImageFactory.getBitmap(mConcentratorBean.getMeterBodyPicPath2()));

                    ExcelUtil.broadCreateFile(ConcentratorLocationActivity.this, new File(mCurrentPicName));
                }
            }

        }


    }


    /**
     * 2D 数据返回
     */
    private final class ScanBack implements  Barcode2DWithSoft.ScanCallback{
        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {
            if (length < 1) {
                LogUtils.i("扫描失败");
                mBeepManager.playError();

            }else {
                LogUtils.i("扫描成功");
                String barCode = new String(bytes, 0, length);
                String scanData = barCode.trim();

                mBeepManager.playSuccessful();

                if (mCurrentScanBtnId == R.id.btn_concentratorAssetNumbers) {
                    mCEtConcentratorAssetNumbers.setText(scanData);

                    if(mConcentratorBeenList != null && mConcentratorBeenList.size()>0) {
                        for (ConcentratorBean bean : mConcentratorBeenList) {
                            //LogUtils.i("bean.toString()" + bean.toString());
                            //LogUtils.i("data" + scanData);

                            if (bean.getAssetNumbers().equals(scanData)) {
                                //LogUtils.i("bean.toString()" + bean.toString());
                                mConcentratorBean = bean;

                                //---------------------------------封扣---------------------------------------

                                mCEtMeterFootNumbersScan.setText(bean.getMeterFootNumbers());
                                mCEtMeterBodyNumbersScan1.setText(bean.getMeterBodyNumbers1());
                                mCEtMeterBodyNumbersScan2.setText(bean.getMeterBodyNumbers2());

                                if (StringUtils.isNotEmpty(mConcentratorBean.getMeterFootPicPath()) &&
                                        new File(mConcentratorBean.getMeterFootPicPath()).exists()) {
                                    mPvCameraMeterFoot.setImageBitmap(ImageFactory.getBitmap(mConcentratorBean.getMeterFootPicPath()));
                                    mRLayoutMeterFoot.setVisibility(View.VISIBLE);
                                    mIBtnCameraMeterFoot.setVisibility(View.GONE);
                                } else {
                                    mRLayoutMeterFoot.setVisibility(View.GONE);
                                    mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);
                                }

                                if (StringUtils.isNotEmpty(mConcentratorBean.getMeterBodyPicPath1()) &&
                                        new File(mConcentratorBean.getMeterBodyPicPath1()).exists()) {
                                    mPvCameraMeterBody1.setImageBitmap(ImageFactory.getBitmap(mConcentratorBean.getMeterBodyPicPath1()));
                                    mRLayoutMeterBody1.setVisibility(View.VISIBLE);
                                    mIBtnCameraMeterBody1.setVisibility(View.GONE);
                                } else {
                                    mRLayoutMeterBody1.setVisibility(View.GONE);
                                    mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);
                                }

                                if (StringUtils.isNotEmpty(mConcentratorBean.getMeterBodyPicPath2()) &&
                                        new File(mConcentratorBean.getMeterBodyPicPath2()).exists()) {
                                    mPvCameraMeterBody2.setImageBitmap(ImageFactory.getBitmap(mConcentratorBean.getMeterBodyPicPath2()));
                                    mRLayoutMeterBody2.setVisibility(View.VISIBLE);
                                    mIBtnCameraMeterBody2.setVisibility(View.GONE);
                                } else {
                                    mRLayoutMeterBody2.setVisibility(View.GONE);
                                    mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);
                                }

                            if(StringUtils.isEmpty(mConcentratorBean.getPicPath())){
                                mPhotoIndex = 1;
                            }else {
                                String path = mConcentratorBean.getPicPath();
                                for(String tempPath : path.split(",")){
                                    if(!(new File(tempPath).exists())){
                                        path = StringUtils.deleteSubStr(path, tempPath);
                                    }
                                }

                                mConcentratorBean.setPicPath(path);
                                if(StringUtils.isNotEmpty(mConcentratorBean.getPicPath())) {
                                    try {
                                        mPhotoIndex = Integer.parseInt(path.substring(path.lastIndexOf("_") + 1, path.lastIndexOf("."))) + 1;
                                    } catch (Exception e) {
                                    }
                                }
                                mPicAdapter.setPathList(mConcentratorBean.getPicPath());
                            }

                                break;
                            } else {
                                mPhotoIndex = 1;
                                mConcentratorBean = new ConcentratorBean();

                                //---------------------------------封扣---------------------------------------
                                mCEtMeterFootNumbersScan.setText("");
                                mRLayoutMeterFoot.setVisibility(View.GONE);
                                mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

                                mCEtMeterBodyNumbersScan1.setText("");
                                mRLayoutMeterBody1.setVisibility(View.GONE);
                                mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

                                mCEtMeterBodyNumbersScan2.setText("");
                                mRLayoutMeterBody2.setVisibility(View.GONE);
                                mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{

                        mPhotoIndex = 1;
                        mConcentratorBean = new ConcentratorBean();

                        //---------------------------------封扣---------------------------------------
                        mCEtMeterFootNumbersScan.setText("");
                        mRLayoutMeterFoot.setVisibility(View.GONE);
                        mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

                        mCEtMeterBodyNumbersScan1.setText("");
                        mRLayoutMeterBody1.setVisibility(View.GONE);
                        mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

                        mCEtMeterBodyNumbersScan2.setText("");
                        mRLayoutMeterBody2.setVisibility(View.GONE);
                        mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);
                    }

                    mConcentratorBean.setAssetNumbers(scanData);

                }else if(mCurrentScanBtnId == R.id.btn_meterFootNumbersScan){
                    mBeepManager.playSuccessful();
                    mCEtMeterFootNumbersScan.setText(scanData);
                    mConcentratorBean.setMeterFootNumbers(scanData);
                }else if(mCurrentScanBtnId == R.id.btn_meterBodyNumbersScan1){
                    mBeepManager.playSuccessful();
                    mCEtMeterBodyNumbersScan1.setText(scanData);
                    mConcentratorBean.setMeterBodyNumbers1(scanData);
                }else if(mCurrentScanBtnId == R.id.btn_meterBodyNumbersScan2){
                    mBeepManager.playSuccessful();
                    mCEtMeterBodyNumbersScan2.setText(scanData);
                    mConcentratorBean.setMeterBodyNumbers2(scanData);
                }

            }
        }
    }


    /**
     * 初始二维扫描
     *
     * rxjava -- 主线程
     */
    Observer initBarcode2DSObserver = new Observer<Barcode2DWithSoft>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Barcode2DWithSoft barcode2DWithSoft) {
            if(barcode2DWithSoft == null) {
                LogUtils.i("二维初始化失败");
            }else {
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
            mBtnConcentratorAssetNumbers.setEnabled(true);

            mBtnMeterFootNumbersScan.setEnabled(true);
            mBtnMeterBodyNumbersScan1.setEnabled(true);
            mBtnMeterBodyNumbersScan2.setEnabled(true);

        }
    };


    /**
     * 添加--集中器
     *
     * rxjava -- 主线程
     */
    Observer addConcentratorObserver = new Observer<Long>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Long aLong) {
            //LogUtils.i(aLong>0? "添加成功" : "添加失败");
            showToast(aLong >0 ? "保存成功" : "保存失败");
            if(aLong > 0){

                mCEtConcentratorAssetNumbers.setText("");

                //---------------------------------封扣---------------------------------------
                mCEtMeterFootNumbersScan.setText("");
                mRLayoutMeterFoot.setVisibility(View.GONE);
                mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

                mCEtMeterBodyNumbersScan1.setText("");
                mRLayoutMeterBody1.setVisibility(View.GONE);
                mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

                mCEtMeterBodyNumbersScan2.setText("");
                mRLayoutMeterBody2.setVisibility(View.GONE);
                mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);

                mPicAdapter.clearPathList();
            }
        }


        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("addConcentratorObserver -- e.getMessage()" + e.getMessage());
        }

        @Override
        public void onComplete(){



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
