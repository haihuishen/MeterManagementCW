package com.zh.metermanagementcw.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.shen.sweetdialog.SweetAlertDialog;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.CopyFileUtils;
import com.zh.metermanagementcw.utils.FilesUtils;
import com.zh.metermanagementcw.utils.GetDeviceIDUtil;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.POIExcelUtil;
import com.zh.metermanagementcw.utils.ResourceUtil;
import com.zh.metermanagementcw.utils.ShortCut;
import com.zh.metermanagementcw.xml.PullSerialNumberParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 闪屏<p>
 *
 * Splash [splæʃ] vt.溅，泼；用...使液体飞溅	n.飞溅的水；污点；卖弄		vi.溅湿；溅开<p>
 *
 * 现在大部分APP都有Splash界面，下面列一下Splash页面的几个作用：<p>
 * 1、展示logo,提高公司形象<br>
 * 2、初始化数据 (拷贝数据到SD)<br>
 * 3、提高用户体验 <br>
 * 4、连接服务器是否有新的版本等。<br>
 *
 *     //implements UncaughtExceptionHandler
 *     //在onCreate()调用下面方法，才能捕获到线程中的异常
 *      Thread.setDefaultUncaughtExceptionHandler(this);
 */
public class SplashActivity extends Activity implements Thread.UncaughtExceptionHandler {


    private Handler handler = new Handler();
    private Runnable runnable;

    private TextView mTvCountDown;      // tv_count_down

    MyCountDownTimer myCountDownTimer;  // 倒计时器

    private final int REQUESTCODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!isTaskRoot()) {
            finish();
            return;

        }

        handler.postDelayed(runnable = new Runnable(){                   // 发送个消息(runnable 可执行事件)到"消息队列中"，延时执行

            @Override
            public void run(){

                Intent intent = new Intent(SplashActivity.this, SelectorMainActivity.class);        // 跳转到主页面
                startActivity(intent);
                finish();
            }
        }, 4000);

        //在此调用下面方法，才能捕获到线程中的异常
        //Thread.setDefaultUncaughtExceptionHandler(this);


//        LogUtils.i("getDeviceId():" + GetDeviceIDUtil.getDeviceId(getApplicationContext()));
//        LogUtils.i("getWLAN_MAC():" + GetDeviceIDUtil.getWLAN_MAC(getApplicationContext()));
//        LogUtils.i("getBT_MAC():" + GetDeviceIDUtil.getBT_MAC());
//
//        //------------------------------------ 获取唯一标识 -------------------------------------------
//        initSerialNumberXML("SerialNumber.xml");
//        PullSerialNumberParser parser = new PullSerialNumberParser();
//        try {
//            parser.parser();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        boolean isZhDevice = false;
//        String serialNumber = GetDeviceIDUtil.getSerialNumber();
//        for(String s : MyApplication.getSerialNumberList()){
//            LogUtils.i("serialNumber:" + serialNumber + "    s:" + s);
//            if(serialNumber.equalsIgnoreCase(s)){
//                isZhDevice = true;
//                break;
//            }
//        }
//
//        if (isZhDevice) {
//            Toast.makeText(this, "设备初始化失败！", Toast.LENGTH_SHORT).show();
//
//            //如果之前创建了Runnable对象,那么就把这任务移除
//            if(runnable!=null){
//                handler.removeCallbacks(runnable);
//            }
//
//            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                    .setTitleText("提示")
//                    .setContentText("设备初始化失败!")
//                    .setConfirmText("确认")
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            MyApplication.exitApp();
//                            sweetAlertDialog.dismiss();
//                        }
//                    });
//
//
//            dialog.setCancelable(false);
//            dialog.show();
//        }


        String sam = GetDeviceIDUtil.getDeviceNum();
        Log.i("shen", "getDeviceNum():"+sam);

        //sam = sam.toLowerCase();
        //if (!sam.contains("kt45") || !sam.contains("c70sc")) {

        if (!sam.equalsIgnoreCase("ZH-E7")) {
            //如果之前创建了Runnable对象,那么就把这任务移除
            if(runnable!=null){
                handler.removeCallbacks(runnable);
            }

            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("提示")
                    .setContentText("设备初始化失败!")
                    .setConfirmText("确认")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            MyApplication.exitApp();
                            sweetAlertDialog.dismiss();
                        }
                    });

            dialog.setCancelable(false);
            dialog.show();
        }


        FilesUtils.createFile(this, Constant.srcPathDir);
        FilesUtils.createFile(this, Constant.IMPORT_METER_INFO_PATH);
        FilesUtils.createFile(this, Constant.IMPORT_PHONE_PATH);
        FilesUtils.createFile(this, Constant.CACHE_IMAGE_PATH);
        FilesUtils.createFile(this, Constant.DIRECTIONSFORUSEIMAGE_PATH);
        FilesUtils.createFile(this, Constant.SetCopyTransformation_PATH);

        FilesUtils.createFile(this, Constant.Acceptance_PATH);
        FilesUtils.createFile(this, Constant.Acceptance_Day_PATH);
        FilesUtils.createFile(this, Constant.Acceptance_Month_PATH);
        FilesUtils.createFile(this, Constant.Acceptance_ExportExcel_Day_PATH);
        FilesUtils.createFile(this, Constant.Acceptance_ExportExcel_Month_PATH);

        FilesUtils.createFile(this, Constant.BatchReplaceMeter_ImportInfo_PATH);
        FilesUtils.createFile(this, Constant.BatchReplaceMeter_ExportInfo_PATH);
        FilesUtils.createFile(this, Constant.BatchNewMeter_ImportInfo_PATH);
        FilesUtils.createFile(this, Constant.BatchNewMeter_ExportInfo_PATH);

        FilesUtils.createFile(this, Constant.ScatteredReplaceMeter_PATH);               // .../电表换装/零散换表/
        FilesUtils.createFile(this, Constant.ScatteredNewMeter_PATH);                   // .../电表换装/零散新装/



        initSrc(Constant.DIRECTIONSFORUSEIMAGE_PATH, "no_preview_picture.png");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            LogUtils.i("shen1");
//            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
//                LogUtils.i("shen2");
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
//            }
//        }else {
//            LogUtils.i("shen3");
//            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                    .setTitleText("提示")
//                    .setContentText("运行创建文件夹")
//                    .setConfirmText("确认")
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            //type_finish();
//                            MyApplication.exitApp();
//                            sweetAlertDialog.dismiss();
//                        }
//                    });
//
//
//            dialog.setCancelable(false);
//            dialog.show();
//
////            FilesUtils.createFile(this, Constant.srcPathDir);
////            FilesUtils.createFile(this, Constant.excelPathDir);
////            FilesUtils.createFile(this, Constant.IMPORT_METER_INFO_PATH);
////            FilesUtils.createFile(this, Constant.IMPORT_PHONE_PATH);
////            FilesUtils.createFile(this, Constant.CACHE_IMAGE_PATH);
////            FilesUtils.createFile(this, Constant.IMAGE_PATH);
////            FilesUtils.createFile(this, Constant.DIRECTIONSFORUSEIMAGE_PATH);
////            FilesUtils.createFile(this, Constant.SetCopyTransformation_PATH);
////
////            initSrc(Constant.CACHE_IMAGE_PATH,"no_preview_picture.png");
//            //initSrc(Constant.CACHE_IMAGE_PATH,"CacheImage.jpg");
//        }




        CopyFileUtils.copyFile(this.getDatabasePath(Constant.DB_NAME).getAbsolutePath(),
                Constant.CACHE_IMAGE_PATH + Constant.DB_NAME);
        FilesUtils.broadCreateFile(this, new File(Constant.CACHE_IMAGE_PATH + Constant.DB_NAME));


        // 在桌面生成图标
        ShortCut.createShortCut(this, ResourceUtil.getMipmapResIDByName(this, "app_icon"),
                ResourceUtil.getStringResIDByName(this, "app_name"));



//        //内存地址
//        public static String root = Environment.getExternalStorageDirectory().getPath();
//        /** 获取SD可用容量 */
//    private static long getAvailableStorage() {
//
//        StatFs statFs = new StatFs(root);
//        long blockSize = statFs.getBlockSize();
//        long availableBlocks = statFs.getAvailableBlocks();
//        long availableSize = blockSize * availableBlocks;
//        // Formatter.formatFileSize(context, availableSize);
//        return availableSize;
//    }
//        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
//                && getAvailableStorage()>1000000) {
//
//            // Toast.makeText(context, "SD卡不可用", Toast.LENGTH_LONG).show();
//
//            Log.i("shen","SD卡不可用");
//            return "请清理空间";
//        }

        initView();
        initListener();
        initData();

    }



    private void initView() {
        mTvCountDown = (TextView) findViewById(R.id.tv_count_down);

    }

    private void initListener(){

        mTvCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SelectorMainActivity.class);        // 跳转到主页面
                startActivity(intent);
                finish();
                //如果之前创建了Runnable对象,那么就把这任务移除
                if(runnable!=null){
                    handler.removeCallbacks(runnable);
                }
            }
        });


    }

    private void initData() {
        myCountDownTimer = new MyCountDownTimer(4000, 1000);
        myCountDownTimer.start();

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull String s) throws Exception {

                        // File Context.getFilesDir();
                        // 该方法返回指向/data/data/<Package Name>/files/目录的一个File对象。
                        for(int i=1; i<=45; i++) {
                            File file;
                            if ((file = new File(Constant.DIRECTIONSFORUSEIMAGE_PATH)).exists()) {                                      //  创建路径（文件夹）
                                file.delete();
                            }
                            initSrc(Constant.DIRECTIONSFORUSEIMAGE_PATH , i + ".png");
                            //initSrc(DIRECTIONSFORUSEIMAGE_PATH , i + ".png");
                        }

                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Boolean aBoolean) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }





    /**
     * 继承 CountDownTimer 防范
     *
     * 重写 父类的方法 onTick() 、 onFinish()
     */
    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数<br>
         * 例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法<br>
         * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        public void onFinish() {
            mTvCountDown.setTextSize(10);
            mTvCountDown.setText("正在跳转");
        }
        public void onTick(long millisUntilFinished) {
            // mTvCountDown.setText("倒计时(" + millisUntilFinished / 1000 + ")");
            mTvCountDown.setTextSize(25);
            mTvCountDown.setText(millisUntilFinished / 1000 +"");
        }
    }



    /**
     * 拷贝数据库（xx.db）到files文件夹下
     * <br>注1：只在第一次打开应用才拷贝,第二次会判断有没有这个数据库
     * <br>注2：xx.db拷贝到工程目录assets目录下
     * <br>拿到files文件夹：File files = getFilesDir();
     * <br>如：复制后的data/data/com.shen.accountbook/files/xx.db"
     *
     * @param dbName	数据库名称
     */
    private void initSerialNumberXML(String dbName) {
        //1,在files文件夹下创建同名dbName数据库文件过程
        File files = getFilesDir();
        // 在files文件夹下生成一个"dbName名字"的文件
        File file = new File(files, dbName);

        // 如果"dbName名字的文件" 存在  （如第二次进入）
        if(file.exists()){
            //return;
            file.delete();
            file = new File(files, dbName);
        }

        InputStream stream = null;
        FileOutputStream fos = null;

        //2,输入流读取assets目录下的文件
        try {
            // getAssets()拿到"资产目录"的文件夹（工程目录下的assets目录）
            // ***打开"dbName名字的文件"    （拿到他的输入流）
            stream = getAssets().open(dbName);

            //3,将读取的内容写入到指定文件夹的文件中去
            // ***拿到"file文件"的"输出流"
            fos = new FileOutputStream(file);

            //4,每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs))!=-1){	// 將"输入流"（stream）读到"bs"
                fos.write(bs, 0, temp);				// 將"bs"写到"fos"（输出流）
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(stream!=null && fos!=null){	// "流"非等于"null",说明没有关闭
                try {
                    // 关闭流
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 检查excel文件是否存在
     *
     * @return
     */
    public boolean fileIsExists() {

        boolean isExists = false;
        try {
            File directory = new File(Constant.IMPORT_METER_INFO_PATH);

            if(directory.exists()){
                File[] files = directory.listFiles();
                if(files.length == 0){

                }else {
                    isExists = true;
                }
            }

        } catch (Exception e) {
            LogUtils.i("e" + e.getMessage());
        }

        return isExists;
    }


    /**
     * 复制资源(assets目录下的文件)到指定路径
     * @param pathName      指定路径
     * @param imageName     图片文件名(文件名)
     */
    private void initSrc(String pathName, String imageName){

        File path = new File(pathName);
        if (!path.exists()) {                                      //  创建路径（文件夹）
            Log.d("TestFile", "Create the path:" + pathName);
            path.mkdir();

            try {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 在电脑上马上可以看到创建的文件
                Uri uri = Uri.fromFile(path);
                intent.setData(uri);
                SplashActivity.this.sendBroadcast(intent);

                Log.i("shen", "广播成功！！！");
            }catch (Exception e){
                Log.i("shen", "广播失败：" + e.getMessage());
            }
        }

        File file = new File(pathName, imageName);

        InputStream stream = null;
        FileOutputStream fos = null;

        //2,输入流读取assets目录下的文件
        try {
            // getAssets()拿到"资产目录"的文件夹（工程目录下的assets目录）
            // ***打开"dbName名字的文件"    （拿到他的输入流）
            stream = getAssets().open(imageName);

            //3,将读取的内容写入到指定文件夹的文件中去
            // ***拿到"file文件"的"输出流"
            fos = new FileOutputStream(file);

            //4,每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs))!=-1){	// 將"输入流"（stream）读到"bs"
                fos.write(bs, 0, temp);				// 將"bs"写到"fos"（输出流）
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(stream!=null && fos!=null){	// "流"非等于"null",说明没有关闭
                try {
                    // 关闭流
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
//                //用户同意
//                FilesUtils.createFile(this, Constant.srcPathDir);
//                FilesUtils.createFile(this, Constant.excelPathDir);
//                FilesUtils.createFile(this, Constant.IMPORT_METER_INFO_PATH);
//                FilesUtils.createFile(this, Constant.IMPORT_PHONE_PATH);
//                FilesUtils.createFile(this, Constant.CACHE_IMAGE_PATH);
//                FilesUtils.createFile(this, Constant.IMAGE_PATH);
//                FilesUtils.createFile(this, Constant.DIRECTIONSFORUSEIMAGE_PATH);
//
//                initSrc(Constant.CACHE_IMAGE_PATH,"no_preview_picture.png");
            } else {
                //用户不同意

            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)                 // 触摸事件
    {

//        if(event.getAction()==MotionEvent.ACTION_UP)
//        {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            type_finish();
//            if (runnable != null)                           // 如果这个(runnable 可执行事件)被new出来了.
//                handler.removeCallbacks(runnable);          // 就从"消息队列"删除(这个事件)
//        }

        return super.onTouchEvent(event);
    }

    /************************************************************************************/
    // 按钮监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {                 // 如果点击的是"返回按钮"

            //如果之前创建了Runnable对象,那么就把这任务移除
//            if(runnable!=null){
//                handler.removeCallbacks(runnable);
//            }

        }
        if(keyCode == KeyEvent.KEYCODE_HOME){

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //在此处理异常， arg1即为捕获到的异常
        Log.i("shen","+++++++++++++++++++++++++++++++++++++++++++++  Exception  ++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.i("shen","AAA:   " + ex);
        Log.i("shen","+++++++++++++++++++++++++++++++++++++++++++++  Exception  ++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
