package com.zh.metermanagementcw.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * Created by shen on 2017/9/7.
 */

public class GetDeviceIDUtil {


    /**
     * 获取设备号
     * @return
     */
    public static String getDeviceNum() {
        return android.os.Build.MODEL;
    }


    /**
     * 获取设备序列号
     *
     * @return
     */
    public static String getSerialNumber(){

        String serial = null;

        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return serial;
    }

    /**
     * 优点：
     *      1.根据不同的手机设备返回IMEI，MEID或者ESN码，唯一性良好 。
     * 不足：
     *      1.非手机：如平板电脑，像这样设备没有通话的硬件功能，系统中也就没有TELEPHONY_SERVICE，自然也就无法获得DEVICE_ID;
     *      2.权限问题：获取DEVICE_ID需要READ_PHONE_STATE权限；
     *      3.厂商定制系统中的Bug：少数手机设备上，由于该实现有漏洞，会返回垃圾，如:00000000或者****
     * @return
     */
    public static String getDeviceId(Context context){

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        return IMEI;
    }


    /**
     * The WLAN MAC Address string
     *  是另一个唯一ID。但是你需要为你的工程加入android.permission.ACCESS_WIFI_STATE 权限，否则这个地址会为null。
     *  WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
     *  String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
     *  Returns: 00:11:22:33:44:55 (这不是一个真实的地址。而且这个地址能轻易地被伪造。).WLan不必打开，就可读取些值。
     *
     * @return
     */
    public static String getWLAN_MAC(Context context){

        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }

    /**
     * The BT MAC Address string
     *  只在有蓝牙的设备上运行。并且要加入android.permission.BLUETOOTH 权限.
     *
     * @return
     */
    public static String getBT_MAC(){

        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        return m_szBTMAC;
    }



}
