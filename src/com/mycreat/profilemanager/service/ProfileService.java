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
	private boolean show = true;  //�Ƿ���ʾ֪ͨ
	private int sound = 2;  //������ʾ 1 - ����; 2 - �رգ� 3 - ֻ���������� 4 - ֻ������
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//mVibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		//Log.i(TAG, "[��������]===>����onCreate");
	}
	
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Bundle bundle = intent.getExtras();
		
		//Log.i(TAG, "[��������]===>����onStart");
		
		if (!bundle.isEmpty()) {
			
			show = bundle.getBoolean("show");
			sound = bundle.getInt("sound");
			
			boolean changed = bundle.getBoolean("changed");
			if (curId == bundle.getInt("id")) 
			{
				if (!changed) {
					//Log.i(TAG, "�ѿ���������Ҫ�ٴ���������");
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
			
			//Log.i(TAG, "����ɹ�������");
		}
		else {
			super.onDestroy();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Log.i(TAG, "[��������]===>����onDestroy");
		try {
			removeNotification(Notification_GOING_ID);
			if (show) showNotification(2, Notification.FLAG_AUTO_CANCEL, Notification_CANCEL_ID, curId);
			else removeNotification(Notification_CANCEL_ID);
		}
		catch (Exception ex) {
			
		}
		
	}
	
	/**
	 * ��������
	 * @param volume
	 * Tips:������ģʽ�����ھ���ģʽ�������񶯡�ʼ��   ����ģʽ  1
			������ģʽ��һ�ɲ��񶯡��Ǿ���   ����ģʽ  0
			�ؾ���ģʽ�����ھ���ģʽ��������   ����ģʽ  2
	 */
	public void setAudioRing(int volume, int vibrator) {
		try {
			if (volume == -1 && vibrator == 0) return;  //�������𶯶����ֲ���
			else if (volume == -1) {  //���������ֲ���
				//Log.i(TAG, "[��������]��ǰ�������ֲ���");
				volume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
			}
			else if (vibrator == 0) {  //���𶯱��ֲ���
				//Log.i(TAG, "[��������]��ǰ�񶯱��ֲ���");
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
			}
			//Log.i(TAG, "[��������]��ʼ����===>����������" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING) + "���񶯣�" + mAudioManager.getStreamVolume(AudioManager.VIBRATE_SETTING_ON) + "����ģʽ��" + mAudioManager.getRingerMode());
			//Log.i(TAG, "[��������]���ò���===>����������" + volume + "���񶯣�" + vibrator);
			if (volume == 0){  //����ģʽ
				//mAudioManager.setVibrateSetting(AudioManager.VIBRATE_SETTING_ON, vibrateSetting);
				if (vibrator == 1) {
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);  //1 - ����������
					mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);  //������
				}
				else if (vibrator == 2) {
					mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);  //0 - ����������
					mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);  //�ر���
				}
			}
			else {
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  //2 - �����������񶯿�����setVibrateSetting����.
				if (vibrator == 1) {
					mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);  //������
		        } else if (vibrator == 2) {
		        	mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);  //�ر���
		        }
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
				mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
			}
			
			//Log.i(TAG, "[��������]�������==>����������" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING) + "����ģʽ��" + mAudioManager.getRingerMode());
		}
		catch (Exception ex) {
			//Log.i(TAG, "[��������]����ʧ��===>����������" + mAudioManager.getStreamVolume(AudioManager.STREAM_RING) + "����ģʽ��" + mAudioManager.getRingerMode());
			//Log.i(TAG, "[��������]������Ϣ===>" + ex.getMessage());
		}
	}
	
	/**
	 * ͨ������
	 * @param volume ������С
	 */
	public void setAudioCall(int volume) {
		try {
			if (volume == -1) return;  //���治��
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
			if (prev == volume) return;  //��ͬ�����޸�
			
			//Log.i(TAG, "[ͨ������]��ʼ����===>ͨ��������" + prev);
			//Log.i(TAG, "[ͨ������]���ò���===>ͨ��������" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volume, 0);
				
			//Log.i(TAG, "[ͨ������]�������===>ͨ��������" + mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[ͨ������]����ʧ��===>ͨ��������" + mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
			//Log.i(TAG, "[ͨ������]������Ϣ===>" + ex.getMessage());
		}
	}
	
	/**
	 * ��������
	 * @param volume ������С
	 */
	public void setAudioAlarm(int volume) {
		try {
			if (volume == -1) return;  //���治��
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
			if (prev == volume) return;  //��ͬ�����޸�
			
			//Log.i(TAG, "[��������]��ʼ����===>����������" + prev);
			//Log.i(TAG, "[��������]���ò���===>����������" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
			
			//Log.i(TAG, "[��������]�������===>����������" + mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[��������]����ʧ��===>����������" + mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
			//Log.i(TAG, "[��������]������Ϣ===>" + ex.getMessage());
		}
	}
	
	/**
	 * ý������
	 * @param volume ������С
	 */
	public void setAudioMusic(int volume) {
		try {
			if (volume == -1) return;  //���治��
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (prev == volume) return;  //��ͬ�����޸�
			
			//Log.i(TAG, "[ý������]��ʼ����===>ý��������" + prev);
			//Log.i(TAG, "[ý������]���ò���===>ý��������" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
			
			//Log.i(TAG, "[ý������]�������===>ý��������" + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[ý������]����ʧ��===>ý��������" + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
			//Log.i(TAG, "[ý������]������Ϣ===>" + ex.getMessage());
		}
	}
	
	/**
	 * ϵͳ����
	 * @param volume ������С
	 */
	public void setAudioSystem(int volume) {
		try {
			if (volume == -1) return;  //���治��
			int prev = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
			if (prev == volume) return;  //��ͬ�����޸�
			
			//Log.i(TAG, "[ϵͳ����]��ʼ����===>ϵͳ������" + prev);
			//Log.i(TAG, "[ϵͳ����]���ò���===>ϵͳ������" + volume);
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
			
			//Log.i(TAG, "[ϵͳ����]��ʼ����===>ϵͳ������" + mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[ϵͳ����]����ʧ��===>ϵͳ������" + mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
			//Log.i(TAG, "[ϵͳ����]����ʧ��===>" + ex.getMessage());
		}
	}
	
	/**
	 * ����ģʽ
	 * @param value ����״̬
	 */
	private void setAirplane(int value) {
		try {
			if (value == 0) return;  //���治��
			int state = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
			if (state == 0 && value == 2) return;  //��Ϊ�ر�״̬
			if (state == 1 && value == 1) return;  //��Ϊ����״̬
			
			//Log.i(TAG, "[����ģʽ]��ʼ����===>" + state);
			//Log.i(TAG, "[����ģʽ]���ò���===>" + value);
			
			Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, value == 1 ? 1 : 0);
			// �㲥����ģʽ�źŵĸı䣬����Ӧ�ĳ�����Դ���  
	        // �����͹㲥ʱ���ڷǷ���ģʽ�£�Android 2.2.1�ϲ��Թر���Wifi,���ر�������ͨ������(��GMS/GPRS��)��  
	        // �����͹㲥ʱ���ڷ���ģʽ�£�Android 2.2.1�ϲ����޷��رշ���ģʽ��
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED); 
			if (value == 1) intent.putExtra("state", true); 
			else intent.putExtra("state", false);
			sendBroadcast(intent);
			
			//Log.i(TAG, "[����ģʽ]�������===>" + Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0));
		}
		catch (Exception ex) {
			//Log.i(TAG, "[����ģʽ]����ʧ��===>" + Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0));
			//Log.i(TAG, "[����ģʽ]������Ϣ===>" + ex.getMessage());
		}
	}
	
	/**
	 * Wi-Fi����
	 * @param value ����״̬
	 */
	private void setWifi(int value) {
		try {
			if (value == 0) return;  //���治��
			boolean state = ((WifiManager)this.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
			if (state == false && value == 2) return;  //��Ϊ�ر�״̬
			if (state == true && value == 1) return;  //��Ϊ����״̬
			
			//Log.i(TAG, "[Wi-Fi����]��ʼ����===>" + state);
			//Log.i(TAG, "[Wi-Fi����]���ò���===>" + value);
			
			WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
			if (value == 1) wifiManager.setWifiEnabled(true);
			else wifiManager.setWifiEnabled(false);
			
			//Log.i(TAG, "[Wi-Fi����]�������===>" + ((WifiManager)this.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled());
		}
		catch (Exception ex) {
			//Log.i(TAG, "[Wi-Fi����]����ʧ��===>" + ((WifiManager)this.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled());
			//Log.i(TAG, "[Wi-Fi����]������Ϣ===>" + ex.getMessage());
		}
	}
	
	/**
	 * ��������
	 * @param value ����״̬
	 */
	private void setBluetooth(int value) {
		try {
			if (value == 0) return;  //���治��
			int state = BluetoothAdapter.getDefaultAdapter().getState();
			if ((state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) && value == 10) return;  //��Ϊ�ر�״̬
			if ((state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_TURNING_ON) && value == 12) return;  //��Ϊ����״̬
			
			//Log.i(TAG, "[��������]��ʼ����===>" + state);
			//Log.i(TAG, "[��������]���ò���===>" + value);
			
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
			if (value == BluetoothAdapter.STATE_OFF || value == BluetoothAdapter.STATE_TURNING_OFF) mBluetoothAdapter.disable();
			else mBluetoothAdapter.enable();
			//mBluetoothAdapter.enable();
			
			//Log.i(TAG, "[��������]�������===>" + BluetoothAdapter.getDefaultAdapter().getState());
		}
		catch (Exception ex) {
			//Log.i(TAG, "[��������]����ʧ��===>" + BluetoothAdapter.getDefaultAdapter().getState());
			//Log.i(TAG, "[��������]������Ϣ===>" + ex.getMessage());
		}
	}

	/**
	 * ��
	 * @param vibrator
	 
	private void setVibrator(int vibrator) {
		if (vibrator == 1) {  //����
			//����ָ����ģʽ������
            //��һ���������������е�һ��Ԫ���ǵȴ��೤��ʱ��������𶯣�
            //֮�󽫻��ǿ����͹ر��𶯵ĳ���ʱ�䣬��λΪ����
            //�ڶ����������ظ���ʱ��pattern�е��������������Ϊ-1���ʾ���ظ���
            mVibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
            Log.i("����", "������");
		} else if (vibrator == 2) {  //�ر�
			mVibrator.cancel();
			Log.i("����", "�ر���");
		}
	}
	*/

	/**
	 * ��ʾ֪ͨ��
	 * @param type
	 * @param flags
	 * @param notifId
	 * @param pic
	 */
	private void showNotification(int type, int flags, int notifId, int pic) {
		String pref = "";
		if (type == 1) pref = "������";
		else if (type == 2) pref = "�ѽ���";
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
		notif.flags |= flags;  //���ڽ��е�
		//Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK
		
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_state);
		contentView.setTextViewText(R.id.tvNotif_name, curName);
		////contentView.setImageViewResource(R.id.imgNotif_icon, picRes);
		
		if (pic != -1) picRes = getResources().getIdentifier("notif_" + pic + "_48", "drawable", "com.mycreat.profilemanager");
		else picRes = R.drawable.icon;
		contentView.setImageViewResource(R.id.imgNotif_icon, picRes);
		
		notif.contentView = contentView;
		notif.contentIntent = contentIntent;
		
		mNotification = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);  //�õ�NotificationManager
		mNotification.notify(notifId, notif);
		
	}
	
	/**
	 * �Ƴ�֪ͨ��
	 * @param notifId ֪ͨId
	 */
	private void removeNotification(int notifId) {
		mNotification.cancel(notifId);
	}
}
