package com.mycreat.profilemanager.db;

import android.provider.BaseColumns;

public interface ProfileTable extends BaseColumns{
	public static final String TB_NAME = "tb_profile";
	public static final String NAME = "name";
	public static final String PIC = "pic";
	public static final String AUDIO_RING = "audioRing";
	public static final String AUDIO_CALL = "audioCall";
	public static final String AUDIO_ALARM = "audioAlarm";
	public static final String AUDIO_MUSIC = "audioMusic";
	public static final String VIBRATOR = "vibrator";
	public static final String AIRPLANE = "airplane";
	public static final String WIFI = "wifi";
	public static final String BLUETOOTH = "bluetooth";
	public static final String CREATETIME = "createTime";
	public static final String UPDATETIME = "updateTime";
	public static final String ISACTIVE = "isActive";
	public static final String USED = "used";
}