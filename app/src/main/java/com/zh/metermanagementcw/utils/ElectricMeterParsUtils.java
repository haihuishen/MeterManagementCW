package com.zh.metermanagementcw.utils;

import android.serialport.Tools;
import android.util.Log;

import com.zh.metermanagementcw.config.MeterAgreement;

/**
 * 电表协议解析 ———— 97、07
 *
 */
public class ElectricMeterParsUtils {

    public static enum METERTYPE{
        _97,
        _07
    }

    /**
     * 根据"数据标识"截取数据
     *
     * @param bytes
     * @return
     */
    public static String[] getMsg(byte[] bytes) {


//        LogUtils.i("shen:" + new String(bytes));
//        LogUtils.i("shen123:" + Tools.bytesToHexString(bytes));

        /** 地址*/
        byte[] mByteDz = new byte[6];
        /**  得到的地址 -- 已经将 bytes转码成 字符串(16进制数字)*/
        String mDz = "";

        /** 控制码*/
        byte[] mBytesDataID;
        /** 得到的控制码 -- 已经将 bytes转码成 字符串(16进制数字)*/
        String mDataID = "";
        /** 接收数据帧*/
        String buf = "";

        try {
            byte[] recvBuffer = bytes;
            int x = 0;
            int i = 0;

            // 2014/7/17判断接收帧中 第一个 68 的位置
            for (i = 0; i < recvBuffer.length; i++) {

                //region 定位"0x68"
                if (recvBuffer[i] == (byte) 0x68) {				// (前面的FE去掉)0x68
                    x = i;										// 拿到"0x68"在"byte[]"中的位置

                    //region 判断控制码
                    // --68 + 6字节地址 + 68 + 1字节控制码-------
                    if ((x + 8) < recvBuffer.length) {

                        // 判断控制码
                        // 97协议为 0x81
                        // 07协议为 0x91
                        if (recvBuffer[x + 8] == (byte) 0x81 || recvBuffer[x + 8] == (byte) 0x91) {
                            Log.i("info", "接收到了 电表数据！");
                            LogUtils.i("shen123:" + recvBuffer.toString());

                            // 数据域长度：有数据时长度为 2+4；无数据时长度为：2
                            // 97协议： 2 + 4 = 6
                            // 07协议： 4 + 4 = 8

                            // --68 + 6字节地址 + 68 + 1字节控制码 + 1字节数据域长度--------
                            if ((x + 9) < recvBuffer.length) {
                                int dataL = recvBuffer[x + 9];		// 数据域长度

                                // --68 + 6字节地址 + 68 + 1字节控制码 + 1字节数据域长度 + n字节数据域--------
                                if (x + 11 + dataL < recvBuffer.length) {

                                    // --68H + 6字节地址 + 68H + 1字节控制码 + 1字节数据域长度 + n字节数据域 + 1字节校验 + 16H--------
                                    if (recvBuffer[x + 11 + dataL] != (byte) 0x16) {
                                        Log.i("info", "---5----错误：没有接收到数据帧！");
                                        continue;
                                    }

                                    // 数据标识的长度  -- 97:2字节   07:4字节
                                    int iData = 0;
                                    if (recvBuffer[x + 8] == (byte) 0x81) {
                                        iData = 2;
                                    } else if (recvBuffer[x + 8] == (byte) 0x91) {
                                        iData = 4;
                                    } else {
                                        Log.i("info", "---6----错误：没有接收到数据帧！");
                                        continue;
                                    }

                                    // 保存"数据域"中对电表数据 -- "数据域" = "数据标识" + "对应的数据"
                                    byte[] dClearData = new byte[dataL - iData];
                                    mBytesDataID = new byte[iData];
                                    Log.i("info", "对比数组长度 == " + dClearData.length);

                                    // 数据解析
                                    int mm = 0;

                                    for (mm = x + 1, i = 0; mm <= x + 6; mm++, i++){
                                        mByteDz[i] = recvBuffer[mm];
                                    }
                                    for (int j = mByteDz.length - 1; j >= 0 ; j--){
                                        mDz  += Tools.byteToString(mByteDz[j]);
                                    }


                                    for (mm = x + 10, i = 0; mm <= x + 9 + iData; mm++, i++){
                                        mBytesDataID[i] = (byte) (recvBuffer[mm] - 0x33);
                                    }
                                    for (int j = mBytesDataID.length - 1; j >= 0 ; j--){
                                        mDataID  += Tools.byteToString(mBytesDataID[j]);
                                    }


                                    // 数据域中的数据(电表返回数据会将其 + 0x33)
                                    for (mm = x + 10 + iData, i = 0; mm <= x + 9 + dataL; mm++, i++) {
                                        dClearData[i] = (byte) (recvBuffer[mm] - 0x33);
                                    }

                                    for (int j = dClearData.length - 1; j >= 0; j--) {
                                        // 将字节以16进制的形式弄成字符串 -- 然后接在一起
                                        buf += Tools.byteToString(dClearData[j]);
                                    }

                                    // 使用StringBuilder包装下,方便插入！
                                    StringBuilder  sbBuf = new StringBuilder (buf);

                                    if(mDataID.length() == 4){              // 97
                                        switch(Integer.valueOf(mDataID, 16)) {

                                            case 0x9010:sbBuf = sbBuf.insert(6,".");break;//"正向有功电度"
                                            case 0x9110:sbBuf = sbBuf.insert(6,".");break;//"正向无功电度"
                                            case 0x9020:sbBuf = sbBuf.insert(6,".");break;//"反向有功电度"
                                            case 0x9120:sbBuf = sbBuf.insert(6,".");break;//"反向无功电度"
                                            case 0xB611:sbBuf = sbBuf.delete(0,1)  ;break;//"A相电压
                                            case 0xB612:sbBuf = sbBuf.delete(0,1)  ;break;//"B相电压
                                            case 0xB613:sbBuf = sbBuf.delete(0,1)  ;break;//"C相电压
                                            case 0xB621:sbBuf = sbBuf.insert(2,".");break;//"A相电流
                                            case 0xB622:sbBuf = sbBuf.insert(2,".");break;//"B相电流
                                            case 0xB623:sbBuf = sbBuf.insert(2,".");break;//"C相电流
                                            case 0xB630:sbBuf = sbBuf.insert(2,".");break;//"有功功率"
                                            case 0xB631:sbBuf = sbBuf.insert(2,".");break;//"A相有功功率
                                            case 0xB632:sbBuf = sbBuf.insert(2,".");break;//"B相有功功率
                                            case 0xB633:sbBuf = sbBuf.insert(2,".");break;//"C相有功功率
                                            case 0xB640:sbBuf = sbBuf.insert(2,".");break;//"无功功率"
                                            case 0xB641:sbBuf = sbBuf.insert(2,".");break;//"A相无功功率
                                            case 0xB642:sbBuf = sbBuf.insert(2,".");break;//"B相无功功率
                                            case 0xB643:sbBuf = sbBuf.insert(2,".");break;//"C相无功功率
                                            case 0xB650:sbBuf = sbBuf.insert(1,".");break;//"总功率因数
                                            case 0xB651:sbBuf = sbBuf.insert(1,".");break;//"A相功率因数
                                            case 0xB652:sbBuf = sbBuf.insert(1,".");break;//"B相功率因数
                                            case 0xB653:sbBuf = sbBuf.insert(1,".");break;//"C相功率因数

                                            //-------------------------------------------
                                            case 0x9410:sbBuf = sbBuf.insert(6,".");break;//"上月正向有功总"
                                            case 0x9810:sbBuf = sbBuf.insert(6,".");break;//"上上月正向有功总"
                                            case 0x9A10:sbBuf = sbBuf.insert(6,".");break;//"上上上月正向有功总"

                                            case 0x9090:sbBuf = sbBuf.insert(6,".");break;//"（上1次）日冻结正向有功总电能"
                                            case 0x9091:sbBuf = sbBuf.insert(6,".");break;//"（上2次）日冻结正向有功总电能"
                                            case 0x9092:sbBuf = sbBuf.insert(6,".");break;//"（上3次）日冻结正向有功总电能"
                                            case 0x9093:sbBuf = sbBuf.insert(6,".");break;//"（上4次）日冻结正向有功总电能"
                                            case 0x9094:sbBuf = sbBuf.insert(6,".");break;//"（上5次）日冻结正向有功总电能"
                                            case 0x9095:sbBuf = sbBuf.insert(6,".");break;//"（上6次）日冻结正向有功总电能"
                                            case 0x9096:sbBuf = sbBuf.insert(6,".");break;//"（上7次）日冻结正向有功总电能"
                                            case 0x9097:sbBuf = sbBuf.insert(6,".");break;//"（上8次）日冻结正向有功总电能"
                                            case 0x9098:sbBuf = sbBuf.insert(6,".");break;//"（上9次）日冻结正向有功总电能"
                                            case 0x9099:sbBuf = sbBuf.insert(6,".");break;//"（上10次）日冻结正向有功总电能"
                                            case 0x909A:sbBuf = sbBuf.insert(6,".");break;//"（上11次）日冻结正向有功总电能"
                                            case 0x909B:sbBuf = sbBuf.insert(6,".");break;//"（上12次）日冻结正向有功总电能"
                                            case 0x909C:sbBuf = sbBuf.insert(6,".");break;//"（上13次）日冻结正向有功总电能"
                                            case 0x909D:sbBuf = sbBuf.insert(6,".");break;//"（上14次）日冻结正向有功总电能"
                                            case 0x909E:sbBuf = sbBuf.insert(6,".");break;//"（上15次）日冻结正向有功总电能"
                                            case 0x909F:sbBuf = sbBuf.insert(6,".");break;//"（上16次）日冻结正向有功总电能"

                                            case 0xC010:                                    //" 日期及星期 -- YYMMDDWW"
                                                sbBuf = sbBuf.delete(6,8)  ;
                                                sbBuf = sbBuf.insert(2,"-");
                                                sbBuf = sbBuf.insert(5,"-");
                                                sbBuf = sbBuf.insert(0,"20");
                                                break;

                                            case 0xC011:                                    //" 时间 -- hhmmss"
                                                sbBuf = sbBuf.insert(2,"-");
                                                sbBuf = sbBuf.insert(5,"-");
                                                break;

                                            default:sbBuf = new StringBuilder ("暂不支持此项标识");break;
                                        }

                                        buf = sbBuf.toString();
                                        return new String[]{mDz, mDataID , buf};

                                    } else if(mDataID.length() == 8){        // 07

                                        switch(Integer.valueOf(mDataID, 16)) {
                                            case 0x00000000:sbBuf = sbBuf.insert(6,".");break;//有功总电度
                                            case 0x00010000:sbBuf = sbBuf.insert(6,".");break;//正向有功电度
                                            case 0x00020000:sbBuf = sbBuf.insert(6,".");break;//反向有功电度
                                            case 0x02010100:sbBuf = sbBuf.insert(3,".");break;//A相电压
                                            case 0x02010200:sbBuf = sbBuf.insert(3,".");break;//B相电压
                                            case 0x02010300:sbBuf = sbBuf.insert(3,".");break;//C相电压
                                            case 0x02020100:sbBuf = sbBuf.insert(3,".");break;//A相电流
                                            case 0x02020200:sbBuf = sbBuf.insert(3,".");break;//B相电流
                                            case 0x02020300:sbBuf = sbBuf.insert(3,".");break;//C相电流
                                            case 0x02030000:sbBuf = sbBuf.insert(2,".");break;//有功功率
                                            case 0x02030100:sbBuf = sbBuf.insert(2,".");break;//A相有功功率
                                            case 0x02030200:sbBuf = sbBuf.insert(2,".");break;//B相有功功率
                                            case 0x02030300:sbBuf = sbBuf.insert(2,".");break;//C相有功功率
                                            case 0x02040000:sbBuf = sbBuf.insert(2,".");break;//无功功率
                                            case 0x02040100:sbBuf = sbBuf.insert(2,".");break;//A相无功功率
                                            case 0x02040200:sbBuf = sbBuf.insert(2,".");break;//B相无功功率
                                            case 0x02040300:sbBuf = sbBuf.insert(2,".");break;//C相无功功率
                                            case 0x02060000:sbBuf = sbBuf.insert(1,".");break;//总功率因数
                                            case 0x02060100:sbBuf = sbBuf.insert(1,".");break;//A相功率因数
                                            case 0x02060200:sbBuf = sbBuf.insert(1,".");break;//B相功率因数
                                            case 0x02060300:sbBuf = sbBuf.insert(1,".");break;//C相功率因数
                                            case 0x02800002:sbBuf = sbBuf.insert(2,".");break;//频率


                                            //-------------------------------------------
                                            case 0x00000001:sbBuf = sbBuf.insert(6,".");break;//"(上1结算月)有功总"
                                            case 0x00000002:sbBuf = sbBuf.insert(6,".");break;//"(上2结算月)有功总"
                                            case 0x00000003:sbBuf = sbBuf.insert(6,".");break;//"(上3结算月)有功总"
                                            case 0x00000004:sbBuf = sbBuf.insert(6,".");break;//"(上4结算月)有功总"
                                            case 0x00000005:sbBuf = sbBuf.insert(6,".");break;//"(上5结算月)有功总"
                                            case 0x00000006:sbBuf = sbBuf.insert(6,".");break;//"(上6结算月)有功总"
                                            case 0x00000007:sbBuf = sbBuf.insert(6,".");break;//"(上7结算月)有功总"
                                            case 0x00000008:sbBuf = sbBuf.insert(6,".");break;//"(上8结算月)有功总"
                                            case 0x00000009:sbBuf = sbBuf.insert(6,".");break;//"(上9结算月)有功总"
                                            case 0x0000000A:sbBuf = sbBuf.insert(6,".");break;//"(上10结算月)有功总"
                                            case 0x0000000B:sbBuf = sbBuf.insert(6,".");break;//"(上11结算月)有功总"
                                            case 0x0000000C:sbBuf = sbBuf.insert(6,".");break;//"(上12结算月)有功总"

                                            case 0x00010001:sbBuf = sbBuf.insert(6,".");break;//"(上1结算月)正向有功总"
                                            case 0x00010002:sbBuf = sbBuf.insert(6,".");break;//"(上2结算月)正向有功总"
                                            case 0x00010003:sbBuf = sbBuf.insert(6,".");break;//"(上3结算月)正向有功总"
                                            case 0x00010004:sbBuf = sbBuf.insert(6,".");break;//"(上4结算月)正向有功总"
                                            case 0x00010005:sbBuf = sbBuf.insert(6,".");break;//"(上5结算月)正向有功总"
                                            case 0x00010006:sbBuf = sbBuf.insert(6,".");break;//"(上6结算月)正向有功总"
                                            case 0x00010007:sbBuf = sbBuf.insert(6,".");break;//"(上7结算月)正向有功总"
                                            case 0x00010008:sbBuf = sbBuf.insert(6,".");break;//"(上8结算月)正向有功总"
                                            case 0x00010009:sbBuf = sbBuf.insert(6,".");break;//"(上9结算月)正向有功总"
                                            case 0x0001000A:sbBuf = sbBuf.insert(6,".");break;//"(上10结算月)正向有功总"
                                            case 0x0001000B:sbBuf = sbBuf.insert(6,".");break;//"(上11结算月)正向有功总"
                                            case 0x0001000C:sbBuf = sbBuf.insert(6,".");break;//"(上12结算月)正向有功总"
                                            //-------------------------------------------
                                            case 0x05060101:sbBuf = sbBuf.insert(6,".");break;//"（上1次）日冻结正向有功总电能"
                                            case 0x05060102:sbBuf = sbBuf.insert(6,".");break;//"（上2次）日冻结正向有功总电能"
                                            case 0x05060103:sbBuf = sbBuf.insert(6,".");break;//"（上3次）日冻结正向有功总电能"
                                            case 0x05060104:sbBuf = sbBuf.insert(6,".");break;//"（上4次）日冻结正向有功总电能"
                                            case 0x05060105:sbBuf = sbBuf.insert(6,".");break;//"（上5次）日冻结正向有功总电能"
                                            case 0x05060106:sbBuf = sbBuf.insert(6,".");break;//"（上6次）日冻结正向有功总电能"
                                            case 0x05060107:sbBuf = sbBuf.insert(6,".");break;//"（上7次）日冻结正向有功总电能"
                                            case 0x05060108:sbBuf = sbBuf.insert(6,".");break;//"（上8次）日冻结正向有功总电能"
                                            case 0x05060109:sbBuf = sbBuf.insert(6,".");break;//"（上9次）日冻结正向有功总电能"
                                            case 0x0506010A:sbBuf = sbBuf.insert(6,".");break;//"（上10次）日冻结正向有功总电能"
                                            case 0x0506010B:sbBuf = sbBuf.insert(6,".");break;//"（上11次）日冻结正向有功总电能"
                                            case 0x0506010C:sbBuf = sbBuf.insert(6,".");break;//"（上12次）日冻结正向有功总电能"
                                            case 0x0506010D:sbBuf = sbBuf.insert(6,".");break;//"（上13次）日冻结正向有功总电能"
                                            case 0x0506010E:sbBuf = sbBuf.insert(6,".");break;//"（上14次）日冻结正向有功总电能"
                                            case 0x0506010F:sbBuf = sbBuf.insert(6,".");break;//"（上15次）日冻结正向有功总电能"
                                            case 0x05060110:sbBuf = sbBuf.insert(6,".");break;//"（上16次）日冻结正向有功总电能"
                                            case 0x05060111:sbBuf = sbBuf.insert(6,".");break;//"（上17次）日冻结正向有功总电能"
                                            case 0x05060112:sbBuf = sbBuf.insert(6,".");break;//"（上18次）日冻结正向有功总电能"
                                            case 0x05060113:sbBuf = sbBuf.insert(6,".");break;//"（上19次）日冻结正向有功总电能"
                                            case 0x05060114:sbBuf = sbBuf.insert(6,".");break;//"（上20次）日冻结正向有功总电能"
                                            case 0x05060115:sbBuf = sbBuf.insert(6,".");break;//"（上21次）日冻结正向有功总电能"
                                            case 0x05060116:sbBuf = sbBuf.insert(6,".");break;//"（上22次）日冻结正向有功总电能"
                                            case 0x05060117:sbBuf = sbBuf.insert(6,".");break;//"（上23次）日冻结正向有功总电能"
                                            case 0x05060118:sbBuf = sbBuf.insert(6,".");break;//"（上24次）日冻结正向有功总电能"
                                            case 0x05060119:sbBuf = sbBuf.insert(6,".");break;//"（上25次）日冻结正向有功总电能"
                                            case 0x0506011A:sbBuf = sbBuf.insert(6,".");break;//"（上26次）日冻结正向有功总电能"
                                            case 0x0506011B:sbBuf = sbBuf.insert(6,".");break;//"（上27次）日冻结正向有功总电能"
                                            case 0x0506011C:sbBuf = sbBuf.insert(6,".");break;//"（上28次）日冻结正向有功总电能"
                                            case 0x0506011D:sbBuf = sbBuf.insert(6,".");break;//"（上29次）日冻结正向有功总电能"
                                            case 0x0506011E:sbBuf = sbBuf.insert(6,".");break;//"（上30次）日冻结正向有功总电能"
                                            case 0x0506011F:sbBuf = sbBuf.insert(6,".");break;//"（上31次）日冻结正向有功总电能"

                                            case 0x05060120:sbBuf = sbBuf.insert(6,".");break;//"（上32次）日冻结正向有功总电能"
                                            case 0x05060121:sbBuf = sbBuf.insert(6,".");break;//"（上33次）日冻结正向有功总电能"
                                            case 0x05060122:sbBuf = sbBuf.insert(6,".");break;//"（上34次）日冻结正向有功总电能"
                                            case 0x05060123:sbBuf = sbBuf.insert(6,".");break;//"（上35次）日冻结正向有功总电能"
                                            case 0x05060124:sbBuf = sbBuf.insert(6,".");break;//"（上36次）日冻结正向有功总电能"
                                            case 0x05060125:sbBuf = sbBuf.insert(6,".");break;//"（上37次）日冻结正向有功总电能"
                                            case 0x05060126:sbBuf = sbBuf.insert(6,".");break;//"（上38次）日冻结正向有功总电能"
                                            case 0x05060127:sbBuf = sbBuf.insert(6,".");break;//"（上39次）日冻结正向有功总电能"
                                            case 0x05060128:sbBuf = sbBuf.insert(6,".");break;//"（上40次）日冻结正向有功总电能"
                                            case 0x05060129:sbBuf = sbBuf.insert(6,".");break;//"（上41次）日冻结正向有功总电能"
                                            case 0x0506012A:sbBuf = sbBuf.insert(6,".");break;//"（上42次）日冻结正向有功总电能"
                                            case 0x0506012B:sbBuf = sbBuf.insert(6,".");break;//"（上43次）日冻结正向有功总电能"
                                            case 0x0506012C:sbBuf = sbBuf.insert(6,".");break;//"（上44次）日冻结正向有功总电能"
                                            case 0x0506012D:sbBuf = sbBuf.insert(6,".");break;//"（上45次）日冻结正向有功总电能"
                                            case 0x0506012E:sbBuf = sbBuf.insert(6,".");break;//"（上46次）日冻结正向有功总电能"
                                            case 0x0506012F:sbBuf = sbBuf.insert(6,".");break;//"（上47次）日冻结正向有功总电能"
                                            case 0x05060130:sbBuf = sbBuf.insert(6,".");break;//"（上48次）日冻结正向有功总电能"
                                            case 0x05060131:sbBuf = sbBuf.insert(6,".");break;//"（上49次）日冻结正向有功总电能"
                                            case 0x05060132:sbBuf = sbBuf.insert(6,".");break;//"（上50次）日冻结正向有功总电能"
                                            case 0x05060133:sbBuf = sbBuf.insert(6,".");break;//"（上51次）日冻结正向有功总电能"
                                            case 0x05060134:sbBuf = sbBuf.insert(6,".");break;//"（上52次）日冻结正向有功总电能"
                                            case 0x05060135:sbBuf = sbBuf.insert(6,".");break;//"（上53次）日冻结正向有功总电能"
                                            case 0x05060136:sbBuf = sbBuf.insert(6,".");break;//"（上54次）日冻结正向有功总电能"
                                            case 0x05060137:sbBuf = sbBuf.insert(6,".");break;//"（上55次）日冻结正向有功总电能"
                                            case 0x05060138:sbBuf = sbBuf.insert(6,".");break;//"（上56次）日冻结正向有功总电能"
                                            case 0x05060139:sbBuf = sbBuf.insert(6,".");break;//"（上57次）日冻结正向有功总电能"
                                            case 0x0506013A:sbBuf = sbBuf.insert(6,".");break;//"（上58次）日冻结正向有功总电能"
                                            case 0x0506013B:sbBuf = sbBuf.insert(6,".");break;//"（上59次）日冻结正向有功总电能"
                                            case 0x0506013C:sbBuf = sbBuf.insert(6,".");break;//"（上60次）日冻结正向有功总电能"
                                            case 0x0506013D:sbBuf = sbBuf.insert(6,".");break;//"（上61次）日冻结正向有功总电能"
                                            case 0x0506014E:sbBuf = sbBuf.insert(6,".");break;//"（上62次）日冻结正向有功总电能"



                                            case 0x04000101:                                    //" 日期及星期 -- YYMMDDWW"
                                                sbBuf = sbBuf.delete(6,8)  ;
                                                sbBuf = sbBuf.insert(2,"-");
                                                sbBuf = sbBuf.insert(5,"-");
                                                sbBuf = sbBuf.insert(0,"20");
                                                break;

                                            case 0x04000102:                                    //" 时间 -- hhmmss"
                                                sbBuf = sbBuf.insert(2,"-");
                                                sbBuf = sbBuf.insert(5,"-");
                                                break;

                                            default:sbBuf = new StringBuilder ("暂不支持此项标识");break;
                                        }

                                        buf = sbBuf.toString();
                                        return new String[]{mDz, mDataID, buf};
                                    }

                                    Log.i("info", "buf ---- " + buf);
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        // TODO 自动生成的 catch 块
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                continue;
                            }
                        } else {
                            Log.i("info", "---3----错误：没有接收到数据帧！");
                            continue;
                        }
                    } else {
                        continue;
                    }
                    //endregion 判断控制码
                }
                //endregion 定位"0x68"
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new String[]{mDz, mDataID, buf.toString()};
    }




    /**
     * 根据"表地址"和"数据标识"生成"命令"
     *
     * @param bdz		表地址
     * @param str		数据标识
     * @return
     */
    public static byte[] getBuffer(byte[] bdz, String str) {
        int sendL = 0;
        int i = 0;

        String bz = str;				// 数据标识
        String strCortrol = "";
        String strLength = "";

        int bzLength = bz.length();
        if (bzLength == 4){ 			// 97协议
            sendL = 18;
            strCortrol = "01";			// 控制码
            strLength = "02";			// 数据域长度 -- (应为是请,没有附带有用的数据--只有数据标识)


        } else if (bzLength == 8){ 		// 07协议
            sendL = 20;
            strCortrol = "11";			// 控制码
            strLength = "04";			// 数据域长度 -- (应为是请,没有附带有用的数据--只有数据标识)
        } else {
            return null;
        }

        byte[] sendBuffer = new byte[sendL];

        sendBuffer[0] = (byte) 0xFE;
        sendBuffer[1] = (byte) 0xFE;
        sendBuffer[2] = (byte) 0xFE;
        sendBuffer[3] = (byte) 0xFE;
        sendBuffer[4] = 0x68;

        // 表地址						// 这个就要注意 -- 地址要从"低到高"
        sendBuffer[5] = bdz[5];
        sendBuffer[6] = bdz[4];
        sendBuffer[7] = bdz[3];
        sendBuffer[8] = bdz[2];
        sendBuffer[9] = bdz[1];
        sendBuffer[10] = bdz[0];

        sendBuffer[11] = 0x68;

        // 控制码
        sendBuffer[12] = Tools.hexString2Bytes(strCortrol)[0];

        //数据域长度
        sendBuffer[13] = Tools.hexString2Bytes(strLength)[0];

        // 数据域 -- 都要 + 0x33
        for (i = 1; i <= Integer.parseInt(strLength.substring(1, 2)); i++) {
            int j = 2 * (Integer.parseInt(strLength.substring(1, 2)) - i);
            sendBuffer[13 + i] = (byte) (Tools.hexString2Bytes(bz.substring(j, j + 2))[0] + 0x33);
        }

        // 校验位
        int sumMod = 0;
        for (i = 4; i <= sendL - 3; i++) {
            sumMod += sendBuffer[i];
        }
        sendBuffer[sendL - 2] = (byte) (sumMod % 256);

        // 结束符
        sendBuffer[sendL - 1] = 0x16;

        //mMeterController.writeCommand("fefefefefefe".getBytes());
        //mInstance.send("fefefefefefe".getBytes());
        // 发送数据 设置延迟10毫秒
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("shen", "sendBuffer === " + Tools.bytesToHexString(sendBuffer));

        return sendBuffer;
    }




    /**
     * 获取表地址 -- 处理
     *
     * @param addr              没加工的表地址
     * @param meterType         97协议 / 07协议
     * @return
     */
    public static byte[] getMeterAddress(String addr, METERTYPE meterType) {

        if(meterType == METERTYPE._97){
            while (addr.length() < 12){
                addr = "A" + addr;
            }
        }else if (meterType == METERTYPE._07){
            while (addr.length() < 12){
                addr = "0" + addr;
            }
        }
        byte[] bdzByte = new byte[6];
        // 取表地址后六位 再在前面拼六个A
        String bdzString = addr;

        bdzByte[0] = Tools.hexString2Bytes(bdzString.substring(0, 2))[0];
        bdzByte[1] = Tools.hexString2Bytes(bdzString.substring(2, 4))[0];
        bdzByte[2] = Tools.hexString2Bytes(bdzString.substring(4, 6))[0];
        bdzByte[3] = Tools.hexString2Bytes(bdzString.substring(6, 8))[0];
        bdzByte[4] = Tools.hexString2Bytes(bdzString.substring(8, 10))[0];
        bdzByte[5] = Tools.hexString2Bytes(bdzString.substring(10, 12))[0];

        return bdzByte;
    }


    /**
     * 解析地址
     *
     * @param str
     * @return
     */
    public static String parseAddr(String str) {
        String addr = "AAAAAAAAAAAA";
        //String addr = "000000000000";
        //str = "000000000622";

        if(str.length() == 14){
            addr = str.substring(8, 14);            // 因为其他原因取 后6位

        }else if(str.length() == 24){
            addr = str.substring(12, 24);
        }else {

            if(str.length() >= 12){
                addr = str.substring(str.length() - 12, str.length());
            }else if(str.length() < 12){
                addr = str;
            }
        }

        LogUtils.i("addr:"+addr + "--addr.length():" + addr.length());

        return addr;
    }

    /**
     * 根据 时间差、协议类型,来获取日冻结的"数据标识"
     *
     * @param timeIn
     * @param timeScan
     * @param type
     * @return
     */
    public static String getMeterAgreementByMonth(String timeIn, String timeScan,
                                                  ElectricMeterParsUtils.METERTYPE type) {
        String meterAgreement = "";
        int i = TimeUtils.getMonthDiff(timeIn, timeScan, "yyyy-MM-dd");

        i += 1;
        switch (i){
            case 1:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9410;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010001;
                break;

            case 2:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9810;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010002;
                break;

            case 3:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9A10;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010003;
                break;

            case 4:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010004;
                break;

            case 5:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010005;
                break;

            case 6:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010006;
                break;

            case 7:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010007;
                break;

            case 8:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010008;
                break;

            case 9:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_00010009;
                break;

            case 10:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0001000A;
                break;

            case 11:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0001000B;
                break;

            case 12:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9010;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0001000C;
                break;

            default:

                break;
        }

        return meterAgreement;

    }

    /**
     * 根据 时间差、协议类型,来获取日冻结的"数据标识"
     *
     * @param timeIn
     * @param timeScan
     * @param type
     * @return
     */
    public static String getMeterAgreementByDay(String timeIn, String timeScan, ElectricMeterParsUtils.METERTYPE type){

        String meterAgreement = "";
        long days = TimeUtils.timeDifference(timeScan, timeIn, "yyyy-MM-dd");
        switch ((int) days){

            case 1:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060101;
                break;
            case 2:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9091;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060102;
                break;
            case 3:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9092;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060103;

                break;
            case 4:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9093;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060104;

                break;
            case 5:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9094;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060105;

                break;
            case 6:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9095;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060106;

                break;
            case 7:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9096;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060107;

                break;
            case 8:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9097;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060108;

                break;
            case 9:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9098;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060109;

                break;
            case 10:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9099;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506010A;

                break;
            case 11:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_909A;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506010B;

                break;
            case 12:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_909B;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506010C;

                break;
            case 13:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_909C;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506010D;

                break;
            case 14:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_909D;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506010E;

                break;
            case 15:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_909E;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506010F;

                break;
            case 16:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060110;

                break;
            case 17:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060111;

                break;
            case 18:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060112;

                break;
            case 19:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060113;

                break;
            case 20:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060114;

                break;
            case 21:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060115;

                break;
            case 22:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060116;

                break;
            case 23:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060117;

                break;
            case 24:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060118;

                break;
            case 25:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060119;

                break;
            case 26:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506011A;

                break;
            case 27:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506011B;

                break;
            case 28:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506011C;

                break;
            case 29:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506011D;

                break;
            case 30:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506011E;

                break;
            case 31:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506011F;

                break;
            case 32:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060120;

                break;
            case 33:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060121;

                break;
            case 34:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060122;

                break;
            case 35:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060123;

                break;
            case 36:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060124;

                break;
            case 37:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060125;

                break;
            case 38:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060127;

                break;
            case 39:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060128;

                break;
            case 40:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060129;

                break;
            case 41:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012A;

                break;
            case 42:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012A;

                break;
            case 43:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012B;

                break;
            case 44:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012C;

                break;
            case 45:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012D;

                break;
            case 46:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012E;

                break;
            case 47:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506012F;

                break;
            case 48:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060130;

                break;
            case 49:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060131;

                break;
            case 50:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060132;

                break;
            case 51:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060133;

                break;
            case 52:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060134;

                break;
            case 53:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060135;

                break;
            case 54:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060136;

                break;
            case 55:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060137;

                break;
            case 56:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060138;

                break;
            case 57:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_05060139;

                break;
            case 58:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506013A;

                break;
            case 59:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506013B;

                break;
            case 60:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506013C;

                break;
            case 61:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506013D;

                break;
            case 62:
                if(type == ElectricMeterParsUtils.METERTYPE._97)
                    meterAgreement = MeterAgreement.Pro97.STR_9090;
                else if(type == ElectricMeterParsUtils.METERTYPE._07)
                    meterAgreement = MeterAgreement.Pro07.STR_0506013E;

                break;


            default:

                break;

        }

        LogUtils.i("type:" + type.toString());
        LogUtils.i("days:" + days);
        LogUtils.i("meterAgreement:" + meterAgreement);

        return meterAgreement;
    }


//    public String removeZero(String num){
//
//        if(num.indexOf(".") > 0){
//            //正则表达
//            num = num.replaceAll("0+?$", ""); //去掉后面无用的零
//            //s = s.replaceAll("[.]$", "");   //如小数点后面全是零则去掉小数点
//        }
//
//        return num;
//    }

}
