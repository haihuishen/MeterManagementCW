package com.zh.metermanagementcw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.zh.metermanagementcw.R;
import com.zh.metermanagementcw.utils.ImageFactory;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加"附件"界面的 -- "图片"、"视频"、"文档" -- 对应的"适配器"
 *
 *
 *  RecyclerView 再生控件 -- 相当于 自定义ListView
 */
public class MyPicAdapter extends BaseAdapter {

    private List<String> mPathList = new ArrayList<>();
    private Context mContext;

    public MyPicAdapter(Context context, List<String> pathList){

        mContext = context;
        mPathList = pathList;
    }

    @Override
    public int getCount() {
        return mPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;
        if(convertView == null){                // 拿缓存
            // 将 layout 填充成"View"
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.item_lv_pic,parent,false);      // listview中每一项的布局

            viewHold = new ViewHold(convertView);

            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.ivPic.setImageBitmap(ImageFactory.getBitmap(mPathList.get(position)));

        return convertView;
    }


    class ViewHold{

        public ImageView ivPic;


        public ViewHold(View view) {

            ivPic = (ImageView) view.findViewById(R.id.iv_lv_time);

        }


    }
}
