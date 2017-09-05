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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.shen.sweetdialog.SweetAlertDialog;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.MeterContentAdapter1;
import com.zh.metermanagementcw.adapter.PicAdapter;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.CollectorNumberBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.StringUtils;
import com.zh.metermanagementcw.view.ClearEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 新装采集器
 *
 */
public class NewCollectorActivity1 extends BaseActivity implements View.OnClickListener {

    /** 拍照获取图片 -- 电表 */
    public static final int TAKE_PHOTO_METER = 1000;
    /** 拍照获取图片 -- 采集器 */
    public static final int TAKE_PHOTO_COLLECTOR = 2000;

    public static final int RESULT_OK = 1;


    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 采集器编号 -- 编辑框 */
    ClearEditText mCEtCollectorNumbers;
    /** 采集器编号 -- 按钮 */
    Button mBtnCollectorNumbers;



    //--------------------------扫描-------------------------
    /** 成为 二维扫描 */
    Barcode2DWithSoft mBarcode2DWithSoft;
    ScanBack mScanBack;
    /** 当前二维扫描的按钮 */
    private int mCurrentScanBtnId = 1;

    //    /** 扫描类 */
//    private ScanInterface scanDecode;
    //--------------------------------------------------------

    private String mScanDate;

    /** 存放(电表资产编号) -- 列表 */
    private ListView mLvMeterContent;

    /** 扫描电表资产编号 -- 按钮*/
    Button mBtnScan;
    /** 保存 -- 按钮*/
    Button mBtnSave;

    MeterContentAdapter1 mMeterContentAdapter;


    /** 用户/表主的信息 */
    private  List<MeterBean1> mMeterBeanList;

    /** 用户/表主的信息 -- 扫描后获取的 */
    private  ArrayList<MeterBean1> mMeterBeanListScan;

    //--------------------------------------------------------


    /** 包裹预览图片的控件*/
    private LinearLayout mLayoutPv;

    private Bitmap mBitmap;

    View mLlayoutParent;
    View mIvBg;
    /** 放大后存放图片的控件*/
    PhotoView mPvBgImg;
    Info mInfo;

    AlphaAnimation in;
    AlphaAnimation out;

    String mCurrentPicName = "";

    /** 扫描列表中的 那个数据的下标 */
    int mCamaraindex = 0;

    //---------------------------------图片-----------------------
    /** 摄影 -- 按钮 */
    private Button mBtnCamera;
    /** 照片列表 -- 再生控件 */
    private RecyclerView mRvPic;

    String[] mTemp;
    PicAdapter mPicAdapter;
    long mPhotoIndex = 0;

    CollectorNumberBean mCollectorNumberBean = new CollectorNumberBean();

    @Override
    public int getContentLayout() {
        return R.layout.activity_new_collector1;
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

        mTvTitle.setText("新装采集器");
    }

    @Override
    public void initView() {


        mCEtCollectorNumbers = (ClearEditText) findViewById(R.id.cet_collectorNumbers);
        mBtnCollectorNumbers = (Button) findViewById(R.id.btn_collectorNumbers);

        mLvMeterContent = (ListView) findViewById(R.id.lv_meterContent);

        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mBtnSave = (Button) findViewById(R.id.btn_save);

        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        mRvPic = (RecyclerView) findViewById(R.id.rv_pic);

        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.iv_bg);
        mPvBgImg = (PhotoView) findViewById(R.id.pv_bg);
    }

    @Override
    public void initListener() {
        mBtnCollectorNumbers.setOnClickListener(this);

        mBtnCamera.setOnClickListener(this);
        mPvBgImg.setOnClickListener(this);

        mBtnScan.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
    }

    @Override
    public void initData() {

        mBtnCollectorNumbers.setEnabled(false);
        mBtnScan.setEnabled(false);

        //LogUtils.i("MyApplication.getCurrentMeteringSection():" + MyApplication.getCurrentMeteringSection());

        mMeterBeanList = new ArrayList<>();
        mMeterBeanListScan = new ArrayList<>();

        showLoadingDialog("","正在加载数据...");
        // 从数据库中加载数据
        taskPresenter1.readDbToBean(readObserver, MyApplication.getCurrentMeteringSection());

        mCEtCollectorNumbers.setEnabled(false);

        //---------------------------------图片 采集器 ---------------------------------------
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);         // 水平
        mRvPic.setLayoutManager(manager);

        mPicAdapter = new PicAdapter(this, new ArrayList<String>(), new PicAdapter.PicListener() {
            @Override
            public void onDelete(final int index, final String path) {                      // 删除资源

                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该图片")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                mCollectorNumberBean.setCollectorPicPath(
                                        StringUtils.deleteSubStr(mCollectorNumberBean.getCollectorPicPath(), path));
                                File file = new File(path);
                                if(file.exists()){
                                    FilesUtils.broadCreateFile(getContext(), file);
                                    file.delete();
                                }

                                mPicAdapter.setPathList(mCollectorNumberBean.getCollectorPicPath());
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

        //---------------------------------图片 采集器下的电表 ---------------------------------------

        mMeterContentAdapter = new MeterContentAdapter1(this, mMeterBeanListScan, new MeterContentAdapter1.MeterContentListener(){
            @Override
            public void onDeletePic(final int position, final String path) {

                if(mMeterBeanListScan.size() > position) {
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("提示")
                            .setContentText("是否删除该图片")
                            .setConfirmText("是")
                            .setCancelText("否")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    MeterBean1 meterBean = mMeterBeanListScan.get(position);
                                    if (StringUtils.isEmpty(meterBean.getMeterPicPath())) {

                                    } else {
                                        String tempPath = meterBean.getMeterPicPath();
                                        File file = new File(tempPath);
                                        if (file.exists()) {
                                            FilesUtils.deleteFile(getContext(), file);
                                        }
                                    }

                                    mMeterBeanListScan.get(position).setMeterPicPath("");
                                    mMeterContentAdapter.setItemList(mMeterBeanListScan);

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
            }

            @Override
            public void onPreView(int position, String path, Info info) {

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

            @Override
            public void onCamera(int position) {
                if (mMeterBeanListScan.size() > position) {
                    mCamaraindex = position;

//                    mCurrentPicName = Constant.IMAGE_PATH +
//                            mMeterBeanListScan.get(position).getUserName() + "_" +
//                            mMeterBeanListScan.get(position).getOldAssetNumbers() + ".jpg";

                    mCurrentPicName = MyApplication.getNoWorkOrderPath().getNewCollectorPhotoPath() +
                            mMeterBeanListScan.get(position).getUserName() + "_" +
                            mMeterBeanListScan.get(position).getOldAssetNumbers() + ".jpg";
                    LogUtils.i("mCurrentPicName:" + mCurrentPicName);

                    if(mBarcode2DWithSoft!=null){
                        mBarcode2DWithSoft.stopScan();
                        mBarcode2DWithSoft.close();
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Constant.CACHE_IMAGE_PATH  + "CacheImage.jpg");  // 携带图片存放路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, TAKE_PHOTO_METER);
                }
            }
            @Override
            public void onDeleteMeter(final int position, String path) {
                LogUtils.i("mMeterBeanListScan.size():"+mMeterBeanListScan.size()
                        +"     position:" + position);

                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("提示")
                        .setContentText("是否删除该电表")
                        .setConfirmText("是")
                        .setCancelText("否")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                if(mMeterBeanListScan.size() > position) {
                                    String tempPath = mMeterBeanListScan.get(position).getMeterPicPath();
                                    if(StringUtils.isNotEmpty(tempPath)) {
                                        File file = new File(tempPath);
                                        if (file.exists()) {
                                            FilesUtils.deleteFile(getContext(), file);
                                        }
                                    }
                                    mMeterBeanListScan.remove(position);
                                    mMeterContentAdapter.setItemList(mMeterBeanListScan);
                                }

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
        });

        mLvMeterContent.setAdapter(mMeterContentAdapter);



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


        mScanBack = new ScanBack();
        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);
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
        if(mBarcode2DWithSoft!=null){
            mBarcode2DWithSoft.stopScan();
            mBarcode2DWithSoft.close();
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBtnCollectorNumbers.setEnabled(false);
        mBtnScan.setEnabled(false);
        taskPresenter1.initBarcode2D(initBarcode2DSObserver, getContext(), mScanBack);

        if (requestCode == TAKE_PHOTO_METER) {                       // 拍照获取图片
            if (resultCode == Activity.RESULT_OK) {
                try {
                    ImageFactory.ratioAndGenThumb(Constant.CACHE_IMAGE_PATH  + "CacheImage.jpg",
                            mCurrentPicName, 1000, 1000, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                mMeterBeanListScan.get(mCamaraindex).setMeterPicPath(mCurrentPicName);
                mMeterContentAdapter.setItemList(mMeterBeanListScan);

                FilesUtils.broadCreateFile(getContext(),  new File(mCurrentPicName));
            }
        }else if (requestCode == TAKE_PHOTO_COLLECTOR){
            if (resultCode == Activity.RESULT_OK) {

                try {
                    ImageFactory.ratioAndGenThumb(Constant.CACHE_IMAGE_PATH + "CacheImage.jpg",
                            mCurrentPicName, 1000, 1000, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (mPicAdapter != null && mCollectorNumberBean != null) {
                    mPicAdapter.addPath(mCurrentPicName);
                    //LogUtils.i("前：" + mMeterBean.getPicPath());

                    if (StringUtils.isEmpty(mCollectorNumberBean.getCollectorPicPath())) {
                        mCollectorNumberBean.setCollectorPicPath(mCurrentPicName);
                    } else {
                        mCollectorNumberBean.setCollectorPicPath(mCollectorNumberBean.getCollectorPicPath()
                                + "," + mCurrentPicName);
                    }
                    //LogUtils.i("后：" + mMeterBean.getPicPath());
                    FilesUtils.broadCreateFile(getContext(),  new File(mCurrentPicName));
                    mPhotoIndex++;
                }


            }
        }
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

            case R.id.btn_collectorNumbers:             // 采集器

                mCEtCollectorNumbers.setText("");
                mPicAdapter.clearPathList();
                mMeterContentAdapter.clearPathList();
                mCollectorNumberBean.clean();
                mMeterBeanListScan.clear();

                mCurrentScanBtnId = R.id.btn_collectorNumbers;
                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;

            case R.id.btn_scan:                         // 扫描资产编号
                mCurrentScanBtnId = R.id.btn_scan;
                if(mBarcode2DWithSoft != null) {
                    mBarcode2DWithSoft.stopScan();
                    mBarcode2DWithSoft.scan();                              //启动扫描
                }
                break;


            case R.id.btn_camera:

                String collectorNumbers = mCEtCollectorNumbers.getText().toString().trim();
                if(!TextUtils.isEmpty(collectorNumbers)) {
                    int size = 0;
                    if(StringUtils.isEmpty(mCollectorNumberBean.getCollectorPicPath())){
                        size = 0;
                    }else {
                        size = mCollectorNumberBean.getCollectorPicPath().split(",").length;
                    }

                    if(size > 3){
                        showToast("照片已超过4张");

                    }else {

                        //mCurrentPicName = Constant.IMAGE_PATH + collectorNumbers + "_" + mPhotoIndex + ".jpg";
                        mCurrentPicName = MyApplication.getNoWorkOrderPath().getNewCollectorPhotoPath()
                                + collectorNumbers +  "_" + mPhotoIndex + ".jpg";

                        if(mBarcode2DWithSoft!=null){
                            mBarcode2DWithSoft.stopScan();
                            mBarcode2DWithSoft.close();
                            mBarcode2DWithSoft = null;
                        }

                        LogUtils.i("mCurrentPicName:" + mCurrentPicName);

                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Constant.CACHE_IMAGE_PATH  + "CacheImage.jpg");  // 携带图片存放路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, TAKE_PHOTO_COLLECTOR);
                    }
                }else{
                    showToast("请输入--采集器资产编号");
                }


                break;


            case R.id.btn_save:                         // 保存

                String strCollectorNumbers = mCEtCollectorNumbers.getText().toString().trim();

                if(!TextUtils.isEmpty(strCollectorNumbers)) {
                    if(mMeterBeanListScan.size() != 0) {

                        showLoadingDialog("","正在保存数据...");
                        taskPresenter1.addCollectorToMeterInfo(saveCollectorToMeterInfoObserver,
                                MyApplication.getCurrentMeteringSection(),
                                strCollectorNumbers,
                                mMeterBeanListScan);

                    }else{
                        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(NewCollectorActivity1.this, SweetAlertDialog.NORMAL_TYPE)
                                //.setTitleText("正在加载");
                                .setTitleText("提示")
                                .setContentText("请输入电表的资产编码")
                                .setConfirmText("确认")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });
                        mSweetAlertDialog.show();
                    }
                } else {
                    SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(NewCollectorActivity1.this, SweetAlertDialog.NORMAL_TYPE)
                            //.setTitleText("正在加载");
                            .setTitleText("提示")
                            .setContentText("请输入采集器编号")
                            .setConfirmText("确认")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                    mSweetAlertDialog.show();
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


    /**
     *
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
            }else {
                LogUtils.i("扫描成功");
                String barCode = new String(bytes, 0, length);

                String data = barCode.trim();
                mScanDate = data;

                if (mCurrentScanBtnId == R.id.btn_collectorNumbers) {              // 采集器
                    mBeepManager.playSuccessful();
                    mCEtCollectorNumbers.setText(data);

                    mCollectorNumberBean.setCollectorNumbers(mScanDate);
                    taskPresenter1.getCollector(getCollctorObserver, MyApplication.getCurrentMeteringSection(), data);


                } else if (mCurrentScanBtnId == R.id.btn_scan) {                    // 扫描资产编号

                    boolean isAllExist = false;                                    // 在"全部的数据"是否存在
                    boolean isScanExist = false;                                    // 在"扫描的数据"是否存在

                    MeterBean1 tempBean = new MeterBean1();
                    for (MeterBean1 bean : mMeterBeanList) {
                        if (bean.getOldAssetNumbers().equals(data)) {
                            for (MeterBean1 meterBean : mMeterBeanListScan) {
                                if (meterBean.getOldAssetNumbers().equals(data)) {
                                    mBeepManager.playError();
                                    SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(NewCollectorActivity1.this, SweetAlertDialog.NORMAL_TYPE)
                                            //.setTitleText("正在加载");
                                            .setTitleText("提示")
                                            .setContentText("该资产编码已添加")
                                            .setConfirmText("确认")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                    sweetAlertDialog.dismiss();
                                                }
                                            });
                                    mSweetAlertDialog.show();
                                    isScanExist = true;                  // 扫描中存在
                                    break;
                                }
                            }
                            isAllExist = true;                  // 全部中存在
                            if (isScanExist) {
                                break;
                            }
                            tempBean = bean;
                        }
                    }

                    if (isAllExist && (!isScanExist)) {
                        mBeepManager.playSuccessful();
                        mMeterBeanListScan.add(0, tempBean);
                        mMeterContentAdapter.setItemList(mMeterBeanListScan);
                    }
                    if (!isAllExist) {
                        mBeepManager.playError();
                        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(NewCollectorActivity1.this, SweetAlertDialog.NORMAL_TYPE)
                                //.setTitleText("正在加载");
                                .setTitleText("提示")
                                .setContentText("该资产编码无匹配的用户数据,\n请核实后再次操作")
                                .setConfirmText("确认")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismiss();
                                    }
                                });
                        mSweetAlertDialog.show();
                    }


                }
            }
            //scaning=false;
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
            mBtnCollectorNumbers.setEnabled(true);
            mBtnScan.setEnabled(true);
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

            LogUtils.i("meterBeen.size()" + meterBeen.size());
            MyApplication.setMeterBean1List(meterBeen);

            mMeterBeanList = meterBeen;
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("readObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }

        @Override
        public void onComplete() {
            closeDialog();
        }
    };



    /**
     * 保存数据！ -- 添加采集器 -- 到"电表详情的那张表"
     * rxjava -- 主线程
     */
    Observer saveCollectorToMeterInfoObserver = new Observer<Long>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Long aLong) {
            //LogUtils.i("aLong:" + aLong);
            //Log.i("shen", "保存情况：" + (aLong>0 ? "成功" : "失败"));
            //showToast("保存" + (aLong>0 ? "成功" : "失败"));
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("saveCollectorToMeterInfoObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }


        @Override
        public void onComplete() {
            //showToast("保存成功");
            //closeDialog();
            taskPresenter1.addCollectorToCollectorTable(saveCollectorToCollectorTableObserver,
                    MyApplication.getCurrentMeteringSection(),
                    mCollectorNumberBean);
        }
    };

    /**
     * 保存数据！ -- 添加采集器 -- 到"所有采集器那张表"
     * rxjava -- 主线程
     */
    Observer saveCollectorToCollectorTableObserver = new Observer<Long>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Long aLong) {
            //LogUtils.i("aLong:" + aLong);
            //Log.i("shen", "保存情况：" + (aLong>0 ? "成功" : "失败"));
            //showToast("保存" + (aLong>0 ? "成功" : "失败"));
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("saveCollectorToCollectorTableObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }


        @Override
        public void onComplete() {
            showToast("保存成功");

            mCEtCollectorNumbers.setText("");
            mPicAdapter.clearPathList();
            mMeterContentAdapter.clearPathList();
            mCollectorNumberBean.clean();
            mMeterBeanListScan.clear();

            closeDialog();
        }
    };


    /**
     * 获取采集器
     *
     * rxjava -- 主线程
     */
    Observer getCollctorObserver = new Observer<CollectorNumberBean>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull CollectorNumberBean collectorNumberBeen) {

            if(mCollectorNumberBean.getCollectorNumbers().equals(collectorNumberBeen.getCollectorNumbers()))
                mCollectorNumberBean = collectorNumberBeen;
        }


        @Override
        public void onError(@NonNull Throwable e) {
            LogUtils.i("getCollctorObserver e.getMessage()" + e.getMessage());
            closeDialog();
        }


        @Override
        public void onComplete() {

                if(StringUtils.isEmpty(mCollectorNumberBean.getCollectorPicPath())){
                    mPhotoIndex = 1;
                }else {
                    String path = mCollectorNumberBean.getCollectorPicPath();
                    for(String tempPath : path.split(",")){
                        if(!(new File(tempPath).exists())){
                            path = StringUtils.deleteSubStr(path, tempPath);
                        }
                    }
                    mCollectorNumberBean.setCollectorPicPath(path);
                    if(StringUtils.isNotEmpty(mCollectorNumberBean.getCollectorPicPath())) {
                        try {
                            mPhotoIndex = Integer.parseInt(path.substring(path.lastIndexOf("_") + 1, path.lastIndexOf("."))) + 1;
                        } catch (Exception e) {
                        }
                    }
                    mPicAdapter.setPathList(mCollectorNumberBean.getCollectorPicPath());
                }

            //closeDialog();
            taskPresenter1.readDbToBeanForCollector(getMeterInfoForCollctorObserver,
                    MyApplication.getCurrentMeteringSection(),
                    mCollectorNumberBean.getCollectorNumbers());
        }
    };

    /**
     * 将数据从数据库读取到内存中 -- 根据采集器
     * rxjava -- 主线程
     */
    Observer getMeterInfoForCollctorObserver = new Observer<List<MeterBean1>>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<MeterBean1> meterBeen) {

            LogUtils.i("meterBeen.size()" + meterBeen.size());
            MyApplication.setMeterBean1List(meterBeen);

            mMeterBeanListScan = (ArrayList<MeterBean1>) meterBeen;
            mMeterContentAdapter.setItemList(mMeterBeanListScan);
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
