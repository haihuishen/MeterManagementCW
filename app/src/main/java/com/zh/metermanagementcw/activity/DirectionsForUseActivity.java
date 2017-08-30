package com.zh.metermanagementcw.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class DirectionsForUseActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    /** 使用手册 -- 列表 */
    ListView mLvDirectionsForUse;


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
        mBtnMenu.setVisibility(View.GONE);

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

        for(int i=1; i<9; i++){
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

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
                break;

        }
    }

}
