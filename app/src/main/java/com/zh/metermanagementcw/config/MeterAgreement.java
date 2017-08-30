package com.zh.metermanagementcw.config;

/**
 * Created by Administrator on 2017/6/14.
 */

public interface MeterAgreement {

    class Pro97{
        /** 正向有功电度 */
        public static final String STR_9010 = "9010";
        /** 正向无功电度 */
        public static final String STR_9110 = "9110";
        /** 反向有功电度 */
        public static final String STR_9020 = "9020";
        /** 反向无功电度 */
        public static final String STR_9120 = "9120";

        /** 上月正向有功总 */
        public static final String STR_9410 = "9410";
        /** 上上月正向有功总 */
        public static final String STR_9810 = "9810";
        /** 上上上月正向有功总 */
        public static final String STR_9A10 = "9A10";


        /** （上1次）日冻结正向有功总电能 */
        public static final String STR_9090 = "9090";
        /** （上2次）日冻结正向有功总电能 */
        public static final String STR_9091 = "9091";
        /** （上3次）日冻结正向有功总电能 */
        public static final String STR_9092 = "9092";
        /** （上4次）日冻结正向有功总电能 */
        public static final String STR_9093 = "9093";
        /** （上5次）日冻结正向有功总电能 */
        public static final String STR_9094 = "9094";
        /** （上6次）日冻结正向有功总电能 */
        public static final String STR_9095 = "9095";
        /** （上7次）日冻结正向有功总电能 */
        public static final String STR_9096 = "9096";
        /** （上8次）日冻结正向有功总电能 */
        public static final String STR_9097 = "9097";
        /** （上9次）日冻结正向有功总电能 */
        public static final String STR_9098 = "9098";
        /** （上10次）日冻结正向有功总电能 */
        public static final String STR_9099 = "9099";
        /** （上11次）日冻结正向有功总电能 */
        public static final String STR_909A = "909A";
        /** （上12次）日冻结正向有功总电能 */
        public static final String STR_909B = "909B";
        /** （上13次）日冻结正向有功总电能 */
        public static final String STR_909C = "909C";
        /** （上14次）日冻结正向有功总电能 */
        public static final String STR_909D = "909D";
        /** （上15次）日冻结正向有功总电能 */
        public static final String STR_909E = "909E";
        /** （上16次）日冻结正向有功总电能 */
        public static final String STR_909F = "909F";



        /** 日期及星期 -- YYMMDDWW*/
        public static final String STR_C010 = "C010";
        /** 时间 -- hhmmss*/
        public static final String STR_C011 = "C011";


        /** A相电压 */
        public static final String STR_B611 = "B611";
        /** B相电压 */
        public static final String STR_B612 = "B612";
        /** C相电压 */
        public static final String STR_B613 = "B613";
        /** A相电流 */
        public static final String STR_B621 = "B621";
        /** B相电流 */
        public static final String STR_B622 = "B622";
        /** C相电流 */
        public static final String STR_B623 = "B623";

        /** 有功功率 */
        public static final String STR_B630 = "B630";
        /** A相有功功率 */
        public static final String STR_B631 = "B631";
        /** B相有功功率 */
        public static final String STR_B632 = "B632";
        /** C相有功功率 */
        public static final String STR_B633 = "B633";

        /** 无功功率 */
        public static final String STR_B640 = "B640";
        /** A相无功功率 */
        public static final String STR_B641 = "B641";
        /** B相无功功率 */
        public static final String STR_B642 = "B642";
        /** C相无功功率 */
        public static final String STR_B643 = "B643";

        /** 总功率因数 */
        public static final String STR_B650 = "B650";
        /** A相功率因数 */
        public static final String STR_B651 = "B651";
        /** B相功率因数 */
        public static final String STR_B652 = "B652";
        /** C相功率因数 */
        public static final String STR_B653 = "B653";


    }


    class Pro07{
        /** 组合有功总电量 */
        public static final String STR_00000000 = "00000000";

        /** 当前正向有功总电量 */
        public static final String STR_00010000 = "00010000";
        /** 正向有功尖电量 */
        public static final String STR_00010100 = "00010100";
        /** 正向有功峰电量 */
        public static final String STR_00010200 = "00010200";
        /** 正向有功平电量 */
        public static final String STR_00010300 = "00010300";
        /** 正向有功谷电量 */
        public static final String STR_00010400 = "00010400";

        /** 反向有功电度 */
        public static final String STR_00020000 = "00020000";
        /** 反向有功尖电量 */
        public static final String STR_00020100 = "00020100";
        /** 反向有功峰电量 */
        public static final String STR_00020200 = "00020200";
        /** 反向有功平电量 */
        public static final String STR_00020300 = "00020300";
        /** 反向有功谷电量 */
        public static final String STR_00020400 = "00020400";

//        <item>00000000 | 组合有功总电量</item>
//        <item>00010000 | 当前正向有功总电量</item>
//        <item>00010100 | 正向有功尖电量</item>
//        <item>00010200 | 正向有功峰电量</item>
//        <item>00010300 | 正向有功平电量</item>
//        <item>00010400 | 正向有功谷电量</item>
//        <item>00020000 | 反向有功总电量</item>
//        <item>00020100 | 正向有功尖电量</item>
//        <item>00020200 | 反向有功峰电量</item>
//        <item>00020300 | 反向有功平电量</item>
//        <item>00020400 | 反向有功谷电量</item>

        /** A相电压 */
        public static final String STR_02010100 = "02010100";
        /** B相电压 */
        public static final String STR_02010200 = "02010200";
        /** C相电压 */
        public static final String STR_02010300 = "02010300";
        /** A相电流 */
        public static final String STR_02020100 = "02020100";
        /** B相电流 */
        public static final String STR_02020200 = "02020200";
        /** C相电流 */
        public static final String STR_02020300 = "02020300";

        /** 有功功率 */
        public static final String STR_02030000 = "02030000";
        /** A相有功功率 */
        public static final String STR_02030100 = "02030100";
        /** B相有功功率 */
        public static final String STR_02030200 = "02030200";
        /** C相有功功率 */
        public static final String STR_02030300 = "02030300";

        /** 无功功率 */
        public static final String STR_02040000 = "02040000";
        /** A相无功功率 */
        public static final String STR_02040100 = "02040100";
        /** B相无功功率 */
        public static final String STR_02040200 = "02040200";
        /** C相无功功率 */
        public static final String STR_02040300 = "02040300";

        /** 总功率因数 */
        public static final String STR_02060000 = "02060000";
        /** A相功率因数 */
        public static final String STR_02060100 = "02060100";
        /** B相功率因数 */
        public static final String STR_02060200 = "02060200";
        /** C相功率因数 */
        public static final String STR_02060300 = "02060300";

        /** 频率 */
        public static final String STR_02800002 = "02800002";


        /** 日期及星期 -- YYMMDDWW */
        public static final String STR_04000101 = "04000101";
        /** 时间 -- hhmmss */
        public static final String STR_04000102 = "04000102";


        /** （上1次）日冻结正向有功总电能 */
        public static final String STR_05060101 = "05060101";
        /** （上2次）日冻结正向有功总电能 */
        public static final String STR_05060102 = "05060102";
        /** （上3次）日冻结正向有功总电能 */
        public static final String STR_05060103 = "05060103";
        /** （上4次）日冻结正向有功总电能 */
        public static final String STR_05060104 = "05060104";
        /** （上5次）日冻结正向有功总电能 */
        public static final String STR_05060105 = "05060105";
        /** （上6次）日冻结正向有功总电能 */
        public static final String STR_05060106 = "05060106";
        /** （上7次）日冻结正向有功总电能 */
        public static final String STR_05060107 = "05060107";
        /** （上8次）日冻结正向有功总电能 */
        public static final String STR_05060108 = "05060108";
        /** （上9次）日冻结正向有功总电能 */
        public static final String STR_05060109 = "05060109";
        /** （上10次）日冻结正向有功总电能 */
        public static final String STR_0506010A = "0506010A";
        /** （上11次）日冻结正向有功总电能 */
        public static final String STR_0506010B = "0506010B";
        /** （上12次）日冻结正向有功总电能 */
        public static final String STR_0506010C = "0506010C";
        /** （上13次）日冻结正向有功总电能 */
        public static final String STR_0506010D = "0506010D";
        /** （上14次）日冻结正向有功总电能 */
        public static final String STR_0506010E = "0506010E";
        /** （上15次）日冻结正向有功总电能 */
        public static final String STR_0506010F = "0506010F";

        /** （上16次）日冻结正向有功总电能 */
        public static final String STR_05060110 = "05060110";
        /** （上17次）日冻结正向有功总电能 */
        public static final String STR_05060111 = "05060111";
        /** （上18次）日冻结正向有功总电能 */
        public static final String STR_05060112 = "05060112";
        /** （上19次）日冻结正向有功总电能 */
        public static final String STR_05060113 = "05060113";
        /** （上20次）日冻结正向有功总电能 */
        public static final String STR_05060114 = "05060115";
        /** （上21次）日冻结正向有功总电能 */
        public static final String STR_05060115 = "05060115";
        /** （上22次）日冻结正向有功总电能 */
        public static final String STR_05060116 = "05060116";
        /** （上23次）日冻结正向有功总电能 */
        public static final String STR_05060117 = "05060117";
        /** （上24次）日冻结正向有功总电能 */
        public static final String STR_05060118 = "05060118";
        /** （上25次）日冻结正向有功总电能 */
        public static final String STR_05060119 = "05060119";
        /** （上26次）日冻结正向有功总电能 */
        public static final String STR_0506011A = "0506011A";
        /** （上27次）日冻结正向有功总电能 */
        public static final String STR_0506011B = "0506011B";
        /** （上28次）日冻结正向有功总电能 */
        public static final String STR_0506011C = "0506011C";
        /** （上29次）日冻结正向有功总电能 */
        public static final String STR_0506011D = "0506011D";
        /** （上30次）日冻结正向有功总电能 */
        public static final String STR_0506011E = "0506011E";
        /** （上31次）日冻结正向有功总电能 */
        public static final String STR_0506011F = "0506011F";


        /** （上32次）日冻结正向有功总电能 */
        public static final String STR_05060120 = "05060120";
        /** （上33次）日冻结正向有功总电能 */
        public static final String STR_05060121 = "05060121";
        /** （上34次）日冻结正向有功总电能 */
        public static final String STR_05060122 = "05060122";
        /** （上35次）日冻结正向有功总电能 */
        public static final String STR_05060123 = "05060123";
        /** （上36次）日冻结正向有功总电能 */
        public static final String STR_05060124 = "05060124";
        /** （上37次）日冻结正向有功总电能 */
        public static final String STR_05060125 = "05060125";
        /** （上38次）日冻结正向有功总电能 */
        public static final String STR_05060126 = "05060126";
        /** （上39次）日冻结正向有功总电能 */
        public static final String STR_05060127 = "05060127";
        /** （上40次）日冻结正向有功总电能 */
        public static final String STR_05060128 = "05060128";
        /** （上41次）日冻结正向有功总电能 */
        public static final String STR_05060129 = "05060129";
        /** （上42次）日冻结正向有功总电能 */
        public static final String STR_0506012A = "0506012A";
        /** （上43次）日冻结正向有功总电能 */
        public static final String STR_0506012B = "0506012B";
        /** （上44次）日冻结正向有功总电能 */
        public static final String STR_0506012C = "0506012C";
        /** （上45次）日冻结正向有功总电能 */
        public static final String STR_0506012D = "0506012D";
        /** （上46次）日冻结正向有功总电能 */
        public static final String STR_0506012E = "0506012E";
        /** （上47次）日冻结正向有功总电能 */
        public static final String STR_0506012F = "0506012F";
        /** （上48次）日冻结正向有功总电能 */
        public static final String STR_05060130 = "05060130";
        /** （上49次）日冻结正向有功总电能 */
        public static final String STR_05060131 = "05060131";
        /** （上50次）日冻结正向有功总电能 */
        public static final String STR_05060132 = "05060132";
        /** （上51次）日冻结正向有功总电能 */
        public static final String STR_05060133 = "05060133";
        /** （上52次）日冻结正向有功总电能 */
        public static final String STR_05060134 = "05060134";
        /** （上53次）日冻结正向有功总电能 */
        public static final String STR_05060135 = "05060135";
        /** （上54次）日冻结正向有功总电能 */
        public static final String STR_05060136 = "05060136";
        /** （上55次）日冻结正向有功总电能 */
        public static final String STR_05060137 = "05060137";
        /** （上56次）日冻结正向有功总电能 */
        public static final String STR_05060138 = "05060138";
        /** （上57次）日冻结正向有功总电能 */
        public static final String STR_05060139 = "05060139";
        /** （上58次）日冻结正向有功总电能 */
        public static final String STR_0506013A = "0506013A";
        /** （上59次）日冻结正向有功总电能 */
        public static final String STR_0506013B = "0506013B";
        /** （上60次）日冻结正向有功总电能 */
        public static final String STR_0506013C = "0506013C";
        /** （上61次）日冻结正向有功总电能 */
        public static final String STR_0506013D = "0506013D";
        /** （上62次）日冻结正向有功总电能 */
        public static final String STR_0506013E = "0506013E";
        // ...上62次




        /** (上1结算月)有功总 */
        public static final String STR_00000001 = "00000001";
        /** (上2结算月)有功总 */
        public static final String STR_00000002 = "00000002";
        /** (上3结算月)有功总 */
        public static final String STR_00000003 = "00000003";
        /** (上4结算月)有功总 */
        public static final String STR_00000004 = "00000004";
        /** (上5结算月)有功总 */
        public static final String STR_00000005 = "00000005";
        /** (上6结算月)有功总 */
        public static final String STR_00000006 = "00000006";
        /** (上7结算月)有功总 */
        public static final String STR_00000007 = "00000007";
        /** (上8结算月)有功总 */
        public static final String STR_00000008 = "00000008";
        /** (上9结算月)有功总 */
        public static final String STR_00000009 = "00000009";
        /** (上10结算月)有功总 */
        public static final String STR_0000000A = "0000000A";
        /** (上11结算月)有功总 */
        public static final String STR_0000000B = "0000000B";
        /** (上12结算月)有功总 */
        public static final String STR_0000000C = "0000000C";


        /** (上1结算月)正向有功总 */
        public static final String STR_00010001 = "00010001";
        /** (上2结算月)正向有功总 */
        public static final String STR_00010002 = "00010002";
        /** (上3结算月)正向有功总 */
        public static final String STR_00010003 = "00010003";
        /** (上4结算月)正向有功总 */
        public static final String STR_00010004 = "00010004";
        /** (上5结算月)正向有功总 */
        public static final String STR_00010005 = "00010005";
        /** (上6结算月)正向有功总 */
        public static final String STR_00010006 = "00010006";
        /** (上7结算月)正向有功总 */
        public static final String STR_00010007 = "00010007";
        /** (上8结算月)正向有功总 */
        public static final String STR_00010008 = "00010008";
        /** (上9结算月)正向有功总 */
        public static final String STR_00010009 = "00010009";
        /** (上10结算月)正向有功总 */
        public static final String STR_0001000A = "0001000A";
        /** (上11结算月)正向有功总 */
        public static final String STR_0001000B = "0001000B";
        /** (上12结算月)正向有功总 */
        public static final String STR_0001000C = "0001000C";

    }

}
