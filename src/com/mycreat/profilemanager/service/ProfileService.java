package com.mycreat.profilemanager.service;

import com.mycreat.profilemanager.R;
import com.mycreat.profilemanager.activity.ProfileListActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

public class ProfileService extends Service {

	private static String TAG = "ProfileService";
	
	private int Notification_GOING_ID = 926;
	private int Notification_CANCEL_ID = 927;
	
	private AudioManager mAudioManager;
	//private Vibrator mVibrator;
	
	NotificationManager mNotification;
	
	private int curId = -1;
	private String curName = "";
	private boolean show = true;  //是否显示通知
	private int sound = 2;  //声音提示 1 - 开启; 2 - 关闭； 3 - 只开启声音； 4 - 只开启振动
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//mVibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		//Log.i(TAG, "[生命周期]===>服务：onCreate");
	}
	
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Bundle bundle = intent.getExtras();
		
		//Log.i(TAG, "[生命周期]===>服务：onStart");
		
		if (!bundle.isEmpty()) {
			
			show = bundle.getBoolean("show");
			sound = bundle.getInt("sound");
			
			boolean changed = bundle.getBoolean("changed");
			if (curId == bundle.getInt("id")) 
			{
				if (!changed) {
					//Log.i(TAG, "已开启，不需要再次启动服务！");
					return;
				}
			}
			else curId = bundle.getInt("id");
			curName = bundle.getString("name");
			mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			setAudioRing(bundle.getInt("audioRing"), bundle.getInt("vibrator"));
			setAudioCall(bundle.getInt("audioCall"));
			setAudioAlarm(bundle.getInt("audioAlarm"));
			setAudioMusic(bundle.getInt("audioMusic"));
			//setVibrator(bundle.getInt("vibrator"));
			setAirplane(bundle.getInt("airplane"));
			setWifi(bundle.getInt("wifi"));
			setBluetooth(bundle.getInt("bluetooth"));
			
			if (show) showNotification(1, Notification.FLAG_ONGOING_EVENT, Notification_GOING_ID, bundle.getInt("pic"));
			else {
				try {
					removeNotification(Notification_GOING_ID);
				}
				catch(Exception ex) {
					
				}
			}
			
			//Log.i(TAG, "服务成功开启！");
		}
		else {
			super.onDestroy();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Log.i(TAG, "[生命周期]===>服务：onDestroy");
		try {
			removeNotification(Notification_GOING_ID);
			if (show) showNotification(2, Notification.FLAG_AUTO_CANCEL, Notification_CANCEL_ID, curId);
			else removeNotification(Notification_CANCEL_ID);
		}
		catch (Exception ex) {
			
		}
		
	}
	
	/**
	 * 铃声音量
	 * @param volume
	 * Tips:开静音模式、仅在静音模式下来电振动、始终   铃声模式  1
			开静音模式、一律不振动、非静音   铃声模式  0
			关静音模式、仅在静音模式下来电振动   铃声模式  2
	 */
	public void setAudioRing(int volume, int vibrator) {
		try {
			if (volume == -1 && vibrator == 0) return;  //铃声和震动都保持不变
			else if (volume == -1) {  //仅铃声保持不变
				//Log.i(TAG, "[铃声音量]当前铃声保持不变");
				volume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
			}
			else if (vibrator == 0) {  //仅震动保持不变
				//Log.i(TAG, "[铃声音量]当前振动保持不变");
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
			}
			//Log.i(TAG, "[铃声音量]初始参数===>铃声音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING) + "；振动：" + mAudioManager.getStreamVolume(AudioManager.VIBRATE_SETTING_ON) + "铃声模式：" + mAudioManager.getRingerMode());
			//Log.i(TAG, "[铃声音量]设置参数===>铃声音量：" + volume + "；振动：" + vibrator);
			if (volume == 0){  //静音模式
				//mAudioManager.setVibrateSetting(AudioManager.VIBRATE_SETTING_ON, vibrateSetting);
				if (vibrator == 1) {
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);  //1 - 静音，有振动
					mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);  //开启振动
				}
				else if (vibrator == 2) {
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);  //0 - 静音，无振动
					mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);  //关闭振动
				}
			}
			else {
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  //2 - 正常声音，振动开关由setVibrateSetting决定.
				if (vibrator == 1) {
					mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);  //开启振动
		        } else if (vibrator == 2) {
		        	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);  //关闭振动
		        }
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
			}
			
			//Log.i(TAG, "[铃声音量]设置完成==>铃声音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING) + "铃声模式：" + mAudioManager.getRingerMode());
		}
		catch (Exception ex) {
			//Log.i(TAG, "[铃声音量]设置失败===>铃声音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING) + "铃声模式：" + mAudioManager.getRingerMode());
			//Log.i(TAG, "[铃声音量]错误信息===>" + ex.getMessage());
		}
	}
	
	/**
	 * 通话音量
	 * @param volume 音量大小
	 */
	public void setAudioCall(int volume) {
		try {
			if (volume == -1) return;  //保存不变
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
			if (prev == volume) return;  //相同不作修改
			
			//Log.i(TAG, "[通话音量]初始参数===>通话音量：" + prev);
			//Log.i(TAG, "[通话音量]设置参数===>通话音量：" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volume, 0);
				
			//Log.i(TAG, "[通话音量]设置完成===>通话音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[通话音量]设置失败===>通话音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
			//Log.i(TAG, "[通话音量]错误信息===>" + ex.getMessage());
		}
	}
	
	/**
	 * 闹钟音量
	 * @param volume 音量大小
	 */
	public void setAudioAlarm(int volume) {
		try {
			if (volume == -1) return;  //保存不变
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
			if (prev == volume) return;  //相同不作修改
			
			//Log.i(TAG, "[闹钟音量]初始参数===>闹钟音量：" + prev);
			//Log.i(TAG, "[闹钟音量]设置参数===>闹钟音量：" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
			
			//Log.i(TAG, "[闹钟音量]设置完成===>闹钟音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[闹钟音量]设置失败===>闹钟音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
			//Log.i(TAG, "[闹钟音量]错误信息===>" + ex.getMessage());
		}
	}
	
	/**
	 * 媒体音量
	 * @param volume 音量大小
	 */
	public void setAudioMusic(int volume) {
		try {
			if (volume == -1) return;  //保存不变
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (prev == volume) return;  //相同不作修改
			
			//Log.i(TAG, "[媒体音量]初始参数===>媒体音量：" + prev);
			//Log.i(TAG, "[媒体音量]设置参数===>媒体音量：" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
			
			//Log.i(TAG, "[媒体音量]设置完成===>媒体音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[媒体音量]设置失败===>媒体音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
			//Log.i(TAG, "[媒体音量]错误信息===>" + ex.getMessage());
		}
	}
	
	/**
	 * 系统音量
	 * @param volume 音量大小
	 */
	public void setAudioSystem(int volume) {
		try {
			if (volume == -1) return;  //保存不变
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
			if (prev == volume) return;  //相同不作修改
			
			//Log.i(TAG, "[系统音量]初始参数===>系统音量：" + prev);
			//Log.i(TAG, "[系统音量]设置参数===>系统音量：" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
			
			//Log.i(TAG, "[系统音量]初始参数===>系统音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[系统音量]设置失败===>系统音量：" + mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
			//Log.i(TAG, "[系统音量]设置失败===>" + ex.getMessage());
		}
	}
	
	/**
	 * 飞行模式
	 * @param value 开关状态
	 */
	private void setAirplane(int value) {
		try {
			if (value == 0) return;  //保存不变
			int state = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
			if (state == 0 && value == 2) return;  //都为关闭状态
			if (state == 1 && value == 1) return;  //都为开启状态
			
			//Log.i(TAG, "[飞行模式]初始参数===>" + state);
			//Log.i(TAG, "[飞行模式]设置参数===>" + value);
			
			Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, value == 1 ? 1 : 0);
			// 广播飞行模式信号的改变，让相应的程序可以处理。  
	        // 不发送广播时，在非飞行模式下，Android 2.2.1上测试关闭了Wifi,不关闭正常的通话网络(如GMS/GPRS等)。  
	        // 不发送广播时，在飞行模式下，Android 2.2.1上测试无法关闭飞行模式。
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED); 
			if (value == 1) intent.putExtra("state", true); 
			else intent.putExtra("state", false);
			sendBroadcast(intent);
			
			//Log.i(TAG, "[飞行模式]设置完成===>" + Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[飞行模式]设置失败===>" + Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0));
			//Log.i(TAG, "[飞行模式]错误信息===>" + ex.getMessage());
		}
	}
	
	/**
	 * Wi-Fi开关
	 * @param value 开关状态
	 */
	private void setWifi(int value) {
		try {
			if (value == 0) return;  //保存不变
			boolean state = ((WifiManager)this.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
			if (state == false && value == 2) return;  //都为关闭状态
			if (state == true && value == 1) return;  //都为开启状态
			
			//Log.i(TAG, "[Wi-Fi开关]初始参数===>" + state);
			//Log.i(TAG, "[Wi-Fi开关]设置参数===>" + value);
			
			WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
			if (value == 1) wifiManager.setWifiEnabled(true);
			else wifiManager.setWifiEnabled(false);
			
			//Log.i(TAG, "[Wi-Fi开关]设置完成===>" + ((WifiManager)this.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled());
		}
		catch (Exception ex) {
			//Log.i(TAG, "[Wi-Fi开关]设置失败===>" + ((WifiManager)this.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled());
			//Log.i(TAG, "[Wi-Fi开关]错误信息===>" + ex.getMessage());
		}
	}
	
	/**
	 * 蓝牙开关
	 * @param value 开关状态
	 */
	private void setBluetooth(int value) {
		try {
			if (value == 0) return;  //保存不变
			int state = BluetoothAdapter.getDefaultAdapter().getState();
			if ((state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) && value == 10) return;  //都为关闭状态
			if ((state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_TURNING_ON) && value == 12) return;  //都为开启状态
			
			//Log.i(TAG, "[蓝牙开关]初始参数===>" + state);
			//Log.i(TAG, "[蓝牙开关]设置参数===>" + value);
			
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
			if (value == BluetoothAdapter.STATE_OFF || value == BluetoothAdapter.STATE_TURNING_OFF) mBluetoothAdapter.disable();
			else mBluetoothAdapter.enable();
			//mBluetoothAdapter.enable();
			
			//Log.i(TAG, "[蓝牙开关]设置完成===>" + BluetoothAdapter.getDefaultAdapter().getState());
		}
		catch (Exception ex) {
			//Log.i(TAG, "[蓝牙开关]设置失败===>" + BluetoothAdapter.getDefaultAdapter().getState());
			//Log.i(TAG, "[蓝牙开关]错误信息===>" + ex.getMessage());
		}
	}

	/**
	 * 震动
	 * @param vibrator
	 
	private void setVibrator(int vibrator) {
		if (vibrator == 1) {  //开启
			//根据指定的模式进行震动
            //第一个参数：该数组中第一个元素是等待多长的时间才启动震动，
            //之后将会是开启和关闭震动的持续时间，单位为毫秒
            //第二个参数：重复震动时在pattern中的索引，如果设置为-1则表示不重复震动
            mVibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
            Log.i("服务", "开启震动");
		} else if (vibrator == 2) {  //关闭
			mVibrator.cancel();
			Log.i("服务", "关闭震动");
		}
	}
	*/

	/**
	 * 显示通知栏
	 * @param type
	 * @param flags
	 * @param notifId
	 * @param pic
	 */
	private void showNotification(int type, int flags, int notifId, int pic) {
		String pref = "";
		if (type == 1) pref = "已启动";
		else if (type == 2) pref = "已禁用";
		Intent notifIntent = new Intent(this, ProfileListActivity.class);
		//notifIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//notifIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		notifIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);  //PendingIntent.FLAG_UPDATE_CURRENT
		
		Notification notif;
		int picRes;
		if (pic != -1) picRes = getResources().getIdentifier("notif_" + pic + "_25", "drawable", "com.mycreat.profilemanager");
		else picRes = R.drawable.icon;
		notif = new Notification(picRes, pref + " " + curName, System.currentTimeMillis());
		//notif.defaults |= Notification.DEFAULT_SOUND;
		//notif.defaults |= Notification.DEFAULT_VIBRATE;
		if (sound == 1) {
			notif.defaults |= Notification.DEFAULT_ALL;
		}
		else if (sound == 2) {
		}
		else if (sound == 3) {
			notif.defaults |= Notification.DEFAULT_SOUND;
		}
		else if (sound == 4) {
			notif.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notif.flags |= flags;  //正在进行的
		//Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK
		
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_state);
		contentView.setTextViewText(R.id.tvNotif_name, curName);
		////contentView.setImageViewResource(R.id.imgNotif_icon, picRes);
		
		if (pic != -1) picRes = getResources().getIdentifier("notif_" + pic + "_48", "drawable", "com.mycreat.profilemanager");
		else picRes = R.drawable.icon;
		contentView.setImageViewResource(R.id.imgNotif_icon, picRes);
		
		notif.contentView = contentView;
		notif.contentIntent = contentIntent;
		
		mNotification = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);  //得到NotificationManager
		mNotification.notify(notifId, notif);
		
	}
	
	/**
	 * 移除通知栏
	 * @param notifId 通知Id
	 */
	private void removeNotification(int notifId) {
		mNotification.cancel(notifId);
	}
}
