package com.zh.metermanagementcw.activity;

import android.content.ContentValues;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.LocationUtils;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.view.ClearEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 *
 * 无工单 -- 集中器(百度获取定位)
 *
 */
public class ConcentratorLocationActivity extends BaseActivity implements View.OnClickListener {

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
    }

    @Override
    public void initListener() {

        mBtnConcentratorAssetNumbers.setOnClickListener(this);
        mBtnGetLocation.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
    }

    @Override
    public void initData() {

        mScanBack = new ScanBack();
        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);
        initBaiduLocate();
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
        if(TextUtils.isEmpty(longitude) || longitude.equals("0.000000")){
            showToast("无经纬度");
            return;
        }
        if(TextUtils.isEmpty(latitude) || longitude.equals("0.000000")){
            showToast("无经纬度");
            return;
        }


        ContentValues values = new ContentValues();
        values.put(Constant.CONCENTRATOR.assetNumbers.toString(), assetNumbers);
        values.put(Constant.CONCENTRATOR.longitude.toString(), longitude);
        values.put(Constant.CONCENTRATOR.latitude.toString(), latitude);
        values.put(Constant.CONCENTRATOR.addr.toString(), addr);
        values.put(Constant.CONCENTRATOR.theMeteringSection.toString(), MyApplication.getCurrentMeteringSection());

        taskPresenter1.addConcentrator(addConcentratorObserver, values, assetNumbers);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;

            case R.id.btn_concentratorAssetNumbers:
                mCEtConcentratorAssetNumbers.setText("");
                //mTvLongitude.setText("");
                //mTvLatitude.setText("");

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

            default:
                break;
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
        }


        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("addConcentratorObserver -- e.getMessage()" + e.getMessage());
        }

        @Override
        public void onComplete(){

        }
    };
}
