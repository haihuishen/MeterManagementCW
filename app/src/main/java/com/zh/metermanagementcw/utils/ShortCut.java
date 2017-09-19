package com.zh.metermanagementcw.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;



/**
 * 在桌面生成图标
 *
 */
public class ShortCut {

//        思路：
//              Launcher为了应用程序能够定制自己的快捷图标，
//              就注册了一个 BroadcastReceiver 专门接收其他应用程序发来的快捷图标定制信息。
//              所以只需要根据该 BroadcastReceiver 构造出相对应的Intent并装入我们的定制信息，
//              最后调用 sendBroadcast 方法就可以创建一个快捷图标了。
//                 
//        步骤：
//        创建快捷方式必须要有权限；
//        创建快捷方式的广播的 Intent 的 action 设置 com.android.launcher.action.INSTALL_SHORTCUT
//        删除快捷方式的广播的 Intent 的 action 设置 com.android.launcher.action.UNINSTALL_SHORTCUT
//        设置快捷方式的图片和名称等信息放在 Intent 中；
//
//              需要添加的权限如下：
//                　　   <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
//                      <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT"/>
//                      <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS"/>
//                      <uses-permission android:name="com.android.launcher2.permission.READ_SETTINGS"/>
//                      <uses-permission android:name="com.android.launcher3.permission.READ_SETTINGS"/>


        //-------------------------------------- 最后使用这种方式 -------------------------------------------
        /**
         * 添加快捷方式
         * */
        public static void creatShortCut(Activity activity, int resourceId) {

                // 获取当前应用名称
                String title = null;
                try {
                        final PackageManager pm = activity.getPackageManager();
                        title = pm.getApplicationLabel(
                                pm.getApplicationInfo(activity.getPackageName(),
                                        PackageManager.GET_META_DATA)).toString();
                } catch (Exception e) {
                }

                Intent intent = new Intent();
                intent.setClass(activity, activity.getClass());
                intent.setAction("android.intent.action.MAIN");         // 以下两句是为了在卸载应用的时候同时删除桌面快捷方式
                intent.addCategory("android.intent.category.LAUNCHER");
                Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

                shortcutintent.putExtra("duplicate", false);                            //不允许重复创建
                shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);      //需要现实的名称
                Parcelable icon = Intent.ShortcutIconResource.fromContext(activity.getApplicationContext(), resourceId); //快捷图片
                shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
                shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);          //点击快捷图片，运行的程序主入口
                activity.sendBroadcast(shortcutintent);                                 //发送广播。OK
        }
        /**
         * 删除快捷方式
         */
        public static void deleteShortCut(Activity activity) {
                Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

                // 获取当前应用名称
                String title = null;
                try {
                        final PackageManager pm = activity.getPackageManager();
                        title = pm.getApplicationLabel(
                                pm.getApplicationInfo(activity.getPackageName(),
                                        PackageManager.GET_META_DATA)).toString();
                } catch (Exception e) {
                }
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);             //快捷方式的名称

                // 在网上看到到的基本都是一下几句，测试的时候发现并不能删除快捷方式。
                // String appClass = activity.getPackageName()+"."+ activity.getLocalClassName();
                // ComponentName comp = new ComponentName( activity.getPackageName(), appClass);
                // shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

                /**
                 * 改成以下方式能够成功删除，估计是删除和创建需要对应才能找到快捷方式并成功删除
                 */
                Intent intent = new Intent();
                intent.setClass(activity, activity.getClass());
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
                activity.sendBroadcast(shortcut);
        }
        /**
         * 判断是否存在快捷方式
         */
        public static boolean hasShortcut(Activity activity){
                String url = "";
                String title = null;
                try {
                        final PackageManager pm = activity.getPackageManager();
                        title = pm.getApplicationLabel(
                                pm.getApplicationInfo(activity.getPackageName(),
                                        PackageManager.GET_META_DATA)).toString();
                } catch (Exception e) {

                }

                /*大于8的时候在com.android.launcher2.settings 里查询（未测试）*/
                if (android.os.Build.VERSION.SDK_INT < 8) {
                        url = "content://com.android.launcher.settings/favorites?notify=true";
                } else if (android.os.Build.VERSION.SDK_INT < 19) {
                        url = "content://com.android.launcher2.settings/favorites?notify=true";
                } else {
                        url = "content://com.android.launcher3.settings/favorites?notify=true";
                }
                ContentResolver resolver = activity.getContentResolver();
                Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",new String[] {title}, null);
                if (cursor != null && cursor.moveToFirst()) {
                        cursor.close();
                        return true;
                }
                return false;
        }
}