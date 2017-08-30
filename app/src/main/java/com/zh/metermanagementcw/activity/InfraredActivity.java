//package com.zh.metermanagementcw.activity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//import com.rscja.deviceapi.Infrared;
//import com.rscja.deviceapi.exception.ConfigurationException;
//import com.rscja.ht.AppConfig;
//import com.rscja.ht.AppContext;
//import com.rscja.ht.BaseInitTask;
//import com.rscja.ht.R;
//import com.rscja.ht.UIHelper;
//import com.rscja.ht.utils.StringUtils;
//import com.rscja.utility.StringUtility;
//
//public class InfraredActivity extends BaseFragmentActivity {
//
//    private BaseInitTask mBaseInitTask;
//
//    @ViewInject(R.id.btnSend_ifr)
//    private Button btnSend_ifr;
//    @ViewInject(R.id.btnClear)
//    private Button btnClear;
//    @ViewInject(R.id.etData)
//    private EditText etData;
//    @ViewInject(R.id.cbContinuous)
//    private CheckBox cbContinuous;
//    @ViewInject(R.id.et_between)
//    private EditText etBetween;
//    @ViewInject(R.id.tvReceive)
//    private TextView tvReceive;
//    @ViewInject(R.id.svResult)
//    private ScrollView svResult;
//    @ViewInject(R.id.cbHex)
//    private CheckBox cbHex;
//
//    @ViewInject(R.id.cbIDPower)
//    private CheckBox cbIDPower;
//    @ViewInject(R.id.rb97)
//    private RadioButton rb97;
//    @ViewInject(R.id.rb07)
//    private RadioButton rb07;
//
//
//    @ViewInject(R.id.tv_succ_count)
//    private TextView tv_succ_count;
//    @ViewInject(R.id.tv_fail_count)
//    private TextView tv_fail_count;
//    @ViewInject(R.id.tv_elapsed_time)
//    private TextView tv_elapsed_time;
//
//
//    @ViewInject(R.id.spCheck)
//    private Spinner spCheck;
//
//    private Button btnInit;
//
//    private CheckBox cbLED;
//    private Button btnFree;
//
//    private boolean isOpen = false;
//
//    private boolean isIDPower = false;
//    private boolean is97 = true;
//
//    private boolean isHex = true;
//
//    AutoThread autoThread = null;
//
//    ReceiveThread mReceiveThread = null;
//
//    private Infrared mInfrared;
//
//
//    int sussCount = 0;
//    int failCount = 0;
//
//    private long lastClickTime;
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg != null) {
//
//
//                String strData = msg.obj.toString();
//
////                Log.i("InfraredActivity", "isIDPower=" + isIDPower + " msg.what=" + msg.what + " msg.obj=" + strData);
//
//                if (isIDPower) {
//                    if (msg.what == 2) {
//                        tvReceive.append(strData + "\n");//zp add
//                        if (strData.indexOf(":") > 0) {
//                            String[] arr = strData.split(":");
//                            if (arr != null && arr.length > 1) {
//                                tvReceive.append(getString(R.string.title_power_id)+":"+arr[0]+" "+getString(R.string.title_id_power)+":"+arr[1] + "\n");
//                            }
//                            else
//                            {
//                                tvReceive.append(strData + "\n");
//                            }
//                        }
//                        else {
//                            tvReceive.append(strData + "\n");
//                        }
//                    }
//
//                } else {
//                    tvReceive.append(strData + "\n");
//                }
//                scrollToBottom(svResult, tvReceive);
//
//                stat();
//            }
//
//        }
//    };
//
//
//    private void reset() {
//        sussCount = 0;
//        failCount = 0;
//
//        tv_succ_count.setText(String.valueOf(sussCount));
//        tv_fail_count.setText(String.valueOf(failCount));
//
//        tvReceive.setText("");
//
//        tv_elapsed_time.setText("");
//    }
//
//    private void stat() {
//
//        tv_succ_count.setText(String.valueOf(sussCount));
//        tv_fail_count.setText(String.valueOf(failCount));
//
//
//        long time = System.currentTimeMillis();
//        long timeD = time - lastClickTime;
//
//
//        tv_elapsed_time.setText(timeD + " ms");
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
////        mBaseInitTask.dismissDialog();
////
////        if (!mBaseInitTask.isCancelled()) {
////            mBaseInitTask.cancel(true);
////
////        }
////
////        if (autoThread != null) {
////            autoThread.stopThread();
////
////            autoThread = null;
////        }
////
////
////        if (mReceiveThread != null) {
////            mReceiveThread.stopThread();
////
////            mReceiveThread = null;
////        }
////
////        if (mBaseInitTask.getStatus() == AsyncTask.Status.FINISHED && mInfrared != null) {
////
////            mInfrared.close();
////        }
//
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mInfrared.closeLED();
//
//        btnFree_onClick(null, false);
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
////        mBaseInitTask = new BaseInitTask(this) {
////
////            @Override
////            protected Boolean doInBackground(String... params) {
////                boolean result = false;
////
////                if (mInfrared != null) {
////                    result = mInfrared.open();
////                }
////
////                return result;
////            }
////
////            @Override
////            protected void onPostExecute(Boolean result) {
////                super.onPostExecute(result);
////                mReceiveThread=new ReceiveThread();
////                mReceiveThread.start();
////
////            }
////        };
////        mBaseInitTask.execute();
//
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_infrared);
//        cbLED=(CheckBox)findViewById(R.id.cbLED);
//        if(!AppContext.getCustDeviceModel().equals("C70S")){
//            cbLED.setVisibility(View.GONE);
//        }
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        ViewUtils.inject(this);
//
//        try {
//            mInfrared = Infrared.getInstance();
//
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
//        cbLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    mInfrared.openLED();
//                }else{
//                    mInfrared.closeLED();
//                }
//            }
//        });
//
//        cbHex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//
//                isHex = isChecked;
//
//            }
//        });
//
//        rb97.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (rb97.isChecked()) {
//                    is97 = true;
//                    etData.setText("68AAAAAAAAAAAA68010243C3D516");
//                }
//            }
//        });
//
//
//        rb07.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (rb07.isChecked()) {
//                    is97 = false;
//
//                    etData.setText("68AAAAAAAAAAAA68110433333333AD16");
//                }
//            }
//        });
//
//
//        cbIDPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//
//                isIDPower = isChecked;
//
////                if (isIDPower) {
////                    if (mReceiveThread != null) {
////                        mReceiveThread.stopThread();
////
////                        mReceiveThread = null;
////                    }
////                } else {
////
////                    if (mReceiveThread == null || mReceiveThread.isStop()) {
////                        mReceiveThread = new ReceiveThread();
////                        mReceiveThread.start();
////                    }
////                }
//
//            }
//        });
//
//
//        btnInit = (Button) findViewById(R.id.btnInit_uart);
//        btnFree = (Button) findViewById(R.id.btnFree_uart);
//
//        btnInit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnInit_onClick(view);
//            }
//        });
//
//
//        btnFree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnFree_onClick(view, true);
//            }
//        });
//
//    }
//
//
//    protected void btnInit_onClick(View view) {
//
//        final int check = spCheck.getSelectedItemPosition();
//
//
//        mBaseInitTask = new BaseInitTask(this) {
//
//            @Override
//            protected Boolean doInBackground(String... params) {
//                boolean result = false;
//
//                if (mInfrared != null) {
//                    result = mInfrared.open(1200, check);
//                }
//
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(Boolean result) {
//                super.onPostExecute(result);
//
//                if (result && !isIDPower) {
//                    mReceiveThread = new ReceiveThread();
//                    mReceiveThread.start();
//                    UIHelper.ToastMessage(mContxt, R.string.msg_init_succ);
//
//
//                }
//
//                isOpen = result;
//
//                setBtnEnable(isOpen);
//
//            }
//        };
//        mBaseInitTask.execute();
//
//    }
//
//    private void setBtnEnable(boolean enable) {
//        btnFree.setEnabled(enable);
//        btnSend_ifr.setEnabled(enable);
//        btnInit.setEnabled(!enable);
//
//        spCheck.setEnabled(!enable);
//        cbIDPower.setEnabled(!enable);
//        rb97.setEnabled(!enable);
//        rb07.setEnabled(!enable);
//    }
//
//    protected void btnFree_onClick(View view, boolean show) {
//
//        if (isOpen) {
//            if (mInfrared.close()) {
//                isOpen = false;
//                if (show)
//                    UIHelper.ToastMessage(InfraredActivity.this, R.string.msg_free_succ);
//
//
//                if (autoThread != null) {
//                    autoThread.stopThread();
//
//                    autoThread = null;
//                }
//
//
//                if (mReceiveThread != null) {
//                    mReceiveThread.stopThread();
//
//                    mReceiveThread = null;
//                }
//
//            } else {
//                if (show)
//                    UIHelper.ToastMessage(InfraredActivity.this, R.string.msg_free_fail);
//
//            }
//
//        }
//
//
//        setBtnEnable(isOpen);
////        cbIDPower.setChecked(false);
////        isIDPower = false;
//    }
//
//
//    @OnClick(R.id.btnSend_ifr)
//    protected void btnSend_ifr_onClick(View view) {
//
//        String content = etData.getText().toString();
//
//        if (TextUtils.isEmpty(content)) {
//            return;
//        }
//
//
//        if (cbHex.isChecked()) {
//            if (!StringUtility.isHexNumberRex(content)) {
//                UIHelper.ToastMessage(InfraredActivity.this, R.string.desfire_msg_pwd_not_hex);
//
//                return;
//            }
//        }
//
//
//        if (mInfrared == null) {
//            return;
//        }
//
//
//        if (btnSend_ifr.getText().toString().equals(getString(R.string.send))) {
//
//            int sleep = StringUtils.toInt(etBetween.getText().toString(), 0);
//
//            boolean auto = cbContinuous.isChecked();
//
//            autoThread = new AutoThread(auto, content, sleep * 1000);
//            autoThread.start();
//
//            if (auto) {
//                btnSend_ifr.setText(R.string.btStop);
//                btnFree.setEnabled(false);
//            }
//
//        } else {
//
//
//            if (autoThread != null) {
//                autoThread.stopThread();
//
//                autoThread = null;
//            }
//            btnSend_ifr.setText(R.string.send);
//            btnFree.setEnabled(true);
//        }
//    }
//
//    @OnClick(R.id.btnClear)
//    protected void btnClear_onClick(View view) {
//
//
//        reset();
//
//    }
//
//    class AutoThread extends Thread {
//        String mContent;
//        int mSleep = 0;
//        boolean mAuto = false;
//        Message msg;
//        String idPower;
//        int tryCount=0;
//
//        public AutoThread(boolean auto, String content, int sleep) {
//            mContent = content;
//            mSleep = sleep;
//            mAuto = auto;
//        }
//
//        @Override
//        public void run() {
//            super.run();
//            byte[] sendBytes = null;
//            do {
//
//                lastClickTime = System.currentTimeMillis();
//
//                if (isIDPower) {
//
//
//                    while (tryCount<2)
//                    {
//                        if (is97) {
//                            idPower = mInfrared.getIDAndPowerWithWattmeter();
//                        } else {
//                            idPower = mInfrared.getIDAndPowerWithWattmeter(07);
//                        }
//
//                        if (!TextUtils.isEmpty(idPower))
//                        {
//                            break;
//                        }
//                        tryCount++;
//                    }
//                    tryCount=0;
//
//
//
//                    msg = new Message();
//                    msg.what = 2;
//
//
//                    if (TextUtils.isEmpty(idPower)) {
//                        msg.obj = "fail";
//                        failCount++;
//                    } else {
//                        msg.obj = idPower;
//                        sussCount++;
//                    }
//                    mHandler.sendMessage(msg);
//
//
//                } else {
//
//                    if (isHex) {
//                        sendBytes = StringUtility.hexString2Bytes(mContent);
//
//
//                    } else {
//                        sendBytes = mContent.getBytes();
//                    }
//
//                    mInfrared.send(sendBytes);
//                }
//
//                try {
//                    sleep(mSleep);
//                } catch (InterruptedException e) {
//
//                }
//            } while (!isInterrupted() && mAuto);
//
//        }
//
//        public void stopThread() {
//
//
//            mAuto = false;
//            Thread.currentThread().interrupt();
//        }
//
//    }
//
//
//    class ReceiveThread extends Thread {
//        byte[] data;
//        Message msg;
//        boolean threadStop = false;
//
//        public ReceiveThread() {
//            threadStop = false;
//        }
//
//
//        @Override
//        public void run() {
//
//            do {
//
//                data = mInfrared.receive();
//
//
//                if (data == null || data.length == 0) {
//
//                    continue;
//
//                } else {
//                    msg = new Message();
//                    msg.arg1 = 1;
//                    sussCount++;
//
//                    if (isHex) {
//                        msg.obj = StringUtility.bytes2HexString(data, data.length);
//                    } else {
//                        msg.obj = new String(data);
//                    }
//                    mHandler.sendMessage(msg);
//                }
//
//
//            } while (!threadStop && !isInterrupted());
//        }
//
//        public void stopThread() {
//            threadStop = true;
//            Thread.currentThread().interrupt();
//        }
//
//        public boolean isStop() {
//            return threadStop;
//        }
//
//
//    }
//
//
//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//
//            default:
//                break;
//        }
//
//        return super.onMenuItemSelected(featureId, item);
//    }
//
//}
