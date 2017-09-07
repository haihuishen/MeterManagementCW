package com.zh.metermanagementcw.xml;

import android.util.Xml;

import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.utils.LogUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shen on 10/26 0026.
 */
public class PullSerialNumberParser implements SerialNumberParser {

    @Override
    public void parser() throws Exception {
        List<String> serialNumberList = new ArrayList<>();
        String serialNumber = "";

        // for each map.keySet()，再调用get获取
        // for(Integer key : hashMap.keySet()){
        //      System.out.println(key);
        //      System.out.println(hashMap.get(key));
        //  }

        // a.在res/xml目录下（推荐使用）：
        // XmlResourceParser parser = AccountBookApplication.getContext().getResources().getXml(R.xml.type);
        // b.在res/xml、res/raw目录下：
        // InputStream inputStream = this.getResources().openRawResource(R.xml.XXX);
        // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // XmlPullParser parser = factory.newPullParser();
        // 或
        // XmlPullParser parser = Xml.newPullParser();                             //由android.util.Xml创建一个XmlPullParser实例
        // parser.setInput(is, "UTF-8");                                           //设置输入流 并指明编码方式

        File files = MyApplication.getContext().getFilesDir();
        File file = new File(files, "SerialNumber.xml");

        InputStream is;
        if(file.exists()){
            is = new FileInputStream(file);
        }else
            return ;

         XmlPullParser parser = Xml.newPullParser();                             //由android.util.Xml创建一个XmlPullParser实例
         parser.setInput(is, "UTF-8");                                           //设置输入流 并指明编码方式

        int eventType = parser.getEventType();                                      //获取第一个事件

        while (eventType != XmlPullParser.END_DOCUMENT) {                           //如果还不是结束文档事件,迭代每一个元素
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:                                  //开始文档事件
                    //serialNumber = "";
                    break;

                case XmlPullParser.START_TAG:                                       //开始元素事件
                    //LogUtils.i("START_TAG:" + parser.getName());
                    if (parser.getName().equals("Number")) {                      //parser.getName()得到当前指针所指向的节点的名称
                        serialNumber = parser.nextText();
                        //LogUtils.i("parser.nextText():" + serialNumber);
                        serialNumberList.add(serialNumber);

                    }
                    break;

                case XmlPullParser.END_TAG:                                         //结束元素事件
                    //LogUtils.i("END_TAG:" + parser.getName());
                    //if (parser.getName().equals("Number")) {
                    //    serialNumberList.add(serialNumber);
                    //}
                    break;
            }
            eventType = parser.next();                                              //进入下一个元素并触发相应事件
        }


        MyApplication.setSerialNumberList(serialNumberList);
    }


}