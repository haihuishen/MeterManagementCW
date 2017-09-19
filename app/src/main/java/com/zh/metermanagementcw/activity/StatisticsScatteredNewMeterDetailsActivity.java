package com.zh.metermanagementcw.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.adapter.ScatteredNewMeterAdapter;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.ImageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class StatisticsScatteredNewMeterDetailsActivity extends BaseActivity implements View.OnClickListener {

    /** 标题 */
    TextView mTvTitle;
    /** 返回按钮 -- 按钮 */
    Button mBtnBack;
    /** 菜单按钮 -- 按钮 */
    Button mBtnMenu;

    ListView mListView;

    List<ScatteredNewMeterBean> mScatteredNewMeterBeanList = new ArrayList<>();

    ScatteredNewMeterAdapter mScatteredNewMeterAdapter;

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
        return R.layout.activity_statistics_scattered_new_meter_details;
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

        mTvTitle.setText("详情");
    }


    @Override
    public void initView() {
        mListView = (ListView) findViewById(R.id.lv_scatteredNewMeter);

        //--------------------------------------------------
        mLlayoutParent = findViewById(R.id.parent);
        mIvBg = findViewById(R.id.iv_bg);
        mPvBgImg = (PhotoView) findViewById(R.id.pv_bg);
    }

    @Override
    public void initListener() {


        mPvBgImg.setOnClickListener(this);
    }

    @Override
    public void initData() {

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

        //------------------------------------------------------------------------------------------

        mTvTitle.setText("总户数");

        mScatteredNewMeterBeanList = MyApplication.getScatteredNewMeterBeanList();
        mScatteredNewMeterAdapter = new ScatteredNewMeterAdapter(getContext(), (ArrayList)mScatteredNewMeterBeanList,
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
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_back_left:

                finish();
                break;

            case R.id.btn_menu_right:
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
