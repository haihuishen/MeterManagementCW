package com.zh.metermanagementcw.activity;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bm.library.Info;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.MyPicAdapter;
import com.zh.metermanagementcw.adapter.PicAdapter;
import com.zh.metermanagementcw.adapter.PicAdapter1;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */

public class DirectionsForUseActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 使用手册 -- 列表 */
    ListView mLvDirectionsForUse;

    /****************************popwindows--选择********************************/
    PopupWindow mPop;

    /** tv_1_1*/
    TextView mTv_1_1;
    /** tv_2_9*/
    TextView mTv_2_9;
    /** tv_3_13*/
    TextView mTv_3_13;
    /** tv_4_18*/
    TextView mTv_4_18;
    /** tv_5_23*/
    TextView mTv_5_23;
    /** tv_6_25*/
    TextView mTv_6_25;
    /** tv_7_33*/
    TextView mTv_7_33;
    /** tv_8_39*/
    TextView mTv_8_39;

    /** tv_9_42*/
    TextView mTv_9_42;
    /** tv_10_49*/
    TextView mTv_10_49;
    /** tv_11_56*/
    TextView mTv_11_56;
    /** tv_12_58*/
    TextView mTv_12_58;



    @Override
    public int getContentLayout() {
        return R.layout.activity_directions_for_use;
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
        mBtnMenu.setVisibility(View.VISIBLE);

        mTvTitle.setText("使用手册");
    }

    @Override
    public void initView() {
        mLvDirectionsForUse = (ListView) findViewById(R.id.lv_directionsForUse);


    }

    @Override
    public void initListener() {


    }

    @Override
    public void initData() {

        List<String> pathList = new ArrayList<>();

        for(int i=1; i<=58; i++){
            pathList.add(Constant.DIRECTIONSFORUSEIMAGE_PATH + i +".png");
//            File file = new File(Constant.DIRECTIONSFORUSEIMAGE_PATH + i +".png");
//            if(file.exists()){
//                LogUtils.i("文件存在：" + Constant.DIRECTIONSFORUSEIMAGE_PATH + i +".png");
//            }else {
//                LogUtils.i("文件不存在：" + Constant.DIRECTIONSFORUSEIMAGE_PATH + i +".png");
//            }
        }

        LogUtils.i("pathList：" + pathList.size());

        MyPicAdapter picAdapter = new MyPicAdapter(this, pathList);
        mLvDirectionsForUse.setAdapter(picAdapter);

        initialPopup();
    }

    /**
     * 初始化popupwindow
     */
    private void initialPopup() {

        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.popupwindow_directions_for_use, null);

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow对象
        mPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPop.setOnDismissListener(this);

        //mPopCbToggleLabels = (CheckBox) view.findViewById(R.id.cb_toggleLabels);
        //mPopCbToggleFilled = (CheckBox) view.findViewById(R.id.cb_toggleFilled);


        // 需要顺利让PopUpWindow dimiss（即点击PopuWindow之外的地方此或者back键PopuWindow会消失）；
        // PopUpWindow的背景不能为空。必须在popuWindow.showAsDropDown(v);
        // 或者其它的显示PopuWindow方法之前设置它的背景不为空：

        // 需要设置一下此参数，点击外边可消失
        mPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        mPop.setOutsideTouchable(true);                // 不设置就没有
        // 设置此参数获得焦点，否则无法点击
        mPop.setFocusable(true);                       // 不设置就没有




        mTv_1_1 = (TextView) view.findViewById(R.id.tv_1_1);
        mTv_2_9 = (TextView) view.findViewById(R.id.tv_2_9);
        mTv_3_13 = (TextView) view.findViewById(R.id.tv_3_13);
        mTv_4_18 = (TextView) view.findViewById(R.id.tv_4_18);
        mTv_5_23 = (TextView) view.findViewById(R.id.tv_5_23);
        mTv_6_25 = (TextView) view.findViewById(R.id.tv_6_25);
        mTv_7_33 = (TextView) view.findViewById(R.id.tv_7_33);
        mTv_8_39 = (TextView) view.findViewById(R.id.tv_8_39);

        mTv_9_42 = (TextView) view.findViewById(R.id.tv_9_42);
        mTv_10_49 = (TextView) view.findViewById(R.id.tv_10_49);
        mTv_11_56 = (TextView) view.findViewById(R.id.tv_11_56);
        mTv_12_58 = (TextView) view.findViewById(R.id.tv_12_58);


        mTv_1_1.setOnClickListener(this);
        mTv_2_9.setOnClickListener(this);
        mTv_3_13.setOnClickListener(this);
        mTv_4_18.setOnClickListener(this);
        mTv_5_23.setOnClickListener(this);
        mTv_6_25.setOnClickListener(this);
        mTv_7_33.setOnClickListener(this);
        mTv_8_39.setOnClickListener(this);

        mTv_9_42.setOnClickListener(this);
        mTv_10_49.setOnClickListener(this);
        mTv_11_56.setOnClickListener(this);
        mTv_12_58.setOnClickListener(this);
    }


    /**
     * 显示popupwindow
     */
    private void showPopupWindow() {

        if (mPop.isShowing()) {
            // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
            mPop.dismiss();
        } else {

            // 显示窗口
            // pop.showAsDropDown(v);
            // 获取屏幕和PopupWindow的width和height
            mPop.setAnimationStyle(R.style.MenuAnimationFade);           // 动画怎么设置，怎会动!
            // 设置了，就铺满窗口
            //mPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

            // pop.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            // 设置显示PopupWindow的位置位于View的左下方，x,y表示坐标偏移量
            mPop.showAsDropDown(mBtnMenu, 0, 20);           // 绑定哪个控件来"控制"控件弹出  ???

            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:
                finish();
                break;

            case R.id.btn_menu_right:

                showPopupWindow();
                break;


            case R.id.tv_1_1:
                mLvDirectionsForUse.setSelection(0);
                mPop.dismiss();
                break;
            case R.id.tv_2_9:
                mLvDirectionsForUse.setSelection(8);
                mPop.dismiss();
                break;
            case R.id.tv_3_13:
                mLvDirectionsForUse.setSelection(12);
                mPop.dismiss();
                break;
            case R.id.tv_4_18:
                mLvDirectionsForUse.setSelection(17);
                mPop.dismiss();
                break;
            case R.id.tv_5_23:
                mLvDirectionsForUse.setSelection(22);
                mPop.dismiss();
                break;
            case R.id.tv_6_25:
                mLvDirectionsForUse.setSelection(24);
                mPop.dismiss();
                break;
            case R.id.tv_7_33:
                mLvDirectionsForUse.setSelection(32);
                mPop.dismiss();
                break;
            case R.id.tv_8_39:
                mLvDirectionsForUse.setSelection(38);
                mPop.dismiss();
                break;

            case R.id.tv_9_42:
                mLvDirectionsForUse.setSelection(41);
                mPop.dismiss();
                break;
            case R.id.tv_10_49:
                mLvDirectionsForUse.setSelection(48);
                mPop.dismiss();
                break;
            case R.id.tv_11_56:
                mLvDirectionsForUse.setSelection(55);
                mPop.dismiss();
                break;
            case R.id.tv_12_58:
                mLvDirectionsForUse.setSelection(57);
                mPop.dismiss();
                break;
        }
    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1f;
        getWindow().setAttributes(params);
    }
}
