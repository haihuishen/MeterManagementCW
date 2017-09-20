package com.zh.metermanagementcw.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.serialport.MeterController;
import android.serialport.Tools;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rscja.deviceapi.Infrared;
import com.rscja.utility.StringUtility;
import com.shen.sweetdialog.SweetAlertDialog;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.config.MeterAgreement;
import com.zh.metermanagementcw.utils.ElectricMeterParsUtils;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.ResourceUtil;
import com.zh.metermanagementcw.utils.StringUtils;
import com.zh.metermanagementcw.utils.TimeUtils;
import com.zh.metermanagementcw.utils.ToFormatUtil;
import com.zh.metermanagementcw.view.ChangeDialog;
import com.zh.metermanagementcw.view.ClearEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 数据验收界面
 */
public class AcceptanceActivity extends BaseActivity implements OnClickListener {

    private final int BROAD_READMETER_FAIL = 100;
    private final int BROAD_READMETER_SUCCESS = 200;

    private final String READMETER_AGREEMENT_TASK = "ReadMeterAgreementTask";
    private final String READMETER_AGREEMENT_TIME_TASK = "ReadMeterAgreementTimeTask";
    private final String TIME_TYPE_DAY = "TimeTypeDay";
    private final String TIME_TYPE_MONTH = "TimeTypeMonth";

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;



    /** 资产编号 -- 编辑框 --  cet_assetNumbers*/
    ClearEditText mCEtAssetNumbers;
    /** 资产编号2D扫描 -- 按钮 --  btn_assetNumbers*/
    Button mBtnAssetNumbers;
    /** 用户编号 -- 文本 --  tv_userNumber*/
    TextView mTvUserNumber;
    /** 用户名称 -- 文本 --  tv_userName*/
    TextView mTvUserName;
    /** 电表地址 -- 文本 --  tv_meterAddr*/
    TextView mTvMeterAddr;


    /** 冻结时间(日) -- 文本 --  tv_daysFreezingTimeIn*/
    TextView mTvDaysFreezingTimeIn;
    /** 日冻结读数(日) -- 文本 --  tv_electricityInByDay*/
    TextView mTvElectricityInByDay;
    /** 复核日冻结时间(日) -- 文本 --  tv_daysFreezingTimeScan*/
    TextView mTvDaysFreezingTimeScan;
    /** 复核日冻结读数(日) -- 编辑框 --  cet_electricityScanByDay*/
    ClearEditText mCEtElectricityScanByDay;
    /** 复核日冻结读数红外读取(日) -- 按钮 --  btn_electricityScanByDay*/
    Button mBtnElectricityScanByDay;
    /** 差异(日) -- 文本 --  tv_differencesByDay*/
    TextView mTvDifferencesByDay;

    /** 冻结时间(月) -- 文本 --  tv_monthFreezingTimeIn*/
    TextView mTvMonthFreezingTimeIn;
    /** 日冻结读数(月) -- 文本 --  tv_electricityInByMonth*/
    TextView mTvElectricityInByMonth;
    /** 复核日冻结时间(月) -- 文本 --  tv_monthFreezingTimeScan*/
    TextView mTvMonthFreezingTimeScan;
    /** 复核日冻结读数(月) -- 编辑框 --  cet_electricityScanByMonth*/
    ClearEditText mCEtElectricityScanByMonth;
    /** 复核日冻结读数红外读取(月) -- 按钮 --  btn_electricityScanByMonth*/
    Button mBtnElectricityScanByMonth;
    /** 差异(月) -- 文本 --  tv_differencesByMonth*/
    TextView mTvDifferencesByMonth;

    /** 保存 -- 按钮 --  btn_save*/
    Button mBtnSave;

    /** 保存是否成功 --*/
    boolean mBooleanSave = false;

    //---------------------------红外-----------------------
    private Infrared mInstance;
    private boolean isOpened = false;
    /** 当前红外扫描的按钮 */
    private int mCurrentReadBtnId = 1;

    /** 日冻结,还是月冻结 */
    private String mTimeType = "";
    private String mTasking = "";
    private String mReadMeter97Or07 = "";

    ReadMeterAgreementTask mReadMeterAgreementTask;
    ReadMeterAgreementTimeTask mReadMeterAgreementTimeTask;
    //--------------------------扫描-------------------------
    /** 成为 二维扫描 */
    Barcode2DWithSoft mBarcode2DWithSoft;
    ScanBack mScanBack;
    /** 当前二维扫描的按钮 */
    private int mCurrentScanBtnId = 1;

    //--------------------------------------------------------

    ReceiveThread mReceiveThread;

    List<AcceptanceBean> mAcceptanceBeanList = new ArrayList<>();
    AcceptanceBean mAcceptanceBean = new AcceptanceBean();

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BROAD_READMETER_FAIL: 												// 抄表失败
                    mBeepManager.playError();
                    //promptTone();
                    showToast("抄表失败");
                    String fallMessage = "";

                    if(mCurrentReadBtnId == R.id.btn_electricityScanByDay){

                    }else if(mCurrentReadBtnId == R.id.btn_electricityScanByMonth){

                    }
                    break;

                case BROAD_READMETER_SUCCESS: 								 // 抄表成功
                    String[] datas = (String[]) msg.obj;					 // 刚刚抄表得到的数据

                    LogUtils.i("广播抄表结果 -- \n"
                            + "表地址:" + datas[0] + "\n"
                            + "数据标识:" + datas[1] + " : " + datas[2]);

                    if(mTasking.equals(READMETER_AGREEMENT_TASK)) {

                        if(datas[1].length() == 4)
                            mReadMeter97Or07 = "97";
                        else if(datas[1].length() == 8)
                            mReadMeter97Or07 = "07";

                        if(!mReadMeter97Or07.equals(""))
                            mBeepManager.playSuccessful();

                        if(mReadMeterAgreementTask!=null
                                && !mReadMeterAgreementTask.isCancelled()
                                && mReadMeterAgreementTask.getStatus() == AsyncTask.Status.RUNNING){

                            mReadMeterAgreementTask.setStop();
                        }

//                        String s = "";
//                        if(datas[2].length() > 11)
//                            s = datas[2].substring(0, 11);
//                        else
//                            s = datas[2];
                        String s = ToFormatUtil.stringToDecimalFormat(datas[2], 4);

                        if(mCurrentReadBtnId == R.id.btn_electricityScanByDay){
                            mCEtElectricityScanByDay.setText(s);
                            float dayIn = Float.valueOf(mTvElectricityInByDay.getText().toString().trim());
                            float dayScan = Float.valueOf(s);

                            float i = dayScan - dayIn;

                            String diff = "";
                            int color = 0;
                            if(i == 0){
                                diff = "正常";
                                color = ResourceUtil.getColorResIDByName(getContext(), "shen_black");
                            }else if(i>0){
                                diff = "异常(大)";
                                color = ResourceUtil.getColorResIDByName(getContext(), "shen_red");

                            }else if(i<0){
                                diff = "异常(小)";
                                color = ResourceUtil.getColorResIDByName(getContext(), "shen_red");
                            }
                            mTvDifferencesByDay.setText(diff);
                            //mTvDifferencesByDay.setTextColor(color);

                            mAcceptanceBean.setElectricityScanByDay(s);
                            mAcceptanceBean.setDifferencesByDay(ToFormatUtil.toDecimalFormat(i, 4));
                            mAcceptanceBean.setConclusionByDay(diff);
                            mAcceptanceBean.setFinishByDay(true);


                        }else if(mCurrentReadBtnId == R.id.btn_electricityScanByMonth){
                            mCEtElectricityScanByMonth.setText(s);
                            float dayIn = Float.valueOf(mTvElectricityInByMonth.getText().toString().trim());
                            float dayScan = Float.valueOf(s);

                            float i = dayScan - dayIn;
                            String diff = "";
                            int color = 0;
                            if(i == 0){
                                diff = "正常";
                                color = ResourceUtil.getColorResIDByName(getContext(), "shen_black");
                            }else if(i>0){
                                diff = "异常(大)";
                                color = ResourceUtil.getColorResIDByName(getContext(), "shen_red");

                            }else if(i<0){
                                diff = "异常(小)";
                                color = ResourceUtil.getColorResIDByName(getContext(), "shen_red");
                            }
                            mTvDifferencesByMonth.setText(diff);
                            //mTvDifferencesByMonth.setTextColor(color);

                            mAcceptanceBean.setElectricityScanByMonth(s);
                            mAcceptanceBean.setDifferencesByMonth(ToFormatUtil.toDecimalFormat(i, 4));
                            mAcceptanceBean.setConclusionByMonth(diff);
                            mAcceptanceBean.setFinishByMonth(true);
                        }

                    }else if(mTasking.equals(READMETER_AGREEMENT_TIME_TASK)){

                        if(datas[1].length() == 4)
                            mReadMeter97Or07 = "97";
                        else if(datas[1].length() == 8)
                            mReadMeter97Or07 = "07";

//                        if(!mReadMeter97Or07.equals(""))
//                            mBeepManager.playSuccessful();

                        LogUtils.i("mTasking.equals(READMETER_AGREEMENT_TIME_TASK -- " + mReadMeter97Or07 );

                        if(mReadMeterAgreementTimeTask!=null
                                && !mReadMeterAgreementTimeTask.isCancelled()
                                && mReadMeterAgreementTimeTask.getStatus() == AsyncTask.Status.RUNNING){

                            mReadMeterAgreementTimeTask.setStop();
                        }

                        if(mCurrentReadBtnId == R.id.btn_electricityScanByDay){
                            mTvDaysFreezingTimeScan.setText(datas[2]);
                            //mCEtElectricityScanByDay.setText(datas[2]);

                            mAcceptanceBean.setDaysFreezingTimeScan(datas[2]);

                        }else if(mCurrentReadBtnId == R.id.btn_electricityScanByMonth){
                            //mCEtElectricityScanByMonth.setText(datas[2]);
                            mTvMonthFreezingTimeScan.setText(datas[2]);
                            mAcceptanceBean.setMonthFreezingTimeScan(datas[2]);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public int getContentLayout() {
        return R.layout.activity_acceptance;
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

        mTvTitle.setText("数据验收");
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(){
        mCEtAssetNumbers = (ClearEditText) findViewById(R.id.cet_assetNumbers);
        mBtnAssetNumbers = (Button) findViewById(R.id.btn_assetNumbers);
        mTvUserNumber = (TextView) findViewById(R.id.tv_userNumber);
        mTvUserName = (TextView) findViewById(R.id.tv_userName);
        mTvMeterAddr = (TextView) findViewById(R.id.tv_meterAddr);

        mTvDaysFreezingTimeIn = (TextView) findViewById(R.id.tv_daysFreezingTimeIn);
        mTvElectricityInByDay = (TextView) findViewById(R.id.tv_electricityInByDay);
        mTvDaysFreezingTimeScan = (TextView) findViewById(R.id.tv_daysFreezingTimeScan);
        mCEtElectricityScanByDay = (ClearEditText) findViewById(R.id.cet_electricityScanByDay);
        mBtnElectricityScanByDay = (Button) findViewById(R.id.btn_electricityScanByDay);
        mTvDifferencesByDay = (TextView) findViewById(R.id.tv_differencesByDay);

        mTvMonthFreezingTimeIn = (TextView) findViewById(R.id.tv_monthFreezingTimeIn);
        mTvElectricityInByMonth = (TextView) findViewById(R.id.tv_electricityInByMonth);
        mTvMonthFreezingTimeScan = (TextView) findViewById(R.id.tv_monthFreezingTimeScan);
        mCEtElectricityScanByMonth = (ClearEditText) findViewById(R.id.cet_electricityScanByMonth);
        mBtnElectricityScanByMonth = (Button) findViewById(R.id.btn_electricityScanByMonth);
        mTvDifferencesByMonth = (TextView) findViewById(R.id.tv_differencesByMonth);

        mBtnSave = (Button) findViewById(R.id.btn_save);

    }

    @Override
    public void initListener() {

        mBtnAssetNumbers.setOnClickListener(this);
        mBtnElectricityScanByDay.setOnClickListener(this);
        mBtnElectricityScanByMonth.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mCEtAssetNumbers.setEnabled(false);
        mCEtElectricityScanByDay.setEnabled(false);
        mCEtElectricityScanByMonth.setEnabled(false);


        mBtnAssetNumbers.setEnabled(false);
        mBtnElectricityScanByDay.setEnabled(false);
        mBtnElectricityScanByMonth.setEnabled(false);


        mScanBack = new ScanBack();
        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);
        taskPresenter1.initInstance(initInstanceObserver, 1200);
        taskPresenter1.readDbToBeanForAcceptance(readObserver);
    }



    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("ReplaceMeterActivity -- onResume");



    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("ReplaceMeterActivity -- onPause");
        if (mBarcode2DWithSoft != null)
            mBarcode2DWithSoft.stopScan();  // 停止


    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i("ReplaceMeterActivity -- onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("ReplaceMeterActivity -- onDestroy");
        if(mBarcode2DWithSoft != null){
            mBarcode2DWithSoft.stopScan();
            mBarcode2DWithSoft.close();
        }


        if (isOpened) {
            if(mInstance.close()){
                if (mReceiveThread != null) {
                    mReceiveThread.stopThread();
                    mReceiveThread = null;
                }
            }
        }

    }



    private void save() {

        mBooleanSave = false;

        if(mAcceptanceBean != null &&
                (mAcceptanceBean.isFinishByDay() || mAcceptanceBean.isFinishByMonth()))
            taskPresenter1.saveAcceptance(saveAcceptanceObserver, mAcceptanceBean);
        else {
            showToast("请输入数据");
        }
    }


    /**
     * 开始抄表
     */
    private void startReadMeter(String addr) {

        if (TextUtils.isEmpty(addr)) {
            showToast("表地址不能为空");
            return;
        }


        //mReadMeterAgreementTask = new ReadMeterAgreementTask(addr, meterAgreement);
        //mReadMeterAgreementTask.execute();

        mReadMeterAgreementTimeTask = new ReadMeterAgreementTimeTask(addr);
        mReadMeterAgreementTimeTask.execute();
    }

    /**
     * 异步  -- 获取地址&电表时间
     */
    public class ReadMeterAgreementTimeTask extends AsyncTask<String, String, String> {

        boolean stop = false;

        String mAddr = "AAAAAAAAAAAA";
        String mMeterAgreement = "";

        public ReadMeterAgreementTimeTask(String addr) {
            mAddr = addr;
        }


        public void setStop() {
            stop = true;
        }

        @Override
        protected void onPreExecute() {                                                 // 执行前
            super.onPreExecute();

            stop = false;
            mTasking = READMETER_AGREEMENT_TIME_TASK;
            mReadMeter97Or07 = "";
            showLoadingDialog("正在红外读取", null);

        }


        @Override
        protected String doInBackground(String... strings) {            // 执行中

            byte[] command;

            //---------------------------------07--------------------------------
            if (TextUtils.isEmpty(mAddr))
                mAddr = "000000000000";
            //if (TextUtils.isEmpty(mMeterAgreement)) {
            mMeterAgreement = MeterAgreement.Pro07.STR_04000101;
            //}
            command = ElectricMeterParsUtils.getBuffer(
                    ElectricMeterParsUtils.getMeterAddress(mAddr, ElectricMeterParsUtils.METERTYPE._07),
                    mMeterAgreement);    // 生成命令！
            mInstance.send(command);
            Timer timer07 = new Timer();
            timer07.schedule(new TimerTask() {
                @Override
                public void run() {
                    stop = true;
                }
            }, 1500);

            while (!stop) {
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            timer07.cancel();

            stop = false;

            //---------------------------------97--------------------------------
            if (mReadMeter97Or07.equals("")) {

                if (TextUtils.isEmpty(mAddr))
                    mAddr = "AAAAAAAAAAAA";
                //if (TextUtils.isEmpty(mMeterAgreement)) {
                mMeterAgreement = MeterAgreement.Pro97.STR_C010;
                //}
                command = ElectricMeterParsUtils.getBuffer(
                        ElectricMeterParsUtils.getMeterAddress(mAddr, ElectricMeterParsUtils.METERTYPE._97),
                        mMeterAgreement);                      // 生成命令！

                mInstance.send(command);

                Timer timer97 = new Timer();
                timer97.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stop = true;
                    }
                }, 1500);

                while (!stop) {
                    try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timer97.cancel();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {           // 执行后
            super.onPostExecute(str);

            if (TextUtils.isEmpty(mReadMeter97Or07)) {                             // 没有收到数据

                LogUtils.i("TextUtils.isEmpty(mReadMeter97Or07) -- " + mReadMeter97Or07 );

                Message message = Message.obtain();
                message.what = BROAD_READMETER_FAIL;
                mHandler.sendMessage(message);
                closeDialog();
            }else{
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(mTimeType.equals(TIME_TYPE_DAY)) {
                            String timeIn = mTvDaysFreezingTimeIn.getText().toString().trim();
                            String timeScan = mTvDaysFreezingTimeScan.getText().toString().trim();
                            mReadMeterAgreementTask = new ReadMeterAgreementTask(mAddr, timeIn, timeScan);
                            mReadMeterAgreementTask.execute();

                        }else if(mTimeType.equals(TIME_TYPE_MONTH)){
                            String timeIn = mTvMonthFreezingTimeIn.getText().toString().trim();
                            String timeScan = mTvMonthFreezingTimeScan.getText().toString().trim();
                            mReadMeterAgreementTask = new ReadMeterAgreementTask(mAddr, timeIn, timeScan);
                            mReadMeterAgreementTask.execute();
                        }
                    }
                }, 500);

            }
            //closeDialog();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            // updataLoadingDialog("正在确定电表协议,请稍等", values[0]);
        }
    }

    /**
     * 异步 -- 获取地址&正向有功电度(日冻结)
     */
    public class ReadMeterAgreementTask extends AsyncTask<String, String, String> {

        boolean stop = false;

        String mAddr = "AAAAAAAAAAAA";
        String mTimeIn = "2017-08-28";
        String mTimeScan = "2017-08-28";
        String mMeterAgreement = "";

        public ReadMeterAgreementTask(String addr, String timeIn, String timeScan){
            mAddr = addr;
            mTimeIn = timeIn;
            mTimeScan = timeScan;
        }


        public void setStop(){
            stop = true;
        }

        @Override
        protected void onPreExecute() {                                                 // 执行前
            super.onPreExecute();

            stop = false;
            mTasking = READMETER_AGREEMENT_TASK;
            mReadMeter97Or07 = "";
            //showLoadingDialog("正在红外读取", null);

        }


        @Override
        protected String doInBackground(String... strings) {            // 执行中

            byte[] command;

            if(mTimeType.equals(TIME_TYPE_DAY)) {
                mMeterAgreement = ElectricMeterParsUtils.getMeterAgreementByDay(mTimeIn, mTimeScan,
                        ElectricMeterParsUtils.METERTYPE._07);
            }else if(mTimeType.equals(TIME_TYPE_MONTH)) {
                mMeterAgreement = ElectricMeterParsUtils.getMeterAgreementByMonth(mTimeIn, mTimeScan,
                        ElectricMeterParsUtils.METERTYPE._07);
            }

            //---------------------------------07--------------------------------
            if(TextUtils.isEmpty(mAddr))
                mAddr = "000000000000";
            if(TextUtils.isEmpty(mMeterAgreement)){
                //mMeterAgreement = MeterAgreement.Pro07.STR_00000001;
                return "";
            }
            command = ElectricMeterParsUtils.getBuffer(
                    ElectricMeterParsUtils.getMeterAddress(mAddr, ElectricMeterParsUtils.METERTYPE._07),
                    mMeterAgreement);    // 生成命令！
            mInstance.send(command);
            Timer timer07 = new Timer();
            timer07.schedule(new TimerTask() {
                @Override
                public void run() {
                    stop = true;
                };
            }, 1500);

            while (!stop) {
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            timer07.cancel();

            stop = false;


            //---------------------------------97--------------------------------
            if(mReadMeter97Or07.equals("")) {


                if(mTimeType.equals(TIME_TYPE_DAY)) {
                    mMeterAgreement = ElectricMeterParsUtils.getMeterAgreementByDay(mTimeIn, mTimeScan,
                            ElectricMeterParsUtils.METERTYPE._97);
                }else if(mTimeType.equals(TIME_TYPE_MONTH)) {
                    mMeterAgreement = ElectricMeterParsUtils.getMeterAgreementByMonth(mTimeIn, mTimeScan,
                            ElectricMeterParsUtils.METERTYPE._07);
                }
                if(TextUtils.isEmpty(mAddr))
                    mAddr = "AAAAAAAAAAAA";
                if(TextUtils.isEmpty(mMeterAgreement)){
                    //mMeterAgreement = MeterAgreement.Pro97.STR_9410;
                    return "";
                }
                command = ElectricMeterParsUtils.getBuffer(
                        ElectricMeterParsUtils.getMeterAddress(mAddr, ElectricMeterParsUtils.METERTYPE._97),
                        mMeterAgreement);                      // 生成命令！

                mInstance.send(command);

                Timer timer97 = new Timer();
                timer97.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stop = true;
                    };
                }, 1500);

                while (!stop) {
                    try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timer97.cancel();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {           // 执行后
            super.onPostExecute(str);

            if(TextUtils.isEmpty(mReadMeter97Or07)) {                        	 // 没有收到数据
                Message message = Message.obtain();
                message.what = BROAD_READMETER_FAIL;
                mHandler.sendMessage(message);
            }
            closeDialog();

            if(mMeterAgreement == null){
                showToast("请查看当前时间");
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //updataLoadingDialog("正在确定电表协议,请稍等", values[0]);
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

                boolean isHave = false;
                if (mCurrentScanBtnId == R.id.btn_assetNumbers) {
                    for(AcceptanceBean bean : mAcceptanceBeanList){

                        if(bean.getAssetNumbers().equalsIgnoreCase(scanData)){
                            mBeepManager.playSuccessful();
                            mAcceptanceBean = bean;
                            LogUtils.i(bean.toString());
                            isHave = true;

                            mCEtAssetNumbers.setText(scanData);
                            mTvUserNumber.setText(bean.getUserNumber());
                            mTvUserName.setText(bean.getUserName());
                            mTvMeterAddr.setText(bean.getMeterAddr());

                            mTvDaysFreezingTimeIn.setText(bean.getDaysFreezingTimeIn());
                            mTvElectricityInByDay.setText(bean.getElectricityInByDay());
                            mTvDaysFreezingTimeScan.setText(bean.getDaysFreezingTimeScan());
                            mCEtElectricityScanByDay.setText(bean.getElectricityScanByDay());
                            mTvDifferencesByDay.setText(bean.getConclusionByDay());

                            mTvMonthFreezingTimeIn.setText(bean.getMonthFreezingTimeIn());
                            mTvElectricityInByMonth.setText(bean.getElectricityInByMonth());
                            mTvMonthFreezingTimeScan.setText(bean.getMonthFreezingTimeScan());
                            mCEtElectricityScanByMonth.setText(bean.getElectricityScanByMonth());
                            mTvDifferencesByMonth.setText(bean.getConclusionByMonth());

                            if(TextUtils.isEmpty(bean.getDaysFreezingTimeIn()) ||
                                    TextUtils.isEmpty(bean.getElectricityInByDay()))
                                mBtnElectricityScanByDay.setEnabled(false);
                            else
                                mBtnElectricityScanByDay.setEnabled(true);


                            if(TextUtils.isEmpty(bean.getMonthFreezingTimeIn()) ||
                                    TextUtils.isEmpty(bean.getElectricityInByMonth()))
                                mBtnElectricityScanByMonth.setEnabled(false);
                            else
                                mBtnElectricityScanByMonth.setEnabled(true);


                            break;
                        }
                    }

                    if(!isHave){
                        mBeepManager.playError();
                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("提示")
                                .setContentText(scanData + "\n该电表资产编码无匹配的用户，\n请通知供电所相关人员")
                                .setConfirmText("确认")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });

                        dialog.show();
                    }
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
            mBtnAssetNumbers.setEnabled(true);

        }
    };



    /**
     * 初始化红外扫描
     *
     * rxjava -- 主线程
     */
    Observer initInstanceObserver = new Observer<Infrared>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Infrared infrared) {
            if(infrared == null) {
                LogUtils.i("红外打开失败");
            }else {
                isOpened = true;
                LogUtils.i("红外打开成功" );
                mInstance = infrared;
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("initInstanceObserver -- e.getMessage()" + e.getMessage());
        }

        @Override
        public void onComplete(){

            mBtnElectricityScanByDay.setEnabled(true);
            mBtnElectricityScanByMonth.setEnabled(true);

            if(mReceiveThread == null || !mReceiveThread.isAlive()) {
                mReceiveThread = new ReceiveThread();
                mReceiveThread.start();
            }
        }
    };


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

            if(mBooleanSave)
                showToast("保存成功！");

            closeDialog();
        }
    };


    /**
     * 保存数据
     *
     * rxjava -- 主线程
     */
    Observer saveAcceptanceObserver = new Observer<Long>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Long aLong) {
            showToast((aLong > 0 ? "保存成功" : "保存失败" ));

            if(aLong > 0 ) {
                mBooleanSave = true;
                taskPresenter1.readDbToBeanForAcceptance(readObserver);
            }else {
                showToast("保存失败");
            }

            mCEtAssetNumbers.setText("");
            mTvUserNumber.setText("");
            mTvUserName.setText("");
            mTvMeterAddr.setText("");

            mTvDaysFreezingTimeIn.setText("");
            mTvElectricityInByDay.setText("");
            mTvDaysFreezingTimeScan.setText("");
            mCEtElectricityScanByDay.setText("");
            mTvDifferencesByDay.setText("");

            mTvMonthFreezingTimeIn.setText("");
            mTvElectricityInByMonth.setText("");
            mTvMonthFreezingTimeScan.setText("");
            mCEtElectricityScanByMonth.setText("");
            mTvDifferencesByMonth.setText("");

            mAcceptanceBean = new AcceptanceBean();
        }



        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("saveAcceptanceObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }

        @Override
        public void onComplete() {
            if(!mBooleanSave)
                closeDialog();
        }
    };




    class ReceiveThread extends Thread {
        byte[] data;
        boolean threadStop = false;
        int tryCount = 0;
        String idPower = "";
        public ReceiveThread() {
            LogUtils.i("ReceiveThread ---- 被打开了");
            threadStop = false;
        }


        @Override
        public void run() {

            do {

                data = mInstance.receive();


                if (data == null || data.length == 0) {

                    continue;

                } else {
                    LogUtils.i("接收：" + StringUtility.bytes2HexString(data, data.length));
                    String result[] = ElectricMeterParsUtils.getMsg(data);

                    if (result != null && result.length == 3 && result[2].length() > 0) {
                        Message message = Message.obtain();
                        message.obj = result;
                        message.what = BROAD_READMETER_SUCCESS;
                        mHandler.sendMessage(message);
                    }
                }

            } while (!threadStop && !isInterrupted());
        }

        public void stopThread() {
            threadStop = true;
            Thread.currentThread().interrupt();
        }

        public boolean isStop()  {
            return threadStop;
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back_left:
                finish();
                break;

            case R.id.btn_menu_right:
                break;

            case R.id.btn_electricityScanByDay:                         	// 红外--日冻结
                mCurrentReadBtnId = R.id.btn_electricityScanByDay;
                mTimeType = TIME_TYPE_DAY;

                mTvDaysFreezingTimeScan.setText("");
                mCEtElectricityScanByDay.setText("");
                mTvDifferencesByDay.setText("");

                //---------------------------------------------
                mAcceptanceBean.setElectricityScanByDay("");
                mAcceptanceBean.setDifferencesByDay("");
                mAcceptanceBean.setConclusionByDay("");
                mAcceptanceBean.setDaysFreezingTimeScan("");
                mAcceptanceBean.setFinishByDay(false);

                String meterAddr1 = mTvMeterAddr.getText().toString().trim();
                startReadMeter(meterAddr1);

                break;

            case R.id.btn_electricityScanByMonth:                         	// 红外--月冻结
                mCurrentReadBtnId = R.id.btn_electricityScanByMonth;
                mTimeType = TIME_TYPE_MONTH;

                mTvMonthFreezingTimeScan.setText("");
                mCEtElectricityScanByMonth.setText("");
                mTvDifferencesByMonth.setText("");

                //---------------------------------------------
                mAcceptanceBean.setElectricityScanByMonth("");
                mAcceptanceBean.setDifferencesByMonth("");
                mAcceptanceBean.setConclusionByMonth("");
                mAcceptanceBean.setMonthFreezingTimeScan("");
                mAcceptanceBean.setFinishByMonth(false);


                String meterAddr2 = mTvMeterAddr.getText().toString().trim();
                startReadMeter(meterAddr2);

                break;


            case R.id.btn_assetNumbers:                         			// 2D扫描

                mCEtAssetNumbers.setText("");
                mTvUserNumber.setText("");
                mTvUserName.setText("");
                mTvMeterAddr.setText("");

                mTvDaysFreezingTimeIn.setText("");
                mTvElectricityInByDay.setText("");
                mTvDaysFreezingTimeScan.setText("");
                mCEtElectricityScanByDay.setText("");
                mTvDifferencesByDay.setText("");

                mTvMonthFreezingTimeIn.setText("");
                mTvElectricityInByMonth.setText("");
                mTvMonthFreezingTimeScan.setText("");
                mCEtElectricityScanByMonth.setText("");
                mTvDifferencesByMonth.setText("");

                //----------------------------------------------------
                mCurrentScanBtnId = R.id.btn_assetNumbers;

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }

                break;

            case R.id.btn_save:
                save();
                break;

            default:

                break;
        }
    }


}
