package com.zh.metermanagementcw.utils;

import android.content.Context;


/**
 * 获取资源id
 */
public class ResourceUtil {

	public static int getLayoutResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "layout",
				context.getPackageName());
	}

	public static int getIdResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "id",
				context.getPackageName());
	}

	// ResourceUtil.getStringResIDByName(this, "app_name")
	public static int getStringResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "string",
				context.getPackageName());
	}

	public static int getColorResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "color",
				context.getPackageName());
	}

	public static int getDrawableResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}

	public static int getMipmapResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "mipmap",
				context.getPackageName());
	}

	public static int getRawResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "raw",
				context.getPackageName());
	}
}
