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
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.shen.sweetdialog.SweetAlertDialog;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.PicAdapter;
import com.zh.metermanagementcw.bean.ScatteredReplaceMeterBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.StringUtils;
import com.zh.metermanagementcw.utils.TimeUtils;
import com.zh.metermanagementcw.view.ClearEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 零散新装
 */
public class ScatteredReplaceMeterActivity extends BaseActivity implements View.OnClickListener {

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

    //----------------------------- 零散新装 --------------------------
    ScatteredReplaceMeterBean mScatteredReplaceMeterBean = new ScatteredReplaceMeterBean();

    //----------------------------- 用户信息 --------------------------
    /** 用户编号 -- 编辑框 -- cet_userNumber */
    ClearEditText mCEtUserNumber;
    /** 用户名称 -- 编辑框 -- cet_userName */
    ClearEditText mCEtUserName;
    /** 用户地址 -- 编辑框 -- cet_userAddr */
    ClearEditText mCEtUserAddr;
    /** 用户电话 -- 编辑框 -- cet_userPhone */
    ClearEditText mCEtUserPhone;

    //-----------------------------旧电表-------------------------------
    /** 旧电能表表地址 -- 编辑框 -- cet_oldAddr */
    private ClearEditText mCEtOldAddr;
    /** 旧表资产编号 -- 编辑框 -- cet_oldAssetNumbersScan */
    private ClearEditText mCEtOldAssetNumbers;
    /** 旧表资产编号(扫描) -- 按钮 -- btn_oldAssetNumbersScan*/
    private Button mBtnOldAssetNumbersScan;
    /** 旧电能表止码 -- 编辑框 -- cet_oldElectricity*/
    private ClearEditText mCEtOldElectricity;
    /** 旧电能表止码(扫描) -- 按钮 -- btn_oldElectricity*/
    private Button mBtnOldElectricity;

    //-----------------------------新电表-------------------------------
    /** 新电能表表地址 -- 编辑框*/
    private ClearEditText mCEtNewAddr;
    /** 新表资产编号 -- 编辑框*/
    private ClearEditText mCEtNewAssetNumbers;
    /** 新表资产编号(扫描) -- 按钮*/
    private Button mBtnNewAssetNumbersScan;
    /** 新电能表止码 -- 编辑框*/
    private ClearEditText mCEtNewElectricity;
    /** 新电能表止码(扫描) -- 按钮*/
    private Button mBtnNewElectricity;

    //--------------------------扫描-------------------------
    /** 成为 二维扫描 */
    Barcode2DWithSoft mBarcode2DWithSoft;
    ScanBack mScanBack;
    /** 当前二维扫描的按钮 */
    private int mCurrentScanBtnId = 1;


    //-------------------------新表的封扣---------------------
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

    //-----------------------------图片---------------------------

    /** 摄影 -- 按钮 */
    private Button mBtnCamera;
    /** 照片列表 -- 再生控件 */
    private RecyclerView mRvPic;


    /** 包裹预览图片的控件*/
    private LinearLayout mLayoutPv;
    /** 预览图片控件*/
    private PhotoView mPvCamaraPhoto;

    private Bitmap mBitmap;

    View mLlayoutParent;
    View mIvBg;
    /** 放大后存放图片的控件*/
    PhotoView mPvBgImg;
    Info mInfo;

    AlphaAnimation in;
    AlphaAnimation out;

    String mCurrentPicName = "";

    /** 保存数据是否成功 */
    boolean isSaveSuccess = true;
    /** 是否是正在保存数据 */
    boolean isSave = false;

    //---------------------------------电表图片-----------------------
    List<String> mPicPaths = new ArrayList<>();
    String[] mTemp;
    PicAdapter mPicAdapter;
    int mPhotoIndex = 0;

    //-----------------------------保存按钮-------------------------------
    /** 保存按钮 -- 按钮*/
    private Button mBtnSave;

    @Override
    public int getContentLayout() {
        return R.layout.activity_scattered_replace_meter;
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

        mTvTitle.setText("零散新装");
    }

    @Override
    public void initView() {

        mCEtUserNumber = (ClearEditText) findViewById(R.id.cet_userNumber);
        mCEtUserName = (ClearEditText) findViewById(R.id.cet_userName);
        mCEtUserAddr = (ClearEditText) findViewById(R.id.cet_userAddr);
        mCEtUserPhone = (ClearEditText) findViewById(R.id.cet_userPhone);

        mCEtOldAddr = (ClearEditText) findViewById(R.id.cet_oldAddr);
        mCEtOldAssetNumbers = (ClearEditText) findViewById(R.id.cet_oldAssetNumbersScan);
        mBtnOldAssetNumbersScan = (Button) findViewById(R.id.btn_oldAssetNumbersScan);
        mCEtOldElectricity = (ClearEditText) findViewById(R.id.cet_oldElectricity);
        mBtnOldElectricity = (Button) findViewById(R.id.btn_oldElectricity);

        mCEtNewAddr = (ClearEditText) findViewById(R.id.cet_newAddr);
        mCEtNewAssetNumbers = (ClearEditText) findViewById(R.id.cet_newAssetNumbersScan);
        mBtnNewAssetNumbersScan = (Button) findViewById(R.id.btn_newAssetNumbersScan);
        mCEtNewElectricity = (ClearEditText) findViewById(R.id.cet_newElectricity);
        mBtnNewElectricity = (Button) findViewById(R.id.btn_newElectricity);


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
        mBtnSave = (Button) findViewById(R.id.btn_save);


        mLayoutPv = (LinearLayout) findViewById(R.id.linearLayout_pv);
        mPvCamaraPhoto = (PhotoView) findViewById(R.id.pv_image);

        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.bg);
        mPvBgImg = (PhotoView) findViewById(R.id.img);
    }

    @Override
    public void initListener() {

        mBtnOldAssetNumbersScan.setOnClickListener(this);
        mBtnOldElectricity.setOnClickListener(this);

        mBtnNewAssetNumbersScan.setOnClickListener(this);
        mBtnNewElectricity.setOnClickListener(this);

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
        mLayoutPv.setOnClickListener(this);
        mPvBgImg.setOnClickListener(this);

        mBtnSave.setOnClickListener(this);
    }

    @Override
    public void initData() {

        mBtnOldAssetNumbersScan.setEnabled(false);
        mBtnNewAssetNumbersScan.setEnabled(false);

        mBtnMeterFootNumbersScan.setEnabled(false);
        mBtnMeterBodyNumbersScan1.setEnabled(false);
        mBtnMeterBodyNumbersScan2.setEnabled(false);

        //--------------------------------初始化红外-----------------------------------
        mScanBack = new ScanBack();
        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);


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

        //---------------------------------图片---------------------------------------
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);         // 水平
        mRvPic.setLayoutManager(manager);

        mPicAdapter = new PicAdapter(this, mPicPaths, new PicAdapter.PicListener() {
            @Override
            public void onDelete(int index, final String path) {                      // 删除资源

                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mScatteredReplaceMeterBean.setPicPath(StringUtils.deleteSubStr(mScatteredReplaceMeterBean.getPicPath(), path));
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                FilesUtils.broadCreateFile(getContext(), path);

                                mPicAdapter.setPathList(mScatteredReplaceMeterBean.getPicPath());
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
                setTitleIsShow(View.GONE);

            }
        });

        mRvPic.setAdapter(mPicAdapter);

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
    }


    /**
     * 解析地址
     *
     * @param str
     * @return
     */
    private String parseAddr(String str) {

        String addr = "AAAAAAAAAAAA";

        if(str.length() == 14){
            addr = str.substring(8, 14);            // 因为其他原因取 后6位

        }else if(str.length() == 24){
            addr = str.substring(12, 24);
        }else {

            if(str.length() >= 6)
                addr = str.substring(str.length() - 6, str.length());
            else
                addr = str;

        }

        LogUtils.i("addr:"+addr + "--addr.length():" + addr.length());

        return addr;
    }

    /**
     * 保存数据
     */
    private void saveDate() {

        String userNumber = mCEtUserNumber.getText().toString().trim();                     // 用户编号
        String userName = mCEtUserName.getText().toString().trim();                         // 用户名称
        String userAddr = mCEtUserAddr.getText().toString().trim();                         // 用户地址
        String userPhone = mCEtUserPhone.getText().toString().trim();                       // 用户电话

        String oldAddr = mCEtOldAddr.getText().toString().trim();                           // 旧电能表表地址
        String oldAssetNumbers = mCEtOldAssetNumbers.getText().toString().trim();   // 旧表资产编号
        String oldElectricity = mCEtOldElectricity.getText().toString().trim();             // 旧电能表止码

        
        String newAddr = mCEtNewAddr.getText().toString().trim();                           // 新电能表表地址
        String newAssetNumbers = mCEtNewAssetNumbers.getText().toString().trim();   // 新表资产编号
        String newElectricity = mCEtNewElectricity.getText().toString().trim();             // 新电能表止码

        String meterFootNumbersScan = mCEtMeterFootNumbersScan.getText().toString().trim();
        String meterBodyNumbersScan1 = mCEtMeterBodyNumbersScan1.getText().toString().trim();
        String mCeterBodyNumbersScan2 = mCEtMeterBodyNumbersScan2.getText().toString().trim();

        mScatteredReplaceMeterBean.setUserNumber(userNumber);
        mScatteredReplaceMeterBean.setUserName(userName);
        mScatteredReplaceMeterBean.setUserAddr(userAddr);
        mScatteredReplaceMeterBean.setUserPhone(userPhone);

        mScatteredReplaceMeterBean.setOldAddr(oldAddr);
        mScatteredReplaceMeterBean.setOldAssetNumbers(oldAssetNumbers);
        mScatteredReplaceMeterBean.setOldElectricity(oldElectricity);
        
        mScatteredReplaceMeterBean.setNewAddr(newAddr);
        mScatteredReplaceMeterBean.setNewAssetNumbers(newAssetNumbers);
        mScatteredReplaceMeterBean.setNewElectricity(newElectricity);

        mScatteredReplaceMeterBean.setMeterFootNumbers(meterFootNumbersScan);
        mScatteredReplaceMeterBean.setMeterBodyNumbers1(meterBodyNumbersScan1);
        mScatteredReplaceMeterBean.setMeterBodyNumbers2(mCeterBodyNumbersScan2);

        mScatteredReplaceMeterBean.setTime(TimeUtils.getCurrentTimeRq());

        if (TextUtils.isEmpty(newAssetNumbers)){
            showToast("请输入新表的资产编号");
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.SCATTEREDREPLACEMETER.userNumber.toString(), mScatteredReplaceMeterBean.getUserNumber());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.userName.toString(), mScatteredReplaceMeterBean.getUserName());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.userAddr.toString(), mScatteredReplaceMeterBean.getUserAddr());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.userPhone.toString(), mScatteredReplaceMeterBean.getUserNumber());

        contentValues.put(Constant.SCATTEREDREPLACEMETER.oldAddr.toString(), mScatteredReplaceMeterBean.getOldAddr());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.oldAssetNumbers.toString(), mScatteredReplaceMeterBean.getOldAssetNumbers());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.oldElectricity.toString(), mScatteredReplaceMeterBean.getOldElectricity());
        
        contentValues.put(Constant.SCATTEREDREPLACEMETER.newAddr.toString(), mScatteredReplaceMeterBean.getNewAddr());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.newAssetNumbers.toString(), mScatteredReplaceMeterBean.getNewAssetNumbers());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.newElectricity.toString(), mScatteredReplaceMeterBean.getNewElectricity());

        contentValues.put(Constant.SCATTEREDREPLACEMETER.meterFootNumbers.toString(), mScatteredReplaceMeterBean.getMeterFootNumbers());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.meterFootPicPath.toString(), mScatteredReplaceMeterBean.getMeterFootPicPath());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.meterBodyNumbers1.toString(), mScatteredReplaceMeterBean.getMeterBodyNumbers1());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.meterBodyPicPath1.toString(), mScatteredReplaceMeterBean.getMeterBodyPicPath1());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.meterBodyNumbers2.toString(), mScatteredReplaceMeterBean.getMeterBodyNumbers2());
        contentValues.put(Constant.SCATTEREDREPLACEMETER.meterBodyPicPath2.toString(), mScatteredReplaceMeterBean.getMeterBodyPicPath2());

        contentValues.put(Constant.SCATTEREDREPLACEMETER.picPath.toString(), mScatteredReplaceMeterBean.getPicPath());

        contentValues.put(Constant.SCATTEREDREPLACEMETER.time.toString(), mScatteredReplaceMeterBean.getTime());

        showLoadingDialog("", "正在保存数据...");

        taskPresenter1.saveScatteredReplaceMeterActivity(saveScatteredReplaceMeterObserver, contentValues, mScatteredReplaceMeterBean.getNewAssetNumbers());
    }

    /**
     * 成为2D扫描返回
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

                final String scanData = barCode.trim();
                if(mCurrentScanBtnId == R.id.btn_oldAssetNumbersScan){    // 旧表资产编号(二维扫描)

                    mBeepManager.playSuccessful();

                    mCEtOldAssetNumbers.setText(scanData);

                }else if(mCurrentScanBtnId == R.id.btn_newAssetNumbersScan){    // 新表资产编号(二维扫描)

                    mBeepManager.playSuccessful();
                    if(scanData.length() != 24) {

                        SweetAlertDialog dialog = new SweetAlertDialog(ScatteredReplaceMeterActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("提示")
                                .setContentText("该资产编码不是24位的是否输入")
                                .setConfirmText("是")
                                .setCancelText("否")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mCEtNewAssetNumbers.setText(scanData);
                                        mCEtNewAddr.setText(parseAddr(scanData));
                                        mCEtNewElectricity.setText("0");
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        mCEtNewAssetNumbers.setText("");
                                        mCEtNewAddr.setText("");
                                        mCEtNewElectricity.setText("");
                                        sweetAlertDialog.dismiss();
                                    }
                                });


                        dialog.setCancelable(false);
                        dialog.show();
                    }else {
                        mCEtNewAssetNumbers.setText(scanData);
                        mCEtNewAddr.setText(parseAddr(scanData));
                        mCEtNewElectricity.setText("0");
                    }

                }else if(mCurrentScanBtnId == R.id.btn_meterFootNumbersScan){
                    mBeepManager.playSuccessful();
                    mCEtMeterFootNumbersScan.setText(scanData);
                    mScatteredReplaceMeterBean.setMeterFootNumbers(scanData);
                }else if(mCurrentScanBtnId == R.id.btn_meterBodyNumbersScan1){
                    mBeepManager.playSuccessful();
                    mCEtMeterBodyNumbersScan1.setText(scanData);
                    mScatteredReplaceMeterBean.setMeterBodyNumbers1(scanData);
                }else if(mCurrentScanBtnId == R.id.btn_meterBodyNumbersScan2){
                    mBeepManager.playSuccessful();
                    mCEtMeterBodyNumbersScan2.setText(scanData);
                    mScatteredReplaceMeterBean.setMeterBodyNumbers2(scanData);
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
            mBtnOldAssetNumbersScan.setEnabled(true);
            mBtnNewAssetNumbersScan.setEnabled(true);

            mBtnMeterFootNumbersScan.setEnabled(true);
            mBtnMeterBodyNumbersScan1.setEnabled(true);
            mBtnMeterBodyNumbersScan2.setEnabled(true);
        }
    };

    /**
     * 保存 -- "零散新装"
     *
     * rxjava -- 主线程
     */
    Observer saveScatteredReplaceMeterObserver = new Observer<Long>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Long aLong) {
            LogUtils.i("aLong:" + aLong);
            Log.i("shen", "保存--零散新装--数据情况：" + (aLong>0 ? "成功" : "失败"));
            showToast("保存" + (aLong>0 ? "成功" : "失败"));

            if(aLong>0){
                mCEtUserNumber.setText("");
                mCEtUserName.setText("");
                mCEtUserAddr.setText("");
                mCEtUserPhone.setText("");

                mCEtOldAddr.setText("");
                mCEtOldAssetNumbers.setText("");
                mCEtOldElectricity.setText("");

                mCEtNewAddr.setText("");
                mCEtNewAssetNumbers.setText("");
                mCEtNewElectricity.setText("");

                mCEtMeterFootNumbersScan.setText("");
                mCEtMeterBodyNumbersScan1.setText("");
                mCEtMeterBodyNumbersScan2.setText("");

                mScatteredReplaceMeterBean = new ScatteredReplaceMeterBean();
                mPicAdapter.clearPathList();

                mCEtMeterFootNumbersScan.setText("");
                mRLayoutMeterFoot.setVisibility(View.GONE);
                mIBtnCameraMeterFoot.setVisibility(View.VISIBLE);

                mCEtMeterBodyNumbersScan1.setText("");
                mRLayoutMeterBody1.setVisibility(View.GONE);
                mIBtnCameraMeterBody1.setVisibility(View.VISIBLE);

                mCEtMeterBodyNumbersScan2.setText("");
                mRLayoutMeterBody2.setVisibility(View.GONE);
                mIBtnCameraMeterBody2.setVisibility(View.VISIBLE);

                //------------------------------------------------
                mBitmap = ImageFactory.getBitmap(Constant.CACHE_IMAGE_PATH + "no_preview_picture.png");
                mPvBgImg.setImageBitmap(mBitmap);
                mPvBgImg.enable();
            }
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


    @Override
    public void onClick(View v) {

        //String userName = mTvUserName.getText().toString().trim();
        String newAssetNumber = mCEtNewAssetNumbers.getText().toString().trim();

        switch (v.getId()) {

            case R.id.btn_back_left:
                finish();
                break;

            case R.id.btn_menu_right:
                break;

            case R.id.btn_oldAssetNumbersScan:                     // 旧表资产编号(二维扫描)
                mCurrentScanBtnId = R.id.btn_oldAssetNumbersScan;
                mCEtOldAssetNumbers.setText("");
                mCEtOldAddr.setText("");
                mCEtOldElectricity.setText("");

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.btn_newAssetNumbersScan:                     // 新表资产编号(二维扫描)
                mCurrentScanBtnId = R.id.btn_newAssetNumbersScan;
                mCEtNewAssetNumbers.setText("");
                mCEtNewAddr.setText("");
                mCEtNewElectricity.setText("");

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.btn_meterFootNumbersScan:        // 电表表脚封扣(扫描) -- 按钮

                if(StringUtils.isEmpty(newAssetNumber)){
                    showToast("请输入--新表资产编号");
                    return;
                }

                mCurrentScanBtnId = R.id.btn_meterFootNumbersScan;
                mCEtMeterFootNumbersScan.setText("");

                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.pv_meterFootPic:                                  // 电表表脚封扣(拍照后得到的照片) -- 图片
                mInfo = mPvCameraMeterFoot.getInfo();                   // 拿到pv_camaraPhoto的信息(如：位置)，用于动画
                mBitmap = ImageFactory.getBitmap(mScatteredReplaceMeterBean.getMeterFootPicPath());
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                setTitleIsShow(View.GONE);
                break;

            case R.id.iv_meterFootPicDelete:            // 电表表脚封扣(拍照后得到的照片的删除按钮) -- 按钮

                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String path = mScatteredReplaceMeterBean.getMeterFootPicPath();
                                mScatteredReplaceMeterBean.setMeterFootPicPath("");
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                FilesUtils.broadCreateFile(getContext(), path);

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
                if(StringUtils.isEmpty(newAssetNumber)){
                    showToast("请输入--新表资产编号");
                    return;
                }

                mCurrentPicName = Constant.ScatteredReplaceMeter_ExportPhone_Day_PATH + newAssetNumber + "_电表表脚封扣.jpg";

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
                if(StringUtils.isEmpty(newAssetNumber)){
                    showToast("请输入--新表资产编号");
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
                mBitmap = ImageFactory.getBitmap(mScatteredReplaceMeterBean.getMeterBodyPicPath1());
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                setTitleIsShow(View.GONE);
                break;

            case R.id.iv_meterBodyPicDelete1:            // 表箱封扣1(拍照后得到的照片的删除按钮) -- 按钮
                dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String path = mScatteredReplaceMeterBean.getMeterBodyPicPath1();
                                mScatteredReplaceMeterBean.setMeterBodyPicPath1("");
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                FilesUtils.broadCreateFile(getContext(), path);

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
                if(StringUtils.isEmpty(newAssetNumber)){
                    showToast("请输入--新表资产编号");
                    return;
                }
                mCurrentPicName = Constant.ScatteredReplaceMeter_ExportPhone_Day_PATH + newAssetNumber + "_表箱封扣1.jpg";

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
                if(StringUtils.isEmpty(newAssetNumber)){
                    showToast("请输入--新表资产编号");
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
                mBitmap = ImageFactory.getBitmap(mScatteredReplaceMeterBean.getMeterBodyPicPath2());
                mPvBgImg.setImageBitmap(mBitmap);
                mIvBg.startAnimation(in);             // 执行动画
                mIvBg.setVisibility(View.VISIBLE);
                mLlayoutParent.setVisibility(View.VISIBLE);
                mPvBgImg.animaFrom(mInfo);
                setTitleIsShow(View.GONE);
                break;

            case R.id.iv_meterBodyPicDelete2:            // 表箱封扣2(拍照后得到的照片的删除按钮) -- 按钮
                dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String path = mScatteredReplaceMeterBean.getMeterBodyPicPath2();
                                mScatteredReplaceMeterBean.setMeterBodyPicPath2("");
                                File file = new File(path);
                                if(file.exists()){
                                    file.delete();
                                }
                                FilesUtils.broadCreateFile(getContext(), path);

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
                if(StringUtils.isEmpty(newAssetNumber)){
                    showToast("请输入--新表资产编号");
                    return;
                }
                mCurrentPicName = Constant.ScatteredReplaceMeter_ExportPhone_Day_PATH + newAssetNumber + "_表箱封扣2.jpg";

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

                if(!TextUtils.isEmpty(newAssetNumber)) {

                    int size = 0;
                    if(StringUtils.isEmpty(mScatteredReplaceMeterBean.getPicPath())){
                        size = 0;
                    }else{
                        size = mScatteredReplaceMeterBean.getPicPath().split(",").length;
                    }
                    if(size > 3){
                        showToast("照片已超过4张");
                    }else {
                        mCurrentPicName = Constant.ScatteredReplaceMeter_ExportPhone_Day_PATH
                                + newAssetNumber + "_" + mPhotoIndex + ".jpg";

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
                    showToast("请输入--新表资产编号");
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


            case R.id.btn_save:

                saveDate();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBtnNewAssetNumbersScan.setEnabled(false);

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

                if (mPicAdapter != null && mScatteredReplaceMeterBean != null) {
                    mPicAdapter.addPath(mCurrentPicName);
                    //LogUtils.i("前：" + mMeterBean.getPicPath());

                    if (StringUtils.isEmpty(mScatteredReplaceMeterBean.getPicPath())) {
                        mScatteredReplaceMeterBean.setPicPath(mCurrentPicName);
                    } else {
                        mScatteredReplaceMeterBean.setPicPath(mScatteredReplaceMeterBean.getPicPath() + "," + mCurrentPicName);
                    }
                    //LogUtils.i("后：" + mMeterBean.getPicPath());
                    FilesUtils.broadCreateFile(getContext(), mCurrentPicName);
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

                if (mScatteredReplaceMeterBean != null) {

                    mRLayoutMeterFoot.setVisibility(View.VISIBLE);
                    mIBtnCameraMeterFoot.setVisibility(View.GONE);

                    mScatteredReplaceMeterBean.setMeterFootPicPath(mCurrentPicName);
                    mPvCameraMeterFoot.setImageBitmap(ImageFactory.getBitmap(mScatteredReplaceMeterBean.getMeterFootPicPath()));
                    FilesUtils.broadCreateFile(getContext(), mCurrentPicName);
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

                if (mScatteredReplaceMeterBean != null) {

                    mRLayoutMeterBody1.setVisibility(View.VISIBLE);
                    mIBtnCameraMeterBody1.setVisibility(View.GONE);

                    mScatteredReplaceMeterBean.setMeterBodyPicPath1(mCurrentPicName);
                    mPvCameraMeterBody1.setImageBitmap(ImageFactory.getBitmap(mScatteredReplaceMeterBean.getMeterBodyPicPath1()));

                    FilesUtils.broadCreateFile(getContext(), mCurrentPicName);
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

                if (mScatteredReplaceMeterBean != null) {

                    mRLayoutMeterBody2.setVisibility(View.VISIBLE);
                    mIBtnCameraMeterBody2.setVisibility(View.GONE);

                    mScatteredReplaceMeterBean.setMeterBodyPicPath2(mCurrentPicName);
                    mPvCameraMeterBody2.setImageBitmap(ImageFactory.getBitmap(mScatteredReplaceMeterBean.getMeterBodyPicPath2()));

                    FilesUtils.broadCreateFile(getContext(), mCurrentPicName);
                }
            }

        }

    }

}
